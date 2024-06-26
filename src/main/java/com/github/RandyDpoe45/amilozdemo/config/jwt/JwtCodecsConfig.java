package com.github.RandyDpoe45.amilozdemo.config.jwt;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;

import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;

@Configuration
public class JwtCodecsConfig {

    private final KeyPair accessTokenKeyPair;

    private final KeyPair refreshTokenKeyPair;

    private final AccessJwtToUserConverter accessJwtToUserConverter;

    private final RefreshJwtToUserConverter refreshJwtToUserConverter;

    public JwtCodecsConfig(
            @Qualifier("accessTokenKeyPair") KeyPair accessTokenKeyPair,
            @Qualifier("refreshTokenKeyPair") KeyPair refreshTokenKeyPair,
            AccessJwtToUserConverter accessJwtToUserConverter,
            RefreshJwtToUserConverter refreshJwtToUserConverter
    ) {
        this.accessTokenKeyPair = accessTokenKeyPair;
        this.refreshTokenKeyPair = refreshTokenKeyPair;
        this.accessJwtToUserConverter = accessJwtToUserConverter;
        this.refreshJwtToUserConverter = refreshJwtToUserConverter;
    }

    @Bean
    public JwtDecoder jwtAccessDecoder() {
        return NimbusJwtDecoder.withPublicKey((RSAPublicKey) accessTokenKeyPair.getPublic())
                .signatureAlgorithm(SignatureAlgorithm.RS256)
                .build();
    }

    @Bean
    public JwtEncoder jwtAccessEncoder() {
        JWK jwk = new RSAKey
                .Builder((RSAPublicKey) accessTokenKeyPair.getPublic())
                .privateKey(accessTokenKeyPair.getPrivate())
                .build();
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

    @Bean
    @Qualifier("jwtAccessAuthenticationProvider")
    public JwtAuthenticationProvider jwtAccessAuthenticationProvider() {
        JwtAuthenticationProvider provider = new JwtAuthenticationProvider(jwtAccessDecoder());
        provider.setJwtAuthenticationConverter(accessJwtToUserConverter);
        return provider;
    }

    @Bean
    public JwtDecoder jwtRefreshDecoder() {
        return NimbusJwtDecoder.withPublicKey((RSAPublicKey) refreshTokenKeyPair.getPublic()).build();
    }

    @Bean
    public JwtEncoder jwtRefreshEncoder() {
        JWK jwk = new RSAKey
                .Builder((RSAPublicKey) refreshTokenKeyPair.getPublic())
                .privateKey(refreshTokenKeyPair.getPrivate())
                .build();
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

    @Bean
    @Qualifier("jwtRefreshAuthenticationProvider")
    public JwtAuthenticationProvider jwtRefreshAuthenticationProvider() {
        JwtAuthenticationProvider provider = new JwtAuthenticationProvider(jwtRefreshDecoder());
        provider.setJwtAuthenticationConverter(refreshJwtToUserConverter);
        return provider;
    }
}
