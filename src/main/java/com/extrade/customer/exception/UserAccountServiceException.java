package com.extrade.customer.exception;

public class UserAccountServiceException extends RuntimeException {
    private String errorCode;
    private String errorMessage;

    public UserAccountServiceException(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public UserAccountServiceException(Throwable cause, String errorCode, String errorMessage) {
        super(cause);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
