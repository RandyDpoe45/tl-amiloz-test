package com.github.RandyDpoe45.amilozdemo.exceptions.dtos;

import lombok.Builder;

@Builder
public record ExceptionResponse(
        String response,
        String exceptionCode
) {
}
