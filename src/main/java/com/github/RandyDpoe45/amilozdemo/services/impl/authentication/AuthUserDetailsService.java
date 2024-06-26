package com.github.RandyDpoe45.amilozdemo.services.impl.authentication;

import com.github.RandyDpoe45.amilozdemo.persistence.jparepositories.user.AuthenticationUserJpaRepository;
import com.github.RandyDpoe45.amilozdemo.persistence.model.user.AuthenticationUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthUserDetailsService implements UserDetailsService {

    private final AuthenticationUserJpaRepository authenticationUserJpaRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AuthenticationUser authenticationUser = authenticationUserJpaRepository.findByUsername(username);
        if(Objects.isNull(authenticationUser))
            throw new UsernameNotFoundException("user with username: "+username+" not found");
        return authenticationUser;
    }

}
