package com.onlinemed.servises.api;

import com.onlinemed.model.Notification;
import com.onlinemed.model.dto.Mail;

import java.util.Locale;
import java.util.UUID;

public interface NotificationsService extends BaseObjectService<Notification> {


   Notification createNotificationWithMail(Notification notification, Locale languageLocale, String senderMail);

   boolean sendMailNotification(Mail mail, Locale languageLocale, UUID receiverId);

   int deleteByVisitId(UUID visitId);

}
