package com.github.RandyDpoe45.amilozdemo.restcontrollers.loan.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.github.RandyDpoe45.amilozdemo.persistence.model.loan.LoanOffer;
import com.github.RandyDpoe45.amilozdemo.restcontrollers.loan.dtos.CreateLoanOfferDto;
import com.github.RandyDpoe45.amilozdemo.restcontrollers.loan.dtos.LoanResultViews;
import com.github.RandyDpoe45.amilozdemo.services.interfaces.loan.LoanOfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("usuarios")
public class LoanOfferController {

    private final LoanOfferService loanOfferService;

    @PostMapping("{userId}/ofertas")
    @JsonView(LoanResultViews.LoanOfferView.class)
    @PreAuthorize("hasAuthority('AMILOZ-ADMIN')")
    public LoanOffer createLoanOffer(
            @PathVariable("userId") Long userId,
            @RequestBody CreateLoanOfferDto createLoanOfferDto
    ) {
        return loanOfferService.create(userId, createLoanOfferDto);
    }

    @GetMapping("{userId}/ofertas")
    @JsonView(LoanResultViews.LoanOfferView.class)
    @PreAuthorize("hasAnyAuthority('AMILOZ-ADMIN','CLIENT')")
    public List<LoanOffer> getClientOffers(
            @PathVariable("userId") Long userId
    ) {
        return loanOfferService.getByUserID(userId);
    }
}
