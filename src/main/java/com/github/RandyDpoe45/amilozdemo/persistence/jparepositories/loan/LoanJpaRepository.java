package com.github.RandyDpoe45.amilozdemo.persistence.jparepositories.loan;

import com.github.RandyDpoe45.amilozdemo.persistence.model.loan.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanJpaRepository extends JpaRepository<Loan, Long> {

    List<Loan> findByLoanOwnerId(Long userId);
}
