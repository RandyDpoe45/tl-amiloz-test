package com.github.RandyDpoe45.amilozdemo.restcontrollers.authentication.dtos;

import lombok.Builder;

@Builder
public record RefreshTokenRequest(
        String refreshToken
) {
}
