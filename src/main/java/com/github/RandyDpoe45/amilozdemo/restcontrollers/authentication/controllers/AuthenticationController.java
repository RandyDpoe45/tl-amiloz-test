package com.github.RandyDpoe45.amilozdemo.restcontrollers.authentication.controllers;

import com.github.RandyDpoe45.amilozdemo.restcontrollers.authentication.dtos.LogInRequest;
import com.github.RandyDpoe45.amilozdemo.restcontrollers.authentication.dtos.RefreshTokenRequest;
import com.github.RandyDpoe45.amilozdemo.services.impl.authentication.JwtTokenResult;
import com.github.RandyDpoe45.amilozdemo.services.interfaces.authentication.LogInService;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final LogInService logInService;

    private final JWKSet jwkSet;

    public AuthenticationController(
            LogInService logInService,
            @Qualifier("accessTokenKeyPair") KeyPair keyPair
    ) {
        this.logInService = logInService;
        RSAKey.Builder builder = new RSAKey.Builder((RSAPublicKey) keyPair.getPublic())
                .privateKey(keyPair.getPrivate())
                .keyUse(KeyUse.SIGNATURE)
                .algorithm(JWSAlgorithm.RS256)
                .keyID("bael-key-id");
        this.jwkSet = new JWKSet(builder.build());
    }

    @PostMapping("/login")
    public JwtTokenResult loginUser(
            @RequestBody LogInRequest logInRequest
    ) {
        return logInService.logInUser(logInRequest.username(), logInRequest.password());
    }

    @PostMapping("/refresh")
    public JwtTokenResult refreshToken(
            @RequestBody RefreshTokenRequest refreshTokenRequest
    ) {
        return logInService.refreshUserToken(refreshTokenRequest.refreshToken());
    }

    @GetMapping("/.well-known/jwks.json")
    public Map<String, Object> keys() {
//        return jwkSet.getKeyByKeyId("bael-key-id").toJSONObject();
        return this.jwkSet.toJSONObject();
    }

}
