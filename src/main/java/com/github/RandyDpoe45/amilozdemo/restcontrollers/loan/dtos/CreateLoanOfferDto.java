package com.github.RandyDpoe45.amilozdemo.restcontrollers.loan.dtos;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record CreateLoanOfferDto(
        Long offerCreatorId,
        BigDecimal totalAmount,
        Long totalInstallments,
        BigDecimal interestRate,
        String paymentTypeCode,
        String interestTypeCode,
        String interestTimeSpanCode
) {
}
