package com.github.RandyDpoe45.amilozdemo.services.interfaces.loan;

import com.github.RandyDpoe45.amilozdemo.persistence.model.loan.Loan;
import com.github.RandyDpoe45.amilozdemo.persistence.model.loan.LoanInstallment;

import java.util.List;

public interface LoanService {

    Loan createLoanFromOffer(Long loanOfferId);

    List<Loan> getLoansByUserId(Long userId);

    List<LoanInstallment> getLoanInstallments(Long loanId);

    void updateLoanPaidState(Long loanId);
}
