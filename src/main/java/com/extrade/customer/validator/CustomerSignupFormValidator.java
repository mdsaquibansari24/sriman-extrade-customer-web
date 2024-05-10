package com.extrade.customer.validator;

import com.extrade.customer.form.CustomerSignupForm;
import com.extrade.customer.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class CustomerSignupFormValidator implements Validator {
    private final UserAccountService userAccountService;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(CustomerSignupForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CustomerSignupForm signupForm = null;

        signupForm = (CustomerSignupForm) target;

        if (errors.hasFieldErrors("password") == false && errors.hasFieldErrors("rePassword")) {
            if (signupForm.getPassword().equals(signupForm.getRePassword()) == false) {
                errors.rejectValue("rePassword", "rePassword.mismatch");
            }
        }

        if (errors.hasFieldErrors("emailAddress") == false) {
            long cEmail = userAccountService.countUserAccountsByEmailAddress(signupForm.getEmailAddress());
            if (cEmail > 0) {
                errors.rejectValue("emailAddress", "emailAddress.notAvailable");
            }
        }

        if (errors.hasFieldErrors("mobileNo") == false) {
            long cMobileNo = userAccountService.countUserAccountsByMobileNo(signupForm.getMobileNo());
            if (cMobileNo > 0) {
                errors.rejectValue("mobileNo", "mobileNo.notAvailable");
            }
        }
    }
}
