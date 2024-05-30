package com.extrade.customer.exception;

public class OTPMismatchException extends UserAccountServiceException {
    public OTPMismatchException(String errorCode, String errorMessage) {
        super(errorCode, errorMessage);
    }
}
