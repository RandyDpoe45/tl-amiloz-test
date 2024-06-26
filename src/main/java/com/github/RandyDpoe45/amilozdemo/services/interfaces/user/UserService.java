package com.github.RandyDpoe45.amilozdemo.services.interfaces.user;

import com.github.RandyDpoe45.amilozdemo.persistence.model.user.AuthenticationUser;
import com.github.RandyDpoe45.amilozdemo.restcontrollers.user.dtos.UserCreationDto;

public interface UserService {
    AuthenticationUser createUser(UserCreationDto user);
    AuthenticationUser createAdminUser(UserCreationDto user);
}
