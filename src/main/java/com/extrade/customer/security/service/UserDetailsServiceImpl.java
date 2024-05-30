package com.extrade.customer.security.service;

import com.extrade.customer.dto.UserAccountDto;
import com.extrade.customer.exception.UserAccountNotFoundException;
import com.extrade.customer.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserAccountService userAccountService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccountDto userAccountDto = null;
        UserDetails userDetails = null;
        try {
            userAccountDto = userAccountService.getUserAccountByEmailAddress(username);
            userDetails = User.withUsername(userAccountDto.getEmailAddress())
                    .password(userAccountDto.getPassword())
                    .roles(userAccountDto.getRoleCode())
                    .accountLocked(userAccountDto.getStatus().equals("L"))
                    .disabled(userAccountDto.getStatus().equals("D"))
                    .accountExpired(false)
                    .credentialsExpired(false)
                    .passwordEncoder((password) -> {
                        return passwordEncoder.encode(password);
                    })
                    .authorities(userAccountDto.getRoleCode())
                    .build();
        } catch (UserAccountNotFoundException e) {
            log.error("user not found while authenticating", e);
            throw new UsernameNotFoundException("user with :" + username + " not found");
        }
        return userDetails;
    }
}
