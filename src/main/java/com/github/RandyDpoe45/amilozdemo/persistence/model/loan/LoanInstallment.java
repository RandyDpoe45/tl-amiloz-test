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
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class LoanInstallment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView({LoanResultViews.LoanInstallmentView.class, LoanResultViews.LoanPaymentView.class})
    private Long id;

    @JsonView(LoanResultViews.LoanInstallmentView.class)
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate paymentDate;

    @JsonView(LoanResultViews.LoanInstallmentView.class)
    private BigDecimal baseAmount;

    @JsonView(LoanResultViews.LoanInstallmentView.class)
    private BigDecimal interestAmount;

    @JsonView(LoanResultViews.LoanInstallmentView.class)
    private Boolean isPending;

    @ManyToOne(targetEntity = Loan.class, fetch = FetchType.LAZY)
    @JsonView(LoanResultViews.LoanInstallmentView.class)
    private Loan loan;

    @OneToMany(targetEntity = Payment.class, mappedBy = "installment")
    private List<Payment> payments;

    @JsonView(LoanResultViews.LoanInstallmentView.class)
    public BigDecimal getTotalAmount() {
        return this.getBaseAmount().add(this.getInterestAmount()).setScale(2, RoundingMode.UP);
    }

    @JsonView(LoanResultViews.LoanInstallmentView.class)
    public BigDecimal getRemainingAmount() {
        List<Payment> payments = this.getPayments().stream().filter(x -> !x.getReversed()).toList();
        BigDecimal payedAmount = payments.stream().map(Payment::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        return this.getTotalAmount().subtract(payedAmount).setScale(2, RoundingMode.UP);
    }
}
