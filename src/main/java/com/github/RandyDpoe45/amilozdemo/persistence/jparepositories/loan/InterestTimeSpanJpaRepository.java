package com.github.RandyDpoe45.amilozdemo.persistence.jparepositories.loan;

import com.github.RandyDpoe45.amilozdemo.persistence.model.loan.InterestTimeSpan;
import com.github.RandyDpoe45.amilozdemo.persistence.model.loan.InterestType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterestTimeSpanJpaRepository extends JpaRepository<InterestTimeSpan, Long> {
    InterestTimeSpan findByCode(String code);
}
