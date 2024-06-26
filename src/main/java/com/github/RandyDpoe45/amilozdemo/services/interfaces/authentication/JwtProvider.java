package com.github.RandyDpoe45.amilozdemo.services.interfaces.authentication;

import com.github.RandyDpoe45.amilozdemo.persistence.model.user.AuthenticationUser;
import com.github.RandyDpoe45.amilozdemo.services.impl.authentication.JwtTokenResult;

public interface JwtProvider {

    JwtTokenResult createToken(AuthenticationUser user);
}
