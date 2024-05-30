package com.extrade.customer.dto;

import lombok.Data;

@Data
public class AccountVerificationStatusDto {
    private int userAccountId;
    private int mobileVerificationStatus;
    private int emailVerificationStatus;
    private String mobileNo;
    private String emailAddress;
    private String accountStatus;
}
