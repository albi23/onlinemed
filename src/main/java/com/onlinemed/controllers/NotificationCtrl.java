package com.onlinemed.controllers;

import com.blueveery.core.ctrls.CreateObjectCtrl;
import com.blueveery.core.ctrls.DeleteObjectCtrl;
import com.blueveery.core.ctrls.UpdateObjectCtrl;
import com.blueveery.core.services.BaseService;
import com.blueveery.scopes.JsonScope;
import com.onlinemed.model.Notification;
import com.onlinemed.model.Person;
import com.onlinemed.model.Visit;
import com.onlinemed.model.dto.Mail;
import com.onlinemed.servises.api.NotificationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;
import java.util.UUID;

import static com.onlinemed.model.SystemFunctionalities.NOTIFICATIONS;

/**
 * Class used to define methods that operate on objects of class Notification
 */
@RestController
@RequestMapping("/api/notification")
@Secured({NOTIFICATIONS})
@JsonScope(positive = true, scope = {Notification.class, Visit.class})
public class NotificationCtrl implements DeleteObjectCtrl<Notification>, CreateObjectCtrl<Notification>,
        UpdateObjectCtrl<Notification> {

    @Autowired
    private NotificationsService notificationsService;

    @Override
    public BaseService<Notification> getService() {
        return notificationsService;
    }


    @RequestMapping(method = {RequestMethod.POST}, consumes = {"application/json"},
            produces = {"application/json"}, path = "/{languageLocale}/")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    @JsonScope(positive = true, scope = {Notification.class, Visit.class, Person.class})
    public Notification createNotificationWithMail(@RequestBody Notification entity,
                                                   @PathVariable("languageLocale") Locale languageLocale,
                                                   @RequestParam(value = "sender") String senderMail) {
        return this.notificationsService.createNotificationWithMail(entity, languageLocale, senderMail);
    }


    @RequestMapping(method = {RequestMethod.POST},
            consumes = {"application/json"},
            produces = {"application/json"},
            path = "decline/{languageLocale}/")
    @ResponseBody
    @JsonScope(positive = true, scope = {Person.class, Mail.class})
    public boolean createMailNotification(@RequestBody Mail mail,
                                          @PathVariable("languageLocale") Locale languageLocale,
                                          @RequestParam(value = "receiverId") UUID receiverId) {
        return this.notificationsService.sendMailNotification(mail, languageLocale, receiverId);
    }

    @RequestMapping(path = {"/visit/{id}"}, method = {RequestMethod.DELETE})
    @JsonScope(positive = true, scope = {Visit.class, Notification.class})
    @ResponseBody
    public int deleteByVisitId(@PathVariable("id") UUID visitId) {
        return this.notificationsService.deleteByVisitId(visitId);
    }


}
