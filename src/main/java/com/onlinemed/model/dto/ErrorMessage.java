package com.onlinemed.model.dto;

import com.blueveery.core.model.BaseEntity;

import java.sql.Timestamp;
import java.util.UUID;

public class ErrorMessage {

    /**
     * Error message ID
     */
    private UUID errorID;

    /**
     * the detail message string of this {@code Throwable} instance
     */
    private String message;

    /**
     * StackTrace of errors
     */
    private String stackTrace;// TODO remove it for production

    /**
     * Api url of error
     */
    private String url;

    /**
     * Instance of the BaseEntity class where the error occurred
     */
    private BaseEntity entity;

    /**
     * Time of error occurrence
     */
    private Timestamp timestamp;

    /**
     * Constructs a new error message with param:
     * errorID , message, stackTrace, url, timestamp
     */
    public ErrorMessage(UUID errorID, String message, String stackTrace, String url, Timestamp timestamp) {
        this.errorID = errorID;
        this.message = message;
        this.stackTrace = stackTrace;
        this.url = url;
        this.timestamp = timestamp;
    }

    /**
     * Constructs a new error message with param:
     * message, entity
     */
    public ErrorMessage(String message, BaseEntity entity) {
        this.message = message;
        this.entity = entity;
    }

    /**
     * Constructs a new error message with param:
     * message, url
     */
    public ErrorMessage(String message, String url) {
        this.message = message;
        this.url = url;
    }

    /**
     * Gets message
     *
     * @return value of message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets <code>ErrorMessage</code> message value
     *
     * @param message - set new value of message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Gets url
     *
     * @return value of url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets <code>ErrorMessage</code> url value
     *
     * @param url - set new value of url
     */
    public void setUrl(String url) {
        this.url = url;
    }
}
