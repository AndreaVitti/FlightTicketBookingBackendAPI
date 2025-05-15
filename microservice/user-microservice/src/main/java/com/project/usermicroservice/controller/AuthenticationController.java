package com.project.usermicroservice.controller;

import com.project.usermicroservice.DTO.AuthRequest;
import com.project.usermicroservice.DTO.RegisterRequest;
import com.project.usermicroservice.DTO.Response;
import com.project.usermicroservice.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    /*Register a user*/
    @PostMapping("/register")
    public ResponseEntity<Response> register(@RequestBody @Valid RegisterRequest regRequest) {
        Response response = authenticationService.register(regRequest);
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }

    /*Login with a user*/
    @PostMapping("/login")
    public ResponseEntity<Response> login(@RequestBody @Valid AuthRequest authRequest) {
        Response response = authenticationService.login(authRequest);
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }

    @GetMapping("/verifyUser/{email}")
    public ResponseEntity<Boolean> verifyUser(@PathVariable("email") String email) {
        Boolean exist = authenticationService.verifyUser(email);
        return ResponseEntity.ok().body(exist);
    }
}
