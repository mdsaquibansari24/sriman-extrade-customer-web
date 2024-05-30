package com.extrade.customer.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserAccountDto {
    private int userAccountId;
    private String firstName;
    private String lastName;
    private String mobileNo;
    private String emailAddress;
    private String password;
    @JsonFormat(pattern = "yyyy/MM/dd")
    private LocalDate dob;
    private String gender;
    private String roleCode;
    private String status;
}
