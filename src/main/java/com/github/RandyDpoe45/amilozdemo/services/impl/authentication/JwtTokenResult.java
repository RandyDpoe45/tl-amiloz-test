package com.github.RandyDpoe45.amilozdemo.services.impl.authentication;

import lombok.Builder;

@Builder
public record JwtTokenResult(
        Long amilozUserId,
        String accessToken,
        String refreshToken,
        Integer expiresIn
) {
}
