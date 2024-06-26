package com.github.RandyDpoe45.amilozdemo.persistence.model.loan;

import com.fasterxml.jackson.annotation.JsonView;
import com.github.RandyDpoe45.amilozdemo.restcontrollers.loan.dtos.LoanResultViews;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class InterestType {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView({LoanResultViews.LoanOfferView.class, LoanResultViews.LoanMasterDetail.class})
    private Long id;

    @JsonView({LoanResultViews.LoanOfferView.class, LoanResultViews.LoanMasterDetail.class})
    private String code;
}

