package com.onlinemed.features.mail;

import com.onlinemed.features.mail.dto.MailPayload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Slf4j
@Service
class KafkaMailListenerService {


    private final MailService emailSender;

    KafkaMailListenerService(MailService emailSender) {
        this.emailSender = emailSender;
    }

    @KafkaListener(id = "mail-receive-listener", topics = KafkaTopicsDefs.MAIL_RECEIVE)
        public void listenOnNewMail(@Payload MailPayload mailBody, Acknowledgment ack) {
        log.info(String.valueOf(mailBody));
        emailSender.sendMessageMail(mailBody);
        ack.acknowledge();
    }


}

