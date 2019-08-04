package com.vova.purchaseservice.ex;

public class RegisterUserAlreadyExistsException extends RuntimeException {
    private final String login;

    public RegisterUserAlreadyExistsException(String login) {
        this.login = login;
    }
}
