package com.github.RandyDpoe45.amilozdemo.restcontrollers.loan.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.github.RandyDpoe45.amilozdemo.persistence.model.loan.Loan;
import com.github.RandyDpoe45.amilozdemo.persistence.model.loan.LoanInstallment;
import com.github.RandyDpoe45.amilozdemo.restcontrollers.loan.dtos.LoanResultViews;
import com.github.RandyDpoe45.amilozdemo.services.interfaces.loan.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("usuarios")
public class LoanController {

    private final LoanService loanService;

    @PostMapping("/prestamo/{loanId}")
    @JsonView(LoanResultViews.LoanMasterDetail.class)
    @PreAuthorize("hasAuthority('CLIENT')")
    public Loan create(
            @PathVariable Long loanId
    ) {
        return loanService.createLoanFromOffer(loanId);
    }

    @GetMapping("/{userID}/prestamo")
    @JsonView(LoanResultViews.LoanMasterDetail.class)
    @PreAuthorize("hasAuthority('CLIENT')")
    public List<Loan> getByUserID(
            @PathVariable Long userID
    ) {
        return loanService.getLoansByUserId(userID);
    }

    @GetMapping("/prestamo/{loanId}/cuotas")
    @PreAuthorize("hasAuthority('CLIENT')")
    @JsonView(LoanResultViews.LoanInstallmentView.class)
    public List<LoanInstallment> getInstallments(@PathVariable Long loanId) {
        return loanService.getLoanInstallments(loanId);
    }
}
