package com.github.RandyDpoe45.amilozdemo.services.impl.loan;

import com.github.RandyDpoe45.amilozdemo.exceptions.exceptiontypes.AmilozServiceException;
import com.github.RandyDpoe45.amilozdemo.exceptions.exceptiontypes.ExceptionCodesEnum;
import com.github.RandyDpoe45.amilozdemo.persistence.enums.InterestTimeSpanEnum;
import com.github.RandyDpoe45.amilozdemo.persistence.enums.InterestTypeEnum;
import com.github.RandyDpoe45.amilozdemo.persistence.enums.PaymentTypeEnum;
import com.github.RandyDpoe45.amilozdemo.persistence.jparepositories.loan.LoanInstallmentJpaRepository;
import com.github.RandyDpoe45.amilozdemo.persistence.jparepositories.loan.LoanJpaRepository;
import com.github.RandyDpoe45.amilozdemo.persistence.jparepositories.loan.LoanOfferJpaRepository;
import com.github.RandyDpoe45.amilozdemo.persistence.model.loan.Loan;
import com.github.RandyDpoe45.amilozdemo.persistence.model.loan.LoanInstallment;
import com.github.RandyDpoe45.amilozdemo.persistence.model.loan.LoanOffer;
import com.github.RandyDpoe45.amilozdemo.services.interfaces.loan.LoanService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService {

    private final LoanJpaRepository loanJpaRepository;
    private final LoanOfferJpaRepository loanOfferJpaRepository;
    private final LoanInstallmentJpaRepository loanInstallmentJpaRepository;
    private final EntityManager em;

    @Override
    @Transactional
    public Loan createLoanFromOffer(Long loanOfferId) {
        LoanOffer loanOffer = loanOfferJpaRepository.findById(loanOfferId)
                .orElseThrow(() -> new AmilozServiceException(ExceptionCodesEnum.LOAN_OFFER_NOT_FOUND));
        if (loanOffer.getAccepted())
            throw new AmilozServiceException(ExceptionCodesEnum.LOAN_OFFER_ALREADY_ACCEPTED);

        Loan loan = new Loan()
                .setLoanOwner(loanOffer.getAmilozUser())
                .setLoanOffer(loanOffer)
                .setInterestRate(loanOffer.getInterestRate())
                .setTotalInstallments(loanOffer.getTotalInstallments())
                .setTotalAmount(loanOffer.getTotalAmount())
                .setPayedInFull(false)
                .setInterestType(loanOffer.getInterestType())
                .setPaymentType(loanOffer.getPaymentType())
                .setInterestTimeSpan(loanOffer.getInterestTimeSpan());
        loan = loanJpaRepository.save(loan);
        createLoanInstallments(loan);
        loanOffer.setAccepted(true);
        loanOfferJpaRepository.saveAndFlush(loanOffer);
        return loan;
    }

    @Override
    public List<Loan> getLoansByUserId(Long userId) {
        return loanJpaRepository.findByLoanOwnerId(userId);
    }

    @Override
    public List<LoanInstallment> getLoanInstallments(Long loanId) {
        return loanInstallmentJpaRepository.findByLoanIdOrderByPaymentDateAsc(loanId);
    }

    @Override
    public void updateLoanPaidState(Long loanId) {
        Loan loan = loanJpaRepository.findById(loanId).get();
        for (LoanInstallment loanInstallment : loan.getLoanInstallments())
            if (loanInstallment.getIsPending()){
                loan.setPayedInFull(false);
                break;
            }
        loan.setPayedInFull(true);
        loanJpaRepository.saveAndFlush(loan);
    }

    private void createLoanInstallments(Loan loan) {
        LocalDate paymentDate = LocalDate.now();
        BigDecimal baseAmount = loan.getTotalAmount().divide(BigDecimal.valueOf(loan.getTotalInstallments()), 2, RoundingMode.UP);
        for (long i = 0L; i < loan.getTotalInstallments(); i++) {
            paymentDate = deltaDate(paymentDate, loan);
            LoanInstallment installment = new LoanInstallment()
                    .setBaseAmount(baseAmount)
                    .setIsPending(true)
                    .setPaymentDate(paymentDate)
                    .setLoan(loan)
                    .setInterestAmount(calculateInterestAmount(loan));
            loanInstallmentJpaRepository.save(installment);
        }
    }

    private BigDecimal calculateInterestAmount(Loan loan) {
        BigDecimal dailyInterestRate = calculateDailyInterestRate(loan);
        if (loan.getInterestType().getCode().equals(InterestTypeEnum.SIMPLE.getCode()))
            return calculateSimpleInterest(loan, dailyInterestRate);
        return BigDecimal.ZERO;
    }

    private BigDecimal calculateSimpleInterest(Loan loan, BigDecimal dailyInterestRate) {
        BigDecimal baseInterest = loan.getTotalAmount().multiply(dailyInterestRate);
        if (loan.getPaymentType().getCode().equals(PaymentTypeEnum.WEEKLY.getCode()))
            return baseInterest.multiply(BigDecimal.valueOf(PaymentTypeEnum.WEEKLY.getDivisor()));
        else if (loan.getPaymentType().getCode().equals(PaymentTypeEnum.MONTHLY.getCode()))
            return baseInterest.multiply(BigDecimal.valueOf(PaymentTypeEnum.MONTHLY.getDivisor()));

        return BigDecimal.ZERO;
    }

    private BigDecimal calculateDailyInterestRate(Loan loan) {
        BigDecimal interestRate = loan.getInterestRate();
        if (loan.getInterestTimeSpan().getCode().equals(InterestTimeSpanEnum.YEARLY.getCode()))
            return interestRate.divide(BigDecimal.valueOf(InterestTimeSpanEnum.YEARLY.getDivisor()), 10, RoundingMode.UP);
        else if (loan.getInterestTimeSpan().getCode().equals(InterestTimeSpanEnum.MONTHLY.getCode()))
            return interestRate.divide(BigDecimal.valueOf(InterestTimeSpanEnum.MONTHLY.getDivisor()), 10, RoundingMode.UP);
        else if (loan.getInterestTimeSpan().getCode().equals(InterestTimeSpanEnum.WEEKLY.getCode()))
            return interestRate.divide(BigDecimal.valueOf(InterestTimeSpanEnum.WEEKLY.getDivisor()), 10, RoundingMode.UP);
        return BigDecimal.ZERO;
    }

    private LocalDate deltaDate(LocalDate baseDate, Loan loan) {
        if (loan.getPaymentType().getCode().equals(PaymentTypeEnum.MONTHLY.getCode()))
            return baseDate.plusMonths(1);
        if (loan.getPaymentType().getCode().equals(PaymentTypeEnum.WEEKLY.getCode()))
            return baseDate.plusWeeks(1);
        return baseDate;
    }
}
