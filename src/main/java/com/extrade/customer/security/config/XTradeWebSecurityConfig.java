package com.extrade.customer.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.ExceptionMappingAuthenticationFailureHandler;

import java.util.Properties;

@Configuration
@EnableWebSecurity
public class XTradeWebSecurityConfig {
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests((requests) -> {
                    requests
                            .requestMatchers("/home", "/static/**", "/customer/signup")
                            .permitAll().anyRequest().authenticated();
                }).formLogin((formLoginConfigurer) -> {
                    formLoginConfigurer.loginPage("/customer/login")
                            .permitAll()
                            .usernameParameter("j_username")
                            .passwordParameter("j_password")
                            .loginProcessingUrl("/customer/j_login")
                            .defaultSuccessUrl("/home")
                            .failureHandler(authenticationFailureHandler());
                }).logout(logout -> logout
                        .logoutSuccessUrl("/customer/logout")
                        .invalidateHttpSession(true).permitAll())
                .build();
    }


    public AuthenticationFailureHandler authenticationFailureHandler() {
        ExceptionMappingAuthenticationFailureHandler failureHandler = null;
        Properties exceptionMappings = null;

        failureHandler = new ExceptionMappingAuthenticationFailureHandler();
        exceptionMappings = new Properties();

        exceptionMappings.put("org.springframework.security.authentication.BadCredentialsException", "/login?error=bad");
        exceptionMappings.put("org.springframework.security.authentication.DisabledException", "/login?error=disabled");

        failureHandler.setExceptionMappings(exceptionMappings);
        return failureHandler;
    }
}
