package com.vova.purchaseservice.ex;

public class UserNotFoundException extends RuntimeException {
    private final String login;

    public UserNotFoundException(String login) {
        this.login = login;
    }
}
