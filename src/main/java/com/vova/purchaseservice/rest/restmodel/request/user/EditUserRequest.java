package com.vova.purchaseservice.rest.restmodel.request.user;


import javax.validation.constraints.Size;

public class EditUserRequest {
    @Size(min = 8, max = 20, message = "{validation.password.length}")
    private final String password;

    @Size(min = 5, max = 20, message = "{validation.name.length}")
    private final String name;

    public EditUserRequest(String password, String name) {
        this.password = password;
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

}
