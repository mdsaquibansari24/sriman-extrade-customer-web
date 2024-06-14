package com.extrade.customer.exception;

public class UserAccountNotFoundException extends UserAccountServiceException{
    public UserAccountNotFoundException(String errorCode, String errorMessage) {
        super(errorCode, errorMessage);
    }
}
