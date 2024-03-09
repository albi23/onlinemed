package com.onlinemed.config;

public final class KafkaTopicsDefs {

    public static final String MAIL_TOPIC = "onlinemed.queuing.mails-payload.json";
    public static final String MAIL_NOTIFICATION = "onlinemed.queuing.notification-payload.json";
    public static final String GENERAL_DEAD = "onlinemed.queuing.any.json.dead";


    private KafkaTopicsDefs() {
        throw new  IllegalStateException("Not allowed");
    }
}
