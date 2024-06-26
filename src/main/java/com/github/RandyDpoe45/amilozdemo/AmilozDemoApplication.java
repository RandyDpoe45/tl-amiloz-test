package com.github.RandyDpoe45.amilozdemo;

import com.github.RandyDpoe45.amilozdemo.config.startup.ConfigProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableConfigurationProperties(ConfigProperties.class)
@EnableWebSecurity
@EnableMethodSecurity
public class AmilozDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(AmilozDemoApplication.class, args);
    }

}
