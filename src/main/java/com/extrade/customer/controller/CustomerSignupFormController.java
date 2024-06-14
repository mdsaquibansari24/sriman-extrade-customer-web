package com.extrade.customer.controller;

import com.extrade.customer.dto.AccountVerificationStatusDto;
import com.extrade.customer.dto.CustomerRegistrationDto;
import com.extrade.customer.exception.OTPAlreadyVerifiedException;
import com.extrade.customer.exception.OTPMismatchException;
import com.extrade.customer.exception.UserAccountAlreadyActivatedException;
import com.extrade.customer.exception.UserAccountNotFoundException;
import com.extrade.customer.form.CustomerSignupForm;
import com.extrade.customer.form.MobileOTPVerificationForm;
import com.extrade.customer.service.UserAccountService;
import com.extrade.customer.utils.StringUtils;
import com.extrade.customer.utils.XTradeConstants;
import com.extrade.customer.validator.CustomerSignupFormValidator;
import com.extrade.customer.validator.MobileOTPVerificationFormValidator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@Controller
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerSignupFormController {
    private static final Logger log = LoggerFactory.getLogger(CustomerSignupFormController.class);
    private final CustomerSignupFormValidator signupFormValidator;
    private final MobileOTPVerificationFormValidator mobileOTPVerificationFormValidator;
    private final UserAccountService userAccountService;
    private final MessageSource messageSource;

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
    public String verifyMobileOTP(@ModelAttribute("mobileOTPVerificationForm") MobileOTPVerificationForm form
            , BindingResult errors, Model model,
                                  Locale locale) {
        AccountVerificationStatusDto accountVerificationStatusDto = null;
        String signupStatusMessage = null;

        if (mobileOTPVerificationFormValidator.supports(form.getClass())) {
            mobileOTPVerificationFormValidator.validate(form, errors);
            if (errors.hasErrors()) {
                return "customer-signup-mobile-verification";
            }
        }

        try {
            accountVerificationStatusDto = userAccountService.verifyOTP(form.getUserAccountId(), form.getOtpCode(), "VERIFY_MOBILE");

            if (accountVerificationStatusDto.getAccountStatus().equals(XTradeConstants.USER_ACCOUNT_ACTIVE)) {
                signupStatusMessage = messageSource.getMessage("userAccount.activated", null, locale);
            } else if (accountVerificationStatusDto.getEmailVerificationStatus() == 0) {
                signupStatusMessage = messageSource.getMessage("userAccount.mobileOTPVerified", null, locale);
            }
        } catch (OTPMismatchException e) {
            log.warn("otpCode mis-match", e);
            errors.reject("otpCode.mismatch");
        } catch (OTPAlreadyVerifiedException e) {
            accountVerificationStatusDto = userAccountService.getUserAccountVerificationStatus(form.getUserAccountId());
            if (accountVerificationStatusDto.getAccountStatus().equals(XTradeConstants.USER_ACCOUNT_ACTIVE)) {
                signupStatusMessage = messageSource.getMessage("userAccount.alreadyActivated", null, locale);
            } else if (accountVerificationStatusDto.getEmailVerificationStatus() == 0) {
                signupStatusMessage = messageSource.getMessage("userAccount.mobileOTPVerified", null, locale);
            }
        } catch (UserAccountAlreadyActivatedException e) {
            signupStatusMessage = messageSource.getMessage("userAccount.alreadyActivated", null, locale);
        }

        if (errors.hasErrors()) {
            return "customer-signup-mobile-verification";
        }

        model.addAttribute("signupStatus", signupStatusMessage);
        return "signup-status";
    }

    @GetMapping(value = "/{userAccountId}/{verificationCode}/verifyEmail")
    public String verifyEmailAddress(@PathVariable("userAccountId") int userAccountId,
                                     @PathVariable("verificationCode") String verificationCode,
                                     Locale locale,
                                     Model model) {
        AccountVerificationStatusDto accountVerificationStatusDto = null;
        MobileOTPVerificationForm mobileOTPVerificationForm = null;
        boolean mobileOTPVerificationPending = false;
        String signupStatusMessage = null;
        String outcome = "signup-status";

        try {
            accountVerificationStatusDto = userAccountService.verifyOTP(userAccountId, verificationCode, "VERIFY_EMAIL_ADDRESS");
            if (accountVerificationStatusDto.getAccountStatus().equals(XTradeConstants.USER_ACCOUNT_ACTIVE)) {
                signupStatusMessage = messageSource.getMessage("userAccount.activated", null, locale);
            } else if (accountVerificationStatusDto.getMobileVerificationStatus() == 0) {
                mobileOTPVerificationPending = true;
            }
        } catch (OTPMismatchException e) {
            log.warn("Email Address OTP Code mis-match", e);
            signupStatusMessage = messageSource.getMessage("userAccount.emailVerificationCodeMisMatch", null, locale);
        } catch (OTPAlreadyVerifiedException e) {
            accountVerificationStatusDto = userAccountService.getUserAccountVerificationStatus(userAccountId);
            if (accountVerificationStatusDto.getAccountStatus().equals(XTradeConstants.USER_ACCOUNT_ACTIVE)) {
                signupStatusMessage = messageSource.getMessage("userAccount.alreadyActivated", null, locale);
            } else if (accountVerificationStatusDto.getMobileVerificationStatus() == 0) {
                accountVerificationStatusDto = userAccountService.getUserAccountVerificationStatus(userAccountId);
                mobileOTPVerificationPending = true;
            }
        } catch (UserAccountAlreadyActivatedException e) {
            signupStatusMessage = messageSource.getMessage("userAccount.alreadyActivated", null, locale);
        }

        if (mobileOTPVerificationPending) {
            mobileOTPVerificationForm = new MobileOTPVerificationForm();
            mobileOTPVerificationForm.setUserAccountId(userAccountId);
            mobileOTPVerificationForm.setVerificationEmailAddress(StringUtils.maskEmailAddress(accountVerificationStatusDto.getEmailAddress(), 4));
            mobileOTPVerificationForm.setVerificationMobileNo(StringUtils.maskMobileNo(accountVerificationStatusDto.getMobileNo(), 4));
            signupStatusMessage = messageSource.getMessage("userAccount.emailAddressVerified", null, locale);
            model.addAttribute("mobileOTPVerificationForm", mobileOTPVerificationForm);
            outcome = "customer-signup-mobile-verification";
        }

        model.addAttribute("signupStatus", signupStatusMessage);

        return outcome;
    }

    @GetMapping("/locked-find-and-activate")
    public String lockedShowFindAccountPage() {
        return "locked-find-and-activate-account";
    }

    @ExceptionHandler(UserAccountNotFoundException.class)
    public String handleUserAccountNotFoundException(UserAccountNotFoundException e, Model model, Locale locale) {
        model.addAttribute("errorMessage", messageSource.getMessage("userAccount.notFound", null, locale));
        return "global-error";
    }


    @GetMapping("/{userAccountId}/resendMobileOTP")
    @ResponseBody
    public ResponseEntity<Void> resendMobileOTP(@PathVariable("userAccountId") int userAccountId) {
        try {
            userAccountService.resendMobileOTPCode(userAccountId);
        } catch (UserAccountNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (UserAccountAlreadyActivatedException e) {
            return ResponseEntity.status(HttpStatus.GONE).build();
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userAccountId}/resendVerificationEmail")
    @ResponseBody
    public ResponseEntity<Void> resendEmailVerificationLink(@PathVariable("userAccountId") int userAccountId) {
        try {
            userAccountService.resendEmailVerificationLink(userAccountId);
        } catch (UserAccountNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (UserAccountAlreadyActivatedException e) {
            return ResponseEntity.status(HttpStatus.GONE).build();
        }
        return ResponseEntity.ok().build();
    }
}






















