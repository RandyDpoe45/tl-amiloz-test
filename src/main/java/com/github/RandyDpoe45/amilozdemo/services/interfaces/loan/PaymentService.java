package com.github.RandyDpoe45.amilozdemo.services.interfaces.loan;

import com.github.RandyDpoe45.amilozdemo.persistence.model.loan.Payment;
import com.github.RandyDpoe45.amilozdemo.restcontrollers.loan.dtos.LoanPaymentDto;


public interface PaymentService {
    Payment createPayment(LoanPaymentDto loanPaymentDto);
    Boolean reversePayment(Long paymentId);
}
