package com.github.RandyDpoe45.amilozdemo.persistence.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PaymentTypeEnum {

    WEEKLY("WEEKLY", 7),
    MONTHLY("MONTHLY", 30);


    private final String code;
    private final int divisor;
}
