package com.onlinemed.servises.impl;


import com.onlinemed.config.KafkaTopicsDefs;
import com.onlinemed.model.dto.Mail;
import com.onlinemed.model.dto.MailPayload;
import com.onlinemed.model.dto.NotificationDto;
import com.onlinemed.servises.api.EmailSendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class EmailSendServiceImpl implements EmailSendService {

    private final SendMailObserver sendMailObserver;
    private final KafkaTemplate<UUID, Object> kafkaTemplate;

    private final Logger logger = LoggerFactory.getLogger(EmailSendServiceImpl.class);

    public EmailSendServiceImpl(SendMailObserver sendMailObserver,
                                KafkaTemplate<UUID, Object> kafkaTemplate) {
        this.sendMailObserver = sendMailObserver;
        this.kafkaTemplate = kafkaTemplate;
    }

    public boolean sendMessageNotificationWithMail(Mail mail, Locale languageLocale, UUID senderId, UUID receiverId) {
        try {
            final boolean wasSent = this.sendMessageMail(mail, languageLocale).get();
            if (wasSent) {
                this.sendMailObserver.notifyObservers(new NotificationDto(senderId, receiverId, mail.getName(), mail.getSurname()));
            }
            return wasSent;
        } catch (ExecutionException | InterruptedException ex) {
            logger.error("Error during sending message with notification", ex);
            return false;
        }

    }



    public CompletableFuture<Boolean> sendMessageMail(Mail mail, Locale languageLocale) {
        var mailPayload = new MailPayload(mail, languageLocale);
        logger.info("Sending {}", mailPayload);
        ListenableFuture<SendResult<UUID, Object>> sent = kafkaTemplate.send(KafkaTopicsDefs.MAIL_TOPIC, UUID.randomUUID(), mailPayload);
        return sent.completable()
                .thenApply((SendResult<UUID, Object> resp) -> true).exceptionally((ex) -> {
                    logger.error("Error during sending message ", ex);
                    return false;
                });
    }

}
