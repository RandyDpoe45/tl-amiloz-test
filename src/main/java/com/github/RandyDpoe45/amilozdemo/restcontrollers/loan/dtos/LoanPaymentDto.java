package com.github.RandyDpoe45.amilozdemo.restcontrollers.loan.dtos;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record LoanPaymentDto(
        Long loanInstallmentId,
        BigDecimal amount
) {
}
