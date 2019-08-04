package com.vova.purchaseservice.ex;

public class InvalidCriteriaException extends RuntimeException {
    public InvalidCriteriaException() {
    }

    public InvalidCriteriaException(String message) {
        super(message);
    }

    public InvalidCriteriaException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidCriteriaException(Throwable cause) {
        super(cause);
    }

    public InvalidCriteriaException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
