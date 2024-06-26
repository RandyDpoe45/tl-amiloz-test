package com.github.RandyDpoe45.amilozdemo.persistence.enums;

import lombok.Getter;

@Getter
public enum UserRoleEnum {

    ADMIN_ROlE("AMILOZ-ADMIN"),
    CLIENT("CLIENT");


    UserRoleEnum(String code) {
        this.code = code;
    }

    private String code;
}
