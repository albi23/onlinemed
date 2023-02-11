package com.onlinemed.servises.impl;

import com.onlinemed.model.Notification;
import com.onlinemed.model.Person;
import com.onlinemed.model.Visit;
import com.onlinemed.model.dto.Mail;
import com.onlinemed.model.dto.NotificationDto;
import com.onlinemed.servises.api.EmailSendService;
import com.onlinemed.servises.api.NotificationsService;
import com.onlinemed.servises.api.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

@Service
public class NotificationsServiceImpl extends BaseObjectServiceImpl<Notification> implements NotificationsService, NotificationReceiver {

    private final PersonService personService;
    private final EmailSendService emailSendService;
    private final Logger logger = LoggerFactory.getLogger(NotificationsServiceImpl.class);

    public NotificationsServiceImpl(PersonService personService,
                                    EmailSendService emailSendService,
                                    SendMailObserver sendMailObserver) {
        this.personService = personService;
        this.emailSendService = emailSendService;
        sendMailObserver.addObserver(this);
    }

    @Override
    @Transactional
    public void notificationToPerson(NotificationDto notification) {
        final Person person = this.personService.find(notification.receiverId());
        if (person == null) {
            logger.error(String.format("[%s] Attempt to assign notification for non existing user ID: %s"
                    , new Timestamp(new Date().getTime()), notification.receiverId()));
            return;
        }
        person.getNotifications().add(new Notification(notification.senderId(), person, notification.name(), notification.surname()));
        this.getEntityManager().merge(person);
    }

    @Override
    @Transactional
    public Notification createNotificationWithMail(Notification notification, Locale languageLocale, String senderMail) {
        final Visit visit = notification.getVisit();
        notification.setVisit(new Visit(visit.getVisitType(), visit.getLocalisation(), visit.getOptionalMessage(), visit.getVisitDate()));
        final Notification merge = this.getEntityManager().merge(notification);
        final Person person = merge.getPerson();
        final Mail mail = this.createMail(notification, person.getEmail(), senderMail);
        this.sendMail(mail, languageLocale);
        return merge;
    }

    @Override
    public boolean sendMailNotification(Mail mail, Locale languageLocale, UUID receiverId) {
        final Person person = this.personService.find(receiverId);
        mail.setReceiverMail(person.getEmail());
        return this.sendMail(mail, languageLocale);
    }


    @Override
    @Transactional
    public int deleteByVisitId(UUID visitId) {
        Query notificationDelete = getEntityManager().createQuery("DELETE FROM Notification n WHERE n.visit_id = :visitId");
        final int removedCount = notificationDelete.setParameter("visitId", visitId).executeUpdate();
        Query visitDelete = getEntityManager().createQuery("DELETE FROM Visit v WHERE id = :visitId");
        visitDelete.setParameter("visitId", visitId).executeUpdate();
        return removedCount;
    }

    private Mail createMail(Notification notification, String receiverMail, String senderMail) {
        final Mail mail = new Mail();
        final Visit visit = notification.getVisit();
        mail.setName(notification.getName());
        mail.setSurname(notification.getSurname());
        if (visit != null) {
            final String optionalMessage = visit.getOptionalMessage();
            if (optionalMessage != null && !optionalMessage.isEmpty()) {
                mail.setBody(optionalMessage);
            }
        }
        mail.setReceiverMail(receiverMail);
        mail.setSenderMail(senderMail);
        mail.setSubject("New visit request"); // TODO: Translation
        return mail;
    }

    private boolean sendMail(Mail mail, Locale languageLocale) {
        try {
            return this.emailSendService.sendMessageMail(mail, languageLocale);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            return false;
        }
    }
}
