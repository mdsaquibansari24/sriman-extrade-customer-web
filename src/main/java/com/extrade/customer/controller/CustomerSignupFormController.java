package com.extrade.customer.controller;

import com.extrade.customer.dto.AccountVerificationStatusDto;
import com.extrade.customer.dto.CustomerRegistrationDto;
import com.extrade.customer.form.CustomerSignupForm;
import com.extrade.customer.form.MobileOTPVerificationForm;
import com.extrade.customer.service.UserAccountService;
import com.extrade.customer.utils.StringUtils;
import com.extrade.customer.validator.CustomerSignupFormValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerSignupFormController {
    private final CustomerSignupFormValidator signupFormValidator;
    private final UserAccountService userAccountService;


    @GetMapping("/signup")
    public String showCustomerSignupForm(Model model) {
        CustomerSignupForm form = new CustomerSignupForm();

        model.addAttribute("customerSignupForm", form);
        return "customer-signup";
    }

    @PostMapping("/signup")
    public String doSignup(@ModelAttribute("customerSignupForm") CustomerSignupForm signupForm,
                           BindingResult errors, Model model) {
        CustomerRegistrationDto registrationDto = null;

        if (signupFormValidator.supports(signupForm.getClass())) {
            signupFormValidator.validate(signupForm, errors);
        }
        if (errors.hasErrors()) {
            return "customer-signup";
        }
        registrationDto = new CustomerRegistrationDto();

        registrationDto.setFirstName(signupForm.getFirstName());
        registrationDto.setLastName(signupForm.getLastName());
        registrationDto.setDob(signupForm.getDob());
        registrationDto.setGender(signupForm.getGender());
        registrationDto.setPassword(signupForm.getPassword());
        registrationDto.setEmailAddress(signupForm.getEmailAddress());
        registrationDto.setMobileNo(signupForm.getMobileNo());
        int customerId = Integer.parseInt(userAccountService.registerCustomer(registrationDto));

        MobileOTPVerificationForm verificationForm = new MobileOTPVerificationForm();
        verificationForm.setUserAccountId(customerId);
        verificationForm.setVerificationEmailAddress(StringUtils.maskEmailAddress(signupForm.getEmailAddress(), 4));
        verificationForm.setVerificationMobileNo(StringUtils.maskMobileNo(signupForm.getMobileNo(), 4));
        verificationForm.setCustomerName(signupForm.getFirstName() + " " + signupForm.getLastName());

        model.addAttribute("mobileOTPVerificationForm", verificationForm);

        return "customer-signup-mobile-verification";
    }

    @PostMapping("/signup/verifyMobileOTP")
    public String verifyMobileOTP(@ModelAttribute("mobileOTPVerificationForm") MobileOTPVerificationForm form, BindingResult errors, Model model) {
        AccountVerificationStatusDto accountVerificationStatusDto = null;

        accountVerificationStatusDto = userAccountService.verifyOTP(form.getUserAccountId(), form.getOtpCode(), "VERIFY_MOBILE");

        
        model.addAttribute("accountVerificationStatus", accountVerificationStatusDto);
        return "signup-status";
    }
}






















