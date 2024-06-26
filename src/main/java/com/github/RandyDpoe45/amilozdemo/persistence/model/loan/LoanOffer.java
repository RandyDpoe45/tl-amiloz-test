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

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class LoanOffer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView({LoanResultViews.LoanOfferView.class, LoanResultViews.LoanMasterDetail.class})
    private Long id;

    @JsonView(LoanResultViews.LoanOfferView.class)
    private BigDecimal totalAmount;

    @JsonView(LoanResultViews.LoanOfferView.class)
    private Long totalInstallments;

    @JsonView(LoanResultViews.LoanOfferView.class)
    private BigDecimal interestRate;

    @JsonView(LoanResultViews.LoanOfferView.class)
    private Boolean accepted;

    @JsonView(LoanResultViews.LoanOfferView.class)
    @ManyToOne(targetEntity = PaymentType.class, fetch = FetchType.EAGER)
    private PaymentType paymentType;

    @JsonView(LoanResultViews.LoanOfferView.class)
    @ManyToOne(targetEntity = InterestType.class, fetch = FetchType.EAGER)
    private InterestType interestType;

    @JsonView(LoanResultViews.LoanOfferView.class)
    @ManyToOne(targetEntity = InterestTimeSpan.class, fetch = FetchType.EAGER)
    private InterestTimeSpan interestTimeSpan;

    @ManyToOne(targetEntity = AmilozUser.class, fetch = FetchType.EAGER)
    private AmilozUser amilozUser;

    @ManyToOne(targetEntity = AmilozUser.class, fetch = FetchType.EAGER)
    private AmilozUser offerCreator;
}
