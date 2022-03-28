package com.onlinemed.config.exceptions;


public class TimeOutException extends RuntimeException {
    public TimeOutException() {
    }

    public TimeOutException(String message) {
        super(message);
    }

    public TimeOutException(String message, Throwable rootCause) {
        super(message, rootCause);
    }

    public TimeOutException(Throwable rootCause) {
        super(rootCause);
    }
}
