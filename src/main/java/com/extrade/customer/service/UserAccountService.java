package com.extrade.customer.service;

import com.extrade.customer.dto.AccountVerificationStatusDto;
import com.extrade.customer.dto.CustomerRegistrationDto;
import com.extrade.customer.dto.UserAccountDto;
import com.extrade.customer.feign.config.UserAccountServiceFeignConfiguration;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "userAccountService", url = "${userAccountService.url}/account", configuration = UserAccountServiceFeignConfiguration.class)
public interface UserAccountService {
    @GetMapping("/count/email")
    Long countUserAccountsByEmailAddress(@RequestParam("emailAddress") String emailAddress);

    @GetMapping("/count/mobileNo")
    Long countUserAccountsByMobileNo(@RequestParam("mobileNo") String mobileNo);

    @PostMapping(value = "/customer", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.TEXT_PLAIN_VALUE})
    String registerCustomer(CustomerRegistrationDto customerRegistrationDto);

    @PutMapping(value = "/{userAccountId}/{otpCode}/{verificationType}", produces = {MediaType.APPLICATION_JSON_VALUE})
    AccountVerificationStatusDto verifyOTP(@PathVariable("userAccountId") int userAccountId,
                                           @PathVariable("otpCode") String otpCode,
                                           @PathVariable("verificationType") String verificationType);

    @GetMapping(value = "/{userAccountId}/verificationStatus", consumes = {MediaType.APPLICATION_JSON_VALUE})
    AccountVerificationStatusDto getUserAccountVerificationStatus(@PathVariable("userAccountId") int userAccountId);

    @GetMapping(value = "/details", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public UserAccountDto getUserAccountByEmailAddress(@RequestParam("emailAddress") String emailAddress);

    @PutMapping(value = "/{userAccountId}/resendMobileOTP")
    ResponseEntity<Void> resendMobileOTPCode(@PathVariable("userAccountId") int userAccountId);

    @PutMapping(value = "/{userAccountId}/resendEmailVerificationLink")
    ResponseEntity<Void> resendEmailVerificationLink(@PathVariable("userAccountId") int userAccountId);

    @GetMapping(value = "/{emailAddress}/verificationStatusByEmail", consumes = {MediaType.APPLICATION_JSON_VALUE})
    AccountVerificationStatusDto getUserAccountVerificationStatusByEmail(@RequestParam("emailAddress") String emailAddress);
}
