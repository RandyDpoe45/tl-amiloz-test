package com.github.RandyDpoe45.amilozdemo.services.interfaces.loan;

import com.github.RandyDpoe45.amilozdemo.persistence.model.loan.LoanOffer;
import com.github.RandyDpoe45.amilozdemo.restcontrollers.loan.dtos.CreateLoanOfferDto;

import java.util.List;

public interface LoanOfferService {
    LoanOffer create(Long amilozUserId, CreateLoanOfferDto createLoanOfferDto);
    List<LoanOffer> getByUserID(Long amilozUserId);
}
