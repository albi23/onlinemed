package com.onlinemed.servises.api;


import com.onlinemed.model.dto.Mail;

import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Interface responsible for sending emails. The interface implementation uses JavaMailSenderImpl (Bean)
 * which is configurable in application.properties
 */
public interface EmailSendService {

    boolean sendMessageNotificationWithMail(Mail mail, Locale languageLocale, UUID senderId, UUID receiverId);

    CompletableFuture<Boolean> sendMessageMail(Mail mail, Locale languageLocale);

}
