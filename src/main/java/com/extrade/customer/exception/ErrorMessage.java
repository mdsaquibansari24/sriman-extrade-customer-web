package com.extrade.customer.exception;

import lombok.Data;

import java.util.Date;

@Data
public class ErrorMessage {
    private String messageID;
    private String errorCode;
    private Date messageDateTime;
    private String errorMessage;
    private String originator;
}
