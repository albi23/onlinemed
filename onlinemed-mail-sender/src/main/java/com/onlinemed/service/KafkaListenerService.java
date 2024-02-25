package com.onlinemed.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static com.onlinemed.config.KafkaTopicsDefs.MAIL_RECEIVE;

@Slf4j
@Service
public class KafkaListenerService {


    @KafkaListener(id = "mail-receive-listener", topics = MAIL_RECEIVE)
    public void listenOnNewMail(String in) {
        log.info(in);
    }
}
