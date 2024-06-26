package com.github.RandyDpoe45.amilozdemo.persistence.model.loan;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import com.github.RandyDpoe45.amilozdemo.restcontrollers.loan.dtos.LoanResultViews;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView({LoanResultViews.LoanInstallmentView.class, LoanResultViews.LoanPaymentView.class})
    private Long id;

    @ManyToOne(targetEntity = LoanInstallment.class)
    @JsonView({LoanResultViews.LoanPaymentView.class})
    private LoanInstallment installment;

    @JsonView({LoanResultViews.LoanInstallmentView.class, LoanResultViews.LoanPaymentView.class})
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate paymentDate;

    @JsonView({LoanResultViews.LoanInstallmentView.class, LoanResultViews.LoanPaymentView.class})
    private BigDecimal amount;

    @JsonView({LoanResultViews.LoanInstallmentView.class, LoanResultViews.LoanPaymentView.class})
    private Boolean reversed;
}
