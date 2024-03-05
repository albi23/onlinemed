package com.onlinemed.config;

public final class KafkaTopicsDefs {

    public static final String MAIL_TOPIC = "onlinemed.queuing.mails-payload.json";

    private KafkaTopicsDefs() {
        throw new  IllegalStateException("Not allowed");
    }
}
