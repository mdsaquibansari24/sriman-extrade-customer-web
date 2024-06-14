package com.extrade.customer.feign.config;


import com.extrade.customer.exception.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

@Configuration
@Slf4j
public class UserAccountServiceFeignConfiguration {
    @Bean
    public ErrorDecoder errorDecoder() {
        return new AccountServiceExceptionDecoder();
    }

    public final class AccountServiceExceptionDecoder implements ErrorDecoder {


        @Override
        public Exception decode(String s, Response response) {
            UserAccountServiceException userAccountServiceException = null;
            ObjectMapper objectMapper = new ObjectMapper();
            ErrorMessage errorMessage = null;
            Reader reader = null;

            try {
                reader = response.body().asReader(StandardCharsets.UTF_8);
                errorMessage = objectMapper.readValue(reader, ErrorMessage.class);
            } catch (IOException e) {
                log.error("unknown error while parsing the error response", e);
                throw new UserAccountServiceException(e, "unknown", "error while decoding the error response");
            }

            if (response.status() == HttpStatus.NOT_FOUND.value()) {
                userAccountServiceException = new UserAccountNotFoundException(errorMessage.getErrorCode(), errorMessage.getErrorMessage());
            } else if (response.status() == HttpStatus.GONE.value()) {
                userAccountServiceException = new UserAccountAlreadyActivatedException(errorMessage.getErrorCode(), errorMessage.getErrorMessage());
            } else if (response.status() == HttpStatus.BAD_REQUEST.value()) {
                userAccountServiceException = new OTPMismatchException(errorMessage.getErrorCode(), errorMessage.getErrorMessage());
            } else if (response.status() == HttpStatus.UNPROCESSABLE_ENTITY.value()) {
                userAccountServiceException = new OTPAlreadyVerifiedException(errorMessage.getErrorCode(), errorMessage.getErrorMessage());
            } else {
                userAccountServiceException = new UserAccountServiceException(errorMessage.getErrorCode(), errorMessage.getErrorMessage());
            }

            return userAccountServiceException;
        }
    }
}
