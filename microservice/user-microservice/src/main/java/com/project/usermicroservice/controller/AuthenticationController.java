package com.project.usermicroservice.controller;

import com.project.usermicroservice.DTO.AuthRequest;
import com.project.usermicroservice.DTO.RegisterRequest;
import com.project.usermicroservice.DTO.Response;
import com.project.usermicroservice.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    /*Register a user*/
    @PostMapping("/register")
    public ResponseEntity<Response> register(@Valid @RequestBody RegisterRequest regRequest) {
        Response response = authenticationService.register(regRequest);
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }

    /*Login with a user*/
    @PostMapping("/login")
    public ResponseEntity<Response> login(@Valid @RequestBody AuthRequest authRequest) {
        Response response = authenticationService.login(authRequest);
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }
}
