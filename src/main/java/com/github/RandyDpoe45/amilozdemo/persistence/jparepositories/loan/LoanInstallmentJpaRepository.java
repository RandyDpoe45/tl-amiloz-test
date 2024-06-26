package com.github.RandyDpoe45.amilozdemo.persistence.jparepositories.loan;

import com.github.RandyDpoe45.amilozdemo.persistence.model.loan.LoanInstallment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanInstallmentJpaRepository extends JpaRepository<LoanInstallment, Long> {

    List<LoanInstallment> findByLoanIdOrderByPaymentDateAsc(Long loanId);
}
