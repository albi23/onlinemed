package com.onlinemed.model.dto;

/**
 * A class that maps data related to mail
 */
public class Mail {
    private String name;
    private String surname;
    private String receiverMail;
    private String senderMail;
    private String subject;
    private String body;

    /**
     * Gets name
     *
     * @return value of name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets <code>Mail</code> name value
     *
     * @param name - set new value of name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets surname
     *
     * @return value of surname
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Sets <code>Mail</code> surname value
     *
     * @param surname - set new value of surname
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * Gets receiverMail
     *
     * @return value of receiverMail
     */
    public String getReceiverMail() {
        return receiverMail;
    }

    /**
     * Sets <code>Mail</code> receiverMail value
     *
     * @param receiverMail - set new value of receiverMail
     */
    public void setReceiverMail(String receiverMail) {
        this.receiverMail = receiverMail;
    }

    /**
     * Gets senderMail
     *
     * @return value of senderMail
     */
    public String getSenderMail() {
        return senderMail;
    }

    /**
     * Sets <code>Mail</code> senderMail value
     *
     * @param senderMail - set new value of senderMail
     */
    public void setSenderMail(String senderMail) {
        this.senderMail = senderMail;
    }

    /**
     * Gets subject
     *
     * @return value of subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Sets <code>Mail</code> subject value
     *
     * @param subject - set new value of subject
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * Gets body
     *
     * @return value of body
     */
    public String getBody() {
        return body;
    }

    /**
     * Sets <code>Mail</code> body value
     *
     * @param body - set new value of body
     */
    public void setBody(String body) {
        this.body = body;
    }
}
