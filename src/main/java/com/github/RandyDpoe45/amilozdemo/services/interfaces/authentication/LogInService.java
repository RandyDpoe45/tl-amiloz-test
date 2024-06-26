package com.github.RandyDpoe45.amilozdemo.services.interfaces.authentication;


import com.github.RandyDpoe45.amilozdemo.services.impl.authentication.JwtTokenResult;

public interface LogInService {

    JwtTokenResult logInUser(String username, String password);
    JwtTokenResult refreshUserToken(String refreshedToken);
}
