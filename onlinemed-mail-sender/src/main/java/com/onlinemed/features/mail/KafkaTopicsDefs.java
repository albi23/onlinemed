package com.onlinemed.features.mail;

public final class KafkaTopicsDefs {

    public static final String MAIL_RECEIVE = "onlinemed.queuing.mails-payload.json";
    public static final String MAIL_RECEIVE_DED = "onlinemed.queuing.mails-payload.json.dead";
    public static final String GENERAL_DEAD = "onlinemed.queuing.any.json.dead";

    private KafkaTopicsDefs() {
       throw new  IllegalStateException("Not allowed");
    }
}
