package com.github.RandyDpoe45.amilozdemo.services.impl.loan;

import com.github.RandyDpoe45.amilozdemo.exceptions.exceptiontypes.AmilozServiceException;
import com.github.RandyDpoe45.amilozdemo.exceptions.exceptiontypes.ExceptionCodesEnum;
import com.github.RandyDpoe45.amilozdemo.persistence.jparepositories.loan.LoanInstallmentJpaRepository;
import com.github.RandyDpoe45.amilozdemo.persistence.jparepositories.loan.PaymentJpaRepository;
import com.github.RandyDpoe45.amilozdemo.persistence.model.loan.LoanInstallment;
import com.github.RandyDpoe45.amilozdemo.persistence.model.loan.Payment;
import com.github.RandyDpoe45.amilozdemo.restcontrollers.loan.dtos.LoanPaymentDto;
import com.github.RandyDpoe45.amilozdemo.services.interfaces.loan.LoanService;
import com.github.RandyDpoe45.amilozdemo.services.interfaces.loan.PaymentService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final LoanInstallmentJpaRepository loanInstallmentJpaRepository;

    private final PaymentJpaRepository paymentJpaRepository;

    private final EntityManager entityManager;

    private final LoanService loanService;

    @Override
    @Transactional
    public Payment createPayment(LoanPaymentDto loanPaymentDto) {
        LoanInstallment loanInstallment = loanInstallmentJpaRepository.findById(loanPaymentDto.loanInstallmentId())
                .orElseThrow(() -> new AmilozServiceException(ExceptionCodesEnum.INSTALLMENT_NOT_FOUND));
        if (!loanInstallment.getIsPending())
            throw new AmilozServiceException(ExceptionCodesEnum.INSTALLMENT_AlREADY_PAID);
        BigDecimal remainingAmount = calculateRemainingAmount(loanInstallment);
        BigDecimal paidAmount = loanPaymentDto.amount().setScale(2, RoundingMode.UP);
        if (paidAmount.compareTo(remainingAmount) > 0)
            throw new AmilozServiceException(ExceptionCodesEnum.PAID_AMOUNT_GREATER_THAN_REMAINING);
        Payment payment = new Payment()
                .setInstallment(loanInstallment)
                .setPaymentDate(LocalDate.now())
                .setAmount(paidAmount)
                .setReversed(false);
        payment = paymentJpaRepository.saveAndFlush(payment);
        entityManager.refresh(loanInstallment);
        updateInstallmentState(loanInstallment);
        loanService.updateLoanPaidState(loanInstallment.getLoan().getId());
        return payment;
    }

    private BigDecimal calculateRemainingAmount(LoanInstallment loanInstallment){
        BigDecimal remainingAmount = loanInstallment.getTotalAmount();
        for (Payment payment : loanInstallment.getPayments()) {
            remainingAmount = remainingAmount.subtract(payment.getAmount());
        }
        return remainingAmount;
    }

    @Override
    @Transactional
    public Boolean reversePayment(Long paymentId) {
        Payment payment = paymentJpaRepository.findById(paymentId)
                .orElseThrow(() -> new AmilozServiceException(ExceptionCodesEnum.PAYMENT_NOT_FOUND));
        payment.setReversed(true);
        paymentJpaRepository.saveAndFlush(payment);
        entityManager.refresh(payment);
        updateInstallmentState(payment.getInstallment());
        loanService.updateLoanPaidState(payment.getInstallment().getLoan().getId());
        return true;
    }

    private void updateInstallmentState(LoanInstallment loanInstallment){
        BigDecimal totalPayed = loanInstallment.getRemainingAmount();
        loanInstallment.setIsPending(totalPayed.compareTo(BigDecimal.ZERO) != 0);
        loanInstallmentJpaRepository.saveAndFlush(loanInstallment);
    }
}
