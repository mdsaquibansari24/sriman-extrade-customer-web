package com.extrade.customer.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CustomerRegistrationDto {
    private String firstName;
    private String lastName;
    @JsonFormat(pattern = "yyyy/MM/dd")
    private LocalDate dob;
    private String gender;
    private String emailAddress;
    private String mobileNo;
    private String password;
}
