package com.extrade.customer.exception;

public class UserAccountAlreadyActivatedException extends UserAccountServiceException {
    public UserAccountAlreadyActivatedException(String errorCode, String errorMessage) {
        super(errorCode, errorMessage);
    }
}
