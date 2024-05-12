package com.extrade.customer.controller;

import com.extrade.customer.dto.CustomerRegistrationDto;
import com.extrade.customer.form.CustomerSignupForm;
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

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(signupFormValidator);
    }

    @GetMapping("/signup")
    public String showCustomerSignupForm(Model model) {
        CustomerSignupForm form = new CustomerSignupForm();

        model.addAttribute("customerSignupForm", form);
        return "customer-signup";
    }

    @PostMapping("/signup")
    public String doSignup(@ModelAttribute("customerSignupForm") @Valid CustomerSignupForm signupForm,
                           BindingResult errors, Model model) {
        CustomerRegistrationDto registrationDto = null;
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
        long customerId = Long.parseLong(userAccountService.registerCustomer(registrationDto));

        model.addAttribute("customerName", signupForm.getFirstName()+ " "+ signupForm.getLastName());
        model.addAttribute("customerId", customerId);
        model.addAttribute("emailAddress", StringUtils.maskEmailAddress(signupForm.getEmailAddress(), 4));
        model.addAttribute("mobileNo", StringUtils.maskMobileNo(signupForm.getMobileNo(), 4));


        return "customer-signup-mobile-verification";
    }
}
