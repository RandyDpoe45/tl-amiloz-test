package com.github.RandyDpoe45.amilozdemo.services.impl.authentication;

import com.github.RandyDpoe45.amilozdemo.persistence.model.user.AuthenticationUser;
import com.github.RandyDpoe45.amilozdemo.services.interfaces.authentication.JwtProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class JwtProviderImpl implements JwtProvider {

    private final JwtEncoder jwtAccessEncoder;


    private final JwtEncoder jwtRefreshEncoder;


    @Value("${security-server-config.jwt.issuer-uri}")
    private String issuerUri;


    @Value("${security-server-config.jwt.access-token-live-time-sec}")
    private int accessTokenLiveTimeSec;

    @Value("${security-server-config.jwt.refresh-token-live-time-sec}")
    private int refreshTokenLiveTimeSec;


    public JwtProviderImpl(
            @Qualifier("jwtAccessEncoder") JwtEncoder jwtAccessEncoder,
            @Qualifier("jwtRefreshEncoder") JwtEncoder jwtRefreshEncoder
    ) {
        this.jwtAccessEncoder = jwtAccessEncoder;
        this.jwtRefreshEncoder = jwtRefreshEncoder;
    }

    private String createAccessToken(
            AuthenticationUser user,
            int expiryTime
    ) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(expiryTime);
        JwtClaimsSet jwtClaims = JwtClaimsSet.builder()
                .subject(user.getUsername())
                .issuer(issuerUri)
                .issuedAt(now)
                .expiresAt(exp)
                .claim("id", user.getId())
                .claim("permissions", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .build();
        return jwtAccessEncoder.encode(JwtEncoderParameters.from(jwtClaims)).getTokenValue();
    }

    private String createRefreshToken(
            AuthenticationUser user,
            int expiryTime
    ) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(expiryTime);
        JwtClaimsSet jwtClaims = JwtClaimsSet.builder()
                .subject(user.getUsername())
                .issuer(issuerUri)
                .issuedAt(now)
                .expiresAt(exp)
                .claim("id", user.getId())
                .build();
        return jwtRefreshEncoder.encode(JwtEncoderParameters.from(jwtClaims)).getTokenValue();
    }

    @Override
    public JwtTokenResult createToken(AuthenticationUser user) {
        return JwtTokenResult.builder()
                .amilozUserId(user.getAmilozUser().getId())
                .accessToken(
                        createAccessToken(
                                user,
                                accessTokenLiveTimeSec
                        )
                )
                .refreshToken(
                        createRefreshToken(
                                user,
                                refreshTokenLiveTimeSec
                        )
                )
                .expiresIn(accessTokenLiveTimeSec)
                .build();
    }

}
