package com.onlinemed.controllers;

import com.blueveery.core.ctrls.BaseCtrl;
import com.blueveery.core.services.BaseService;
import com.onlinemed.model.BaseObject;
import com.onlinemed.model.dto.Mail;
import com.onlinemed.servises.api.EmailSendService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;
import java.util.UUID;

import static com.onlinemed.model.SystemFunctionalities.NOTIFICATIONS;

/**
 * Class used to define methods that manage the process of sending e-mail notifications
 */
@RestController
@Secured({NOTIFICATIONS})
@RequestMapping("/api/email")
public class EmailSendCtrl implements BaseCtrl<BaseObject> {


    private EmailSendService emailSendService;

    public EmailSendCtrl(EmailSendService emailSendService) {
        this.emailSendService = emailSendService;
    }

    @Override
    public BaseService<BaseObject> getService() {
        throw new UnsupportedOperationException("Method not supported in " + this.getClass().getSimpleName());
    }

    @RequestMapping(path = "/{languageLocale}/send", method = RequestMethod.POST)
    @ResponseBody
    public boolean sendMessageMail(@RequestBody Mail mail,
                                   @PathVariable("languageLocale") Locale languageLocale,
                                   @RequestParam(value = "sender") UUID senderId,
                                   @RequestParam(value = "receiver") UUID receiverId) {
        return this.emailSendService.sendMessageNotificationWithMail(mail, languageLocale, senderId, receiverId);
    }
}
