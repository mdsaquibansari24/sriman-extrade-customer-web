package com.extrade.customer.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "userAccountService", url = "${userAccountService.url}/account")
public interface UserAccountService {
    @GetMapping("/count/email")
    Long countUserAccountsByEmailAddress(@RequestParam("emailAddress") String emailAddress);

    @GetMapping("/count/mobileNo")
    Long countUserAccountsByMobileNo(@RequestParam("mobileNo") String mobileNo);
}
