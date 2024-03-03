package com.onlinemed.features.mail.dto;

public record MailPayload(String name,
                          String surname,
                          String receiverMail,
                          String senderMail,
                          String subject,
                          String body) {

}
