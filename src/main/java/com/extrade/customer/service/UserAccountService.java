package com.extrade.customer.service;

import com.extrade.customer.dto.CustomerRegistrationDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "userAccountService", url = "${userAccountService.url}/account")
public interface UserAccountService {
    @GetMapping("/count/email")
    Long countUserAccountsByEmailAddress(@RequestParam("emailAddress") String emailAddress);

    @GetMapping("/count/mobileNo")
    Long countUserAccountsByMobileNo(@RequestParam("mobileNo") String mobileNo);

    @PostMapping(value = "/customer", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.TEXT_PLAIN_VALUE})
    String registerCustomer(CustomerRegistrationDto customerRegistrationDto);
}
