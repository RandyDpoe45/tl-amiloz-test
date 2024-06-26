package com.github.RandyDpoe45.amilozdemo.restcontrollers.user.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.github.RandyDpoe45.amilozdemo.persistence.model.user.AuthenticationUser;
import com.github.RandyDpoe45.amilozdemo.restcontrollers.user.dtos.UserCreationDto;
import com.github.RandyDpoe45.amilozdemo.restcontrollers.user.dtos.UserResponseViews;
import com.github.RandyDpoe45.amilozdemo.services.interfaces.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("usuarios")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    @JsonView(UserResponseViews.UserResponseView.class)
    public AuthenticationUser createClientUser(@RequestBody UserCreationDto userCreationDto) {
        return userService.createUser(userCreationDto);
    }

    @PostMapping("admin")
    @JsonView(UserResponseViews.UserResponseView.class)
    public AuthenticationUser createAdminUser(@RequestBody UserCreationDto userCreationDto){
        return userService.createAdminUser(userCreationDto);
    }
}
