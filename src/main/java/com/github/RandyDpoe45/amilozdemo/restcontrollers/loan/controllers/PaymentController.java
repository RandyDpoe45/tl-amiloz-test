package com.github.RandyDpoe45.amilozdemo.restcontrollers.loan.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.github.RandyDpoe45.amilozdemo.persistence.model.loan.Payment;
import com.github.RandyDpoe45.amilozdemo.restcontrollers.loan.dtos.LoanPaymentDto;
import com.github.RandyDpoe45.amilozdemo.restcontrollers.loan.dtos.LoanResultViews;
import com.github.RandyDpoe45.amilozdemo.services.interfaces.loan.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/prestamos/pagos")
    @PreAuthorize("hasAuthority('CLIENT')")
    @JsonView(LoanResultViews.LoanPaymentView.class)
    public Payment createPayment(@RequestBody LoanPaymentDto payment) {
        return paymentService.createPayment(payment);
    }

    @PutMapping("/prestamos/pagos/{paymentId}/revertir")
    public boolean revertPayment(@PathVariable("paymentId") Long paymentId) {
        return paymentService.reversePayment(paymentId);
    }
}
