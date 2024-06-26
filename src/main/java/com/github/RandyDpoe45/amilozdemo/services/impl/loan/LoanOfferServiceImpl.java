package com.github.RandyDpoe45.amilozdemo.services.impl.loan;

import com.github.RandyDpoe45.amilozdemo.exceptions.exceptiontypes.AmilozServiceException;
import com.github.RandyDpoe45.amilozdemo.exceptions.exceptiontypes.ExceptionCodesEnum;
import com.github.RandyDpoe45.amilozdemo.persistence.enums.UserRoleEnum;
import com.github.RandyDpoe45.amilozdemo.persistence.jparepositories.loan.InterestTimeSpanJpaRepository;
import com.github.RandyDpoe45.amilozdemo.persistence.jparepositories.loan.InterestTypeJpaRepository;
import com.github.RandyDpoe45.amilozdemo.persistence.jparepositories.loan.LoanOfferJpaRepository;
import com.github.RandyDpoe45.amilozdemo.persistence.jparepositories.loan.PaymentTypeJpaRepository;
import com.github.RandyDpoe45.amilozdemo.persistence.jparepositories.user.AmilozUserJpaRepository;
import com.github.RandyDpoe45.amilozdemo.persistence.model.loan.InterestTimeSpan;
import com.github.RandyDpoe45.amilozdemo.persistence.model.loan.InterestType;
import com.github.RandyDpoe45.amilozdemo.persistence.model.loan.LoanOffer;
import com.github.RandyDpoe45.amilozdemo.persistence.model.loan.PaymentType;
import com.github.RandyDpoe45.amilozdemo.persistence.model.user.AmilozUser;
import com.github.RandyDpoe45.amilozdemo.restcontrollers.loan.dtos.CreateLoanOfferDto;
import com.github.RandyDpoe45.amilozdemo.services.interfaces.loan.LoanOfferService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class LoanOfferServiceImpl implements LoanOfferService {

    private final LoanOfferJpaRepository loanOfferJpaRepository;

    private final PaymentTypeJpaRepository paymentTypeJpaRepository;

    private final EntityManager em;
    private final AmilozUserJpaRepository amilozUserJpaRepository;
    private final InterestTypeJpaRepository interestTypeJpaRepository;
    private final InterestTimeSpanJpaRepository interestTimeSpanJpaRepository;

    @Override
    @Transactional
    public LoanOffer create(
            Long amilozUserId,
            CreateLoanOfferDto createLoanOfferDto
    ) {
        PaymentType paymentType = paymentTypeJpaRepository.findByCode(createLoanOfferDto.paymentTypeCode());
        if (Objects.isNull(paymentType))
            throw new AmilozServiceException(ExceptionCodesEnum.PAYMENT_TYPE_NOT_FOUND);
        InterestType interestType = interestTypeJpaRepository.findByCode(createLoanOfferDto.interestTypeCode());
        if (Objects.isNull(interestType))
            throw new AmilozServiceException(ExceptionCodesEnum.INTEREST_TYPE_NOT_FOUND);
        InterestTimeSpan interestTimeSpan = interestTimeSpanJpaRepository.findByCode(createLoanOfferDto.interestTimeSpanCode());
        if (Objects.isNull(interestTimeSpan))
            throw new AmilozServiceException(ExceptionCodesEnum.INTEREST_TIME_SPAN_NOT_FOUND);

        AmilozUser amilozUser = amilozUserJpaRepository.findById(amilozUserId)
                .orElseThrow(() -> new AmilozServiceException(ExceptionCodesEnum.AMILOZ_USER_NOT_FOUND));

        AmilozUser adminUser = amilozUserJpaRepository.findById(createLoanOfferDto.offerCreatorId())
                .orElseThrow(() -> new AmilozServiceException(ExceptionCodesEnum.AMILOZ_USER_NOT_FOUND));

        if (!adminUser.getAuthenticationUser().getUserRoles().stream().anyMatch(x -> x.getName().equals(UserRoleEnum.ADMIN_ROlE.getCode())))
            throw new AmilozServiceException(ExceptionCodesEnum.OFFER_CREATOR_NOT_ADMIN);

        LoanOffer loanOffer = new LoanOffer()
                .setOfferCreator(adminUser)
                .setInterestRate(createLoanOfferDto.interestRate())
                .setPaymentType(paymentType)
                .setInterestType(interestType)
                .setTotalAmount(createLoanOfferDto.totalAmount())
                .setTotalInstallments(createLoanOfferDto.totalInstallments())
                .setInterestTimeSpan(interestTimeSpan)
                .setAmilozUser(amilozUser)
                .setAccepted(false);

        loanOffer = loanOfferJpaRepository.saveAndFlush(loanOffer);
        em.refresh(loanOffer);
        return loanOffer;
    }

    @Override
    public List<LoanOffer> getByUserID(Long amilozUserId) {
        return loanOfferJpaRepository.findByAmilozUserId(amilozUserId);
    }
}
