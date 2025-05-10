package com.project.ticketmicroservice.config;

import com.project.ticketmicroservice.handler.FeignPaymentErrDecoder;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomPaymentFeignConfig {

    @Bean
    public ErrorDecoder paymentErrorDecoder() {
        return new FeignPaymentErrDecoder();
    }
}
