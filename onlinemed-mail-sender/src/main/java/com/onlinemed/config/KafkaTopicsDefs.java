package com.onlinemed.config;

public final class KafkaTopicsDefs {

    public static final String MAIL_RECEIVE = "onlinemed.queuing.received_mails.json";

    private KafkaTopicsDefs() {
       throw new  IllegalStateException("Not allowed");
    }
}
