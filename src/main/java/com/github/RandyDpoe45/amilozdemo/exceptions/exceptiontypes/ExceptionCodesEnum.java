package com.github.RandyDpoe45.amilozdemo.exceptions.exceptiontypes;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionCodesEnum {
    UNPARSEABLE_JSON("001", "Unparseable JSON"),
    UNEXPECTED_ERROR("002", "Unexpected error"),
    INVALID_JWT_TOKEN("003", "Invalid JWT token"),
    ELEMENT_NOT_FOUND("004", "Element not found"),
    INVALID_CREDENTIALS("005", "Invalid username and password"),
    INVALID_REFRESH_TOKEN("006", "Invalid refresh token"),
    USERNAME_IN_USE("007", "Username is already in use"),
    EMAIl_IN_USE("007", "Email is already in use"),
    PAYMENT_TYPE_NOT_FOUND("008", "Payment type not found"),
    AMILOZ_USER_NOT_FOUND("009", "Amiloz user not found"),
    LOAN_OFFER_NOT_FOUND("010", "Loan offer not found"),
    INTEREST_TYPE_NOT_FOUND("011", "Payment type not found"),
    INTEREST_TIME_SPAN_NOT_FOUND("012", "Interest time span not found"),
    OFFER_CREATOR_NOT_ADMIN("013", "Offer creator is not admin"),
    INSTALLMENT_NOT_FOUND("014", "Installment not found"),
    PAYMENT_NOT_FOUND("015", "Payment not found"),
    LOAN_OFFER_ALREADY_ACCEPTED("016", "Loan offer already accepted"),
    INSTALLMENT_AlREADY_PAID("017", "Installment already payed"),
    PAID_AMOUNT_GREATER_THAN_REMAINING("018", "Paid amount greater than pending amount"),;

    private final String code;
    private final String defaultMessage;
}
