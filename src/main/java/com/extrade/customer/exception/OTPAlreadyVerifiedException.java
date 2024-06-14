package com.extrade.customer.exception;

public class OTPAlreadyVerifiedException extends UserAccountServiceException {
    public OTPAlreadyVerifiedException(String errorCode, String errorMessage) {
        super(errorCode, errorMessage);
    }
}
