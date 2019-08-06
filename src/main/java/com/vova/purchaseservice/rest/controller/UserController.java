package com.vova.purchaseservice.rest.controller;

import com.vova.purchaseservice.data.model.User;
import com.vova.purchaseservice.data.service.UserService;
import com.vova.purchaseservice.rest.restmodel.request.user.CreateUserRequest;
import com.vova.purchaseservice.rest.restmodel.request.user.EditUserRequest;
import com.vova.purchaseservice.rest.restmodel.response.user.UserActionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/user/")
public class UserController {

    //todo тесты
    //todo логирование

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping(path = "/register", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public UserActionResponse registerUser(@Valid CreateUserRequest request) {
        User user = userService.registerUser(request.getLogin(), request.getPassword(), request.getName());
        return new UserActionResponse(user.getIdUser(), user.getLogin(), user.getName(), user.getRegTime());
    }

    @PostMapping(path = "/edit", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public UserActionResponse edit(@Valid EditUserRequest request) {

        User user = userService.editUser(UserService.getLoginFromSecurityContext(),
                request.getPassword(), request.getName());
        return new UserActionResponse(user.getIdUser(), user.getLogin(), user.getName(), user.getRegTime());
    }

    @GetMapping(path = "/info", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public UserActionResponse edit() {
        User info = userService.info(UserService.getLoginFromSecurityContext());
        return new UserActionResponse(info.getIdUser(), info.getLogin(), info.getName(), info.getRegTime());
    }
}
