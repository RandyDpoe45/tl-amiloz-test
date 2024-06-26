package com.github.RandyDpoe45.amilozdemo.persistence.model.loan;

import com.fasterxml.jackson.annotation.JsonView;
import com.github.RandyDpoe45.amilozdemo.persistence.model.user.AmilozUser;
import com.github.RandyDpoe45.amilozdemo.restcontrollers.loan.dtos.LoanResultViews;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView({LoanResultViews.LoanMasterDetail.class, LoanResultViews.LoanInstallmentView.class})
    private Long id;

    @ManyToOne(targetEntity = AmilozUser.class, fetch = FetchType.EAGER)
    @JsonView(LoanResultViews.LoanMasterDetail.class)
    private AmilozUser loanOwner;

    @JsonView(LoanResultViews.LoanMasterDetail.class)
    private BigDecimal totalAmount;

    @JsonView(LoanResultViews.LoanMasterDetail.class)
    private Long totalInstallments;

    @JsonView(LoanResultViews.LoanMasterDetail.class)
    private BigDecimal interestRate;

    @JsonView(LoanResultViews.LoanMasterDetail.class)
    private Boolean payedInFull;

    @JsonView(LoanResultViews.LoanMasterDetail.class)
    @OneToOne(targetEntity = LoanOffer.class, fetch = FetchType.LAZY)
    private LoanOffer loanOffer;

    @JsonView(LoanResultViews.LoanMasterDetail.class)
    @ManyToOne(targetEntity = PaymentType.class, fetch = FetchType.EAGER)
    private PaymentType paymentType;

    @JsonView(LoanResultViews.LoanMasterDetail.class)
    @ManyToOne(targetEntity = InterestType.class, fetch = FetchType.EAGER)
    private InterestType interestType;

    @JsonView(LoanResultViews.LoanMasterDetail.class)
    @ManyToOne(targetEntity = InterestTimeSpan.class, fetch = FetchType.EAGER)
    private InterestTimeSpan interestTimeSpan;

    @OneToMany(targetEntity = LoanInstallment.class, mappedBy = "loan")
    List<LoanInstallment> loanInstallments;
}

