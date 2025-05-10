package com.project.ticketmicroservice.feignClient;

import com.project.ticketmicroservice.DTO.UserResponse;
import com.project.ticketmicroservice.config.CustomUserFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "user-microservice",
        url = "${api.urls.userUrl}",
        configuration = CustomUserFeignConfig.class
)
public interface UserClient {

    @GetMapping("/getUserById/{id}")
    ResponseEntity<UserResponse> getUserById(@RequestHeader("Authorization") String jwToken, @PathVariable("id") Long id);
}
