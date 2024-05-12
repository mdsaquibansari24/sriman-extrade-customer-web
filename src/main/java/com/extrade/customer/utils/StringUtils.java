package com.extrade.customer.utils;

import java.security.SecureRandom;

public class StringUtils {
    public static String maskMobileNo(String mobileNo, int len) {
        String maskedMobileNo = "";

        for (int i = 0; i < (mobileNo.length() - len); i++) {
            maskedMobileNo += "x";
        }
        maskedMobileNo += mobileNo.substring(mobileNo.length() - len, mobileNo.length());
        return maskedMobileNo;
    }

    public static String maskEmailAddress(String emailAddress, int len) {
        String address = null;
        String domain = null;
        char[] maskedEmailAddress = null;
        int maskedLen = 0;

        address = emailAddress.substring(0, emailAddress.indexOf("@"));
        domain = emailAddress.substring(emailAddress.indexOf("@") + 1, emailAddress.length());

        maskedEmailAddress = new char[address.length()];
        for (int i = 0; i < address.length(); i++) {
            maskedEmailAddress[i] = 'x';
        }

        SecureRandom random = new SecureRandom();
        for (int i = 0; i < len; i++) {
            int r = random.nextInt(address.length());
            maskedEmailAddress[r] = address.charAt(r);
        }

        return new String(maskedEmailAddress) + "@" + domain;
    }


}
