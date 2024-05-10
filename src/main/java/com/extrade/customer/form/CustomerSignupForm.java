package com.extrade.customer.form;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

@Data
public class CustomerSignupForm {
    @Email
    @NotBlank
    private String emailAddress;
    @Pattern(regexp = "\\d{10}|(?:\\d{3}-){2}\\d{4}|\\(\\d{3}\\)\\d{3}-?\\d{4}")
    private String mobileNo;
    @Pattern(regexp = "/^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*\\W)(?!.* ).{8,16}$/")
    private String password;
    @Pattern(regexp = "/^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*\\W)(?!.* ).{8,16}$/")
    private String rePassword;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @Past
    private LocalDate dob;
    @NotBlank
    private String gender;
    @NotBlank
    private String termsAndConditions;
}
