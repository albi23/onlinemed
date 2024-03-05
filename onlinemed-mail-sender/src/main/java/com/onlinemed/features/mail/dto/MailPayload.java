package com.onlinemed.features.mail.dto;


import java.util.Locale;

public record MailPayload(MailBody mail, Locale languageLocale) {
}

