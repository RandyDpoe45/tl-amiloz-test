package com.github.RandyDpoe45.amilozdemo.restcontrollers.loan.dtos;

import lombok.Builder;

@Builder
public record CreateLoanDto(
        Long loanId
) {
}
