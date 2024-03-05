package com.onlinemed.features.mail;

import com.onlinemed.exception.ValidationException;
import com.onlinemed.exception.Violation;
import com.onlinemed.features.mail.dto.MailBody;
import com.onlinemed.features.mail.dto.MailPayload;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
class MailService {

    public static final String MAIL_TEMPLATE = "<p>$body</p>" + "<br>" +
            "<p>-------------------- $information --------------</p>" +
            "<pre>$info-body</pre>" +
            "<p style=\"font-weight: bold;color: blue\">$sender</p>";

    private final JavaMailSender emailSender;
    private final StaticTranslationService staticTranslation;
    private final ConcurrentHashMap<Locale, String> LOCAL_CACHE = new ConcurrentHashMap<>();

    public MailService(JavaMailSender emailSender, StaticTranslationService staticTranslation) {
        this.emailSender = emailSender;
        this.staticTranslation = staticTranslation;
    }

    public boolean sendMessageMail(MailPayload mail) {
        try {
            var template = LOCAL_CACHE.getOrDefault(mail.languageLocale(), getNewLanguageTemplate(mail.languageLocale()));
            sendMessage(mail.mail(), template);
        } catch (MessagingException | MailException ex) {
            log.error("Error has occurred", ex);
            if (ex instanceof MailException) {
                throw new ValidationException(new Violation("model.sending-mail-temporarily-unavailable"));
            } else if (ex instanceof AddressException) {
                throw new ValidationException(new Violation("model.incorrect-receiver-mail"));
            }
        }
        return true;
    }

    private void sendMessage(MailBody mail, String template) throws MessagingException, MailException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(String.format("%s %s <%s>", mail.name(), mail.surname(), ((JavaMailSenderImpl)emailSender).getHost()));
        final InternetAddress address = new InternetAddress(mail.receiverMail());
        try {
            new InternetAddress(mail.senderMail());
        } catch (AddressException ex) {
            throw new ValidationException(new Violation("model.incorrect-sender-mail"));
        }
        message.addRecipient(Message.RecipientType.TO, address);
        final String parsedMessage = getBodyWithTemplate(mail.senderMail(), mail.body(), template);
        helper.setSubject(mail.subject());
        helper.setText(parsedMessage, true);
        emailSender.send(message);
    }

    private String getBodyWithTemplate(String senderMail, String body, String template) {
        return template.replace("$body", body)
                .replace("$sender", senderMail);
    }

    private String getNewLanguageTemplate(Locale languageLocale) {
        List<String> staticTranslation =
                this.staticTranslation.getTranslations(languageLocale, "mail", List.of("information", "info-body"));
        if (staticTranslation.size() < 2) return LOCAL_CACHE.get(Locale.UK);
        String template = MAIL_TEMPLATE;
        template = template.replace("$information", staticTranslation.get(0));
        template = template.replace("$info-body", staticTranslation.get(1));
        LOCAL_CACHE.put(languageLocale, template);
        return template;
    }

 }
