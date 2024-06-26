package com.github.RandyDpoe45.amilozdemo.config.passwordhash;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.SecureRandom;

@Configuration
public class PasswordHashConfig {

    @Value("${security-server-config.bcrypt-strength}")
    private int bcryptStrength;

    @Value("${security-server-config.random-seed}")
    private String randomSeed;

    @Bean
    public PasswordEncoder passwordEncoder(){
        SecureRandom sr = new SecureRandom();
        sr.setSeed(randomSeed.getBytes());
        return new BCryptPasswordEncoder(bcryptStrength, sr);
    }

}
