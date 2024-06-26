package com.github.RandyDpoe45.amilozdemo.services.impl.authentication;

import com.github.RandyDpoe45.amilozdemo.exceptions.exceptiontypes.AmilozServiceException;
import com.github.RandyDpoe45.amilozdemo.exceptions.exceptiontypes.ExceptionCodesEnum;
import com.github.RandyDpoe45.amilozdemo.persistence.jparepositories.user.AuthenticationUserJpaRepository;
import com.github.RandyDpoe45.amilozdemo.persistence.model.user.AuthenticationUser;
import com.github.RandyDpoe45.amilozdemo.services.interfaces.authentication.JwtProvider;
import com.github.RandyDpoe45.amilozdemo.services.interfaces.authentication.LogInService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class LoginServiceImpl implements LogInService {

    private final JwtProvider jwtProvider;

    private final AuthenticationManager internalAuthenticationManager;

    private final JwtAuthenticationProvider jwtAuthenticationProvider;

    private final AuthenticationUserJpaRepository authenticationUserJpaRepository;

    public LoginServiceImpl(
            JwtProvider jwtProvider,
            @Qualifier("internalAuthenticationManager")
            AuthenticationManager internalAuthenticationManager,
            @Qualifier("jwtRefreshAuthenticationProvider")
            JwtAuthenticationProvider jwtAuthenticationProvider,
            AuthenticationUserJpaRepository authenticationUserJpaRepository
    ) {
        this.jwtAuthenticationProvider = jwtAuthenticationProvider;
        this.internalAuthenticationManager = internalAuthenticationManager;
        this.jwtProvider = jwtProvider;
        this.authenticationUserJpaRepository = authenticationUserJpaRepository;
    }

    @Override
    public JwtTokenResult logInUser(String username, String password) {
        try {
            AuthenticationUser internalUser = authenticationUserJpaRepository.findByUsername(username);
            if (Objects.isNull(internalUser))
                throw new AmilozServiceException(
                        ExceptionCodesEnum.ELEMENT_NOT_FOUND,
                        "Invalid username"
                );
            Authentication auth = internalAuthenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            username,
                            password
                    )
            );
            return jwtProvider.createToken(internalUser);

        } catch (AuthenticationException e) {
            throw new AmilozServiceException(ExceptionCodesEnum.INVALID_CREDENTIALS);
        }
    }

    @Override
    public JwtTokenResult refreshUserToken(String refreshToken) {
        try {
            JwtAuthenticationToken auth = (JwtAuthenticationToken) jwtAuthenticationProvider.authenticate(
                    new BearerTokenAuthenticationToken(
                            refreshToken
                    )
            );
            AuthenticationUser internalUser = authenticationUserJpaRepository.findByUsername(auth.getName());
            if (Objects.isNull(internalUser))
                throw new AmilozServiceException(
                        ExceptionCodesEnum.ELEMENT_NOT_FOUND,
                        "Invalid username"
                );
            return jwtProvider.createToken(internalUser);
        } catch (AuthenticationException e) {
            throw new AmilozServiceException(ExceptionCodesEnum.INVALID_REFRESH_TOKEN);
        }
    }

}
