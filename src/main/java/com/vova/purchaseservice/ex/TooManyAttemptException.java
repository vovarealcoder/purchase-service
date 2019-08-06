package com.vova.purchaseservice.ex;

import org.springframework.security.core.AuthenticationException;

public class TooManyAttemptException extends AuthenticationException {
    public TooManyAttemptException(String message) {
        super(message);
    }

    public TooManyAttemptException(String message, Throwable cause) {
        super(message, cause);
    }
    public static boolean isTooManyAuthException(Throwable t) {
        Throwable ex = t;
        do {
            if (ex instanceof TooManyAttemptException) {
                return true;
            }
            ex = ex.getCause();
        } while (ex != null);
        return false;
    }
}
