package com.project.ticketmicroservice.config;

import com.project.ticketmicroservice.handler.FeignUserErrDecoder;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomUserFeignConfig {

    @Bean
    public ErrorDecoder userErrorDecoder() {
        return new FeignUserErrDecoder();
    }
}
