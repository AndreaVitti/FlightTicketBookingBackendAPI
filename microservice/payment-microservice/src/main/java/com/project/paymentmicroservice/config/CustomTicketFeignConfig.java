package com.project.paymentmicroservice.config;

import com.project.paymentmicroservice.handler.FeignTicketErrDecoder;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomTicketFeignConfig {

    @Bean
    public ErrorDecoder ticketErrorDecoder() {
        return new FeignTicketErrDecoder();
    }
}
