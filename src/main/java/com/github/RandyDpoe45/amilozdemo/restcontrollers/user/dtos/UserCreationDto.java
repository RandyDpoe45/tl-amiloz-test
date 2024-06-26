package com.github.RandyDpoe45.amilozdemo.restcontrollers.user.dtos;

import lombok.Builder;

@Builder
public record UserCreationDto(
        String username,
        String password,
        String name,
        String lastname,
        String email,
        String documentId
) {
}
