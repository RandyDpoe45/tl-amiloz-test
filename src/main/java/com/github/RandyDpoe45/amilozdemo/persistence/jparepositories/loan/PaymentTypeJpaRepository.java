package com.github.RandyDpoe45.amilozdemo.persistence.jparepositories.loan;

import com.github.RandyDpoe45.amilozdemo.persistence.model.loan.PaymentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentTypeJpaRepository extends JpaRepository<PaymentType, Long> {
    PaymentType findByCode(String code);
}
