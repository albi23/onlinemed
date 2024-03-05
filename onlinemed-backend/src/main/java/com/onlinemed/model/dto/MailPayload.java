package com.onlinemed.model.dto;

import java.util.Locale;
import java.util.Objects;

public  class MailPayload {
    private  Mail mail;
    private  Locale languageLocale;


    public MailPayload() {
    }

    public MailPayload(Mail mail, Locale languageLocale) {
        this.mail = mail;
        this.languageLocale = languageLocale;
    }

    public Mail getMail() {
        return mail;
    }

    public Locale getLanguageLocale() {
        return languageLocale;
    }

    public void setMail(Mail mail) {
        this.mail = mail;
    }

    public void setLanguageLocale(Locale languageLocale) {
        this.languageLocale = languageLocale;
    }
}