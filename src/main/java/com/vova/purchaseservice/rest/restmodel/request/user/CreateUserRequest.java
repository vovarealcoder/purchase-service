package com.vova.purchaseservice.rest.restmodel.request.user;


import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class CreateUserRequest {
    @NotEmpty(message = "{validation.login.empty}")
    @Size(min = 5, max = 20, message = "{validation.login.length}")
    private final String login;

    @NotEmpty(message = "{validation.password.empty}")
    @Size(min = 8, max = 20, message = "{validation.password.length}")
    private final String password;

    @NotEmpty(message = "{validation.name.empty}")
    @Size(min = 5, max = 20, message = "{validation.name.length}")
    private final String name;

    public CreateUserRequest(String login, String password, String name) {
        this.login = login;
        this.password = password;
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

}
