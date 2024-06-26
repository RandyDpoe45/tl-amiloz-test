package com.github.RandyDpoe45.amilozdemo.persistence.jparepositories.loan;

import com.github.RandyDpoe45.amilozdemo.persistence.model.loan.LoanOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanOfferJpaRepository extends JpaRepository<LoanOffer, Long> {

    List<LoanOffer> findByAmilozUserId(Long userId);
}
