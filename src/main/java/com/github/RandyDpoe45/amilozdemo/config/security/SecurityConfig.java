package com.github.RandyDpoe45.amilozdemo.config.security;

import com.github.RandyDpoe45.amilozdemo.config.startup.ConfigProperties;
import com.github.RandyDpoe45.amilozdemo.persistence.enums.UserRoleEnum;
import com.github.RandyDpoe45.amilozdemo.services.impl.authentication.AuthUserDetailsService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final ConfigProperties configProperties;

    private final JwtDecoder jwtAccessDecoder;

    public JwtAuthenticationProvider jwtAccessAuthenticationProvider;

    public SecurityConfig(
            @Qualifier("jwtAccessDecoder") JwtDecoder jwtAccessDecoder,
            @Qualifier("jwtAccessAuthenticationProvider") JwtAuthenticationProvider jwtAccessAuthenticationProvider,
            ConfigProperties configProperties
    ) {
        this.configProperties = configProperties;
        this.jwtAccessDecoder = jwtAccessDecoder;
        this.jwtAccessAuthenticationProvider = jwtAccessAuthenticationProvider;
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .cors(cors -> cors.disable())
                .csrf(csrf -> csrf.disable())
                .authenticationProvider(jwtAccessAuthenticationProvider)
                .authorizeHttpRequests(req -> {
                            req.requestMatchers(HttpMethod.POST, "/auth/login").permitAll();
                            req.requestMatchers(HttpMethod.POST, "/usuarios").permitAll();
                            req.requestMatchers(HttpMethod.POST, "/usuarios/admin").hasAuthority(UserRoleEnum.ADMIN_ROlE.getCode());
//                            req.requestMatchers(HttpMethod.POST,"/loan").hasAuthority(UserRoleEnum.ADMIN_ROlE.getCode());
                            req.anyRequest().authenticated();
                        }
                ).oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwtConfigurer ->
                                jwtConfigurer.decoder(jwtAccessDecoder)
                                        .jwtAuthenticationConverter(jwtAuthenticationConverter())
                        )

                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }


    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("permissions");
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

    @Bean
    @Primary
    @Qualifier("internalAuthenticationManager")
    public AuthenticationManager internalAuthenticationManager(
            AuthUserDetailsService authUserDetailsService,
            PasswordEncoder passwordEncoder
    ) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(authUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(provider);
    }

}
