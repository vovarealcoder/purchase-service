package com.vova.purchaseservice.ex;

public class NotAuthorizedException extends RuntimeException {
    private static NotAuthorizedException ourInstance = new NotAuthorizedException();

    public NotAuthorizedException() {
    }

    public static NotAuthorizedException getInstance() {
        return ourInstance;
    }
}
