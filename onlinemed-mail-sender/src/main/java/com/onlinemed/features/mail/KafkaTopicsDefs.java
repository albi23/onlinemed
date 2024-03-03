package com.onlinemed.features.mail;

public final class KafkaTopicsDefs {

    public static final String MAIL_RECEIVE = "onlinemed.queuing.received_mails.json";

    private KafkaTopicsDefs() {
       throw new  IllegalStateException("Not allowed");
    }
}
