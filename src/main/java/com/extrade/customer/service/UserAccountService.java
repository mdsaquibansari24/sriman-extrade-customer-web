package com.extrade.customer.service;

import com.extrade.customer.dto.AccountVerificationStatusDto;
import com.extrade.customer.dto.CustomerRegistrationDto;
import com.extrade.customer.dto.UserAccountDto;
import com.extrade.customer.feign.config.UserAccountServiceFeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
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
}
