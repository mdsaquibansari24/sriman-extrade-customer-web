package com.extrade.customer.form;

import lombok.Data;

@Data
public class MobileOTPVerificationForm {
    private int userAccountId;
    private String customerName;
    private String verificationMobileNo;
    private String verificationEmailAddress;
    private String otpCode;

}

