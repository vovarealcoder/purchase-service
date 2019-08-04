package com.vova.purchaseservice.rest.restmodel.response.user;

import java.util.Date;

public class UserActionResponse {
    private final int userId;
    private final String login;
    private final String userName;
    private final Date regTime;

    public UserActionResponse(int userId, String login, String userName, Date regTime) {
        this.userId = userId;
        this.login = login;
        this.userName = userName;
        this.regTime = regTime;
    }

    public String getLogin() {
        return login;
    }

    public String getUserName() {
        return userName;
    }

    public int getUserId() {
        return userId;
    }

    public Date getRegTime() {
        return regTime;
    }
}
