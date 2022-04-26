package com.onlinemed.servises.impl;


import com.onlinemed.config.exceptions.ValidationException;
import com.onlinemed.model.dto.Mail;
import com.onlinemed.model.dto.Violation;
import com.onlinemed.model.translations.StaticTranslation;
import com.onlinemed.servises.api.EmailSendService;
import com.onlinemed.servises.api.NotificationsService;
import com.onlinemed.servises.api.StaticTranslationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
public class EmailSendServiceImpl implements EmailSendService {

    @Autowired
    private final JavaMailSenderImpl emailSender;

    @Autowired
    private StaticTranslationService staticTranslationService;

    @Autowired
    private NotificationsService notificationsService;

    private final HashMap<Locale, String> cacheTemplates = new HashMap<>();
    private final Logger logger = LoggerFactory.getLogger(EmailSendServiceImpl.class);

    public EmailSendServiceImpl(JavaMailSenderImpl emailSender) {
        this.emailSender = emailSender;
    }

    public boolean sendMessageNotificationWithMail(Mail mail, Locale languageLocale, UUID senderId, UUID receiverId){
        final boolean b = this.sendMessageMail(mail, languageLocale);
        if (b) this.notificationsService.addMessageNotificationToPerson(senderId, receiverId, mail.getName(), mail.getSurname());
        return b;
    }

    public boolean sendMessageMail(Mail mail, Locale languageLocale) {
        try {
            String template = this.cacheTemplates.get(languageLocale);
            if (template == null) {
                template = getNewLanguageTemplate(languageLocale);
            }
            sendMessage(mail, template);
        } catch (MessagingException | MailException ex) {
            logger.error(ex.getMessage());
            if (ex instanceof MailException) {
                throw new ValidationException(new Violation("model.sending-mail-temporarily-unavailable"));
            } else if (ex instanceof AddressException) {
                throw new ValidationException(new Violation("model.incorrect-receiver-mail"));
            }
        }
        return true;
    }


    private void sendMessage(Mail mail, String template) throws MessagingException, MailException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(String.format("%s %s <%s>", mail.getName(), mail.getSurname(), emailSender.getHost()));
        final InternetAddress address = new InternetAddress(mail.getReceiverMail());
        try {
            new InternetAddress(mail.getSenderMail());
        } catch (AddressException ex) {
            throw new ValidationException(new Violation("model.incorrect-sender-mail"));
        }
        message.addRecipient(Message.RecipientType.TO, address);
        final String senderMail = mail.getSenderMail();
        final String body = mail.getBody();
        final String parsedMessage = getBodyWithTemplate(senderMail, body, template);
        helper.setSubject(mail.getSubject());
        helper.setText(parsedMessage, true);
        emailSender.send(message);
    }

    private String getNewLanguageTemplate(Locale languageLocale) {
        final List<StaticTranslation> staticTranslation =
                this.staticTranslationService.getStaticTranslation(languageLocale, "mail", "information", "info-body");
        if (staticTranslation.size() < 2) return this.cacheTemplates.get(Locale.UK);
        String template = this.generateResponseTemplate();
        template = template.replace("$information", staticTranslation.get(0).getName());
        template = template.replace("$info-body", staticTranslation.get(1).getName());
        this.cacheTemplates.put(languageLocale, template);
        return template;
    }

    private String generateResponseTemplate() {
        return "<p>$body</p>" + "<br>" +
                "<p>-------------------- $information --------------</p>" +
                "<pre>$info-body</pre>" +
                "<p style=\"font-weight: bold;color: blue\">$sender</p>";
    }

    private String getBodyWithTemplate(String senderMail, String body, String template) {
        return template.replace("$body", body).replace("$sender", senderMail);
    }
}
