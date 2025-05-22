package com.project.usermicroservice.controller;

import com.project.usermicroservice.DTO.AuthRequest;
import com.project.usermicroservice.DTO.RegisterRequest;
import com.project.usermicroservice.DTO.Response;
import com.project.usermicroservice.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    @Value("${expire.refresh-token}")
    private int refreshTokenExpire;

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
        HttpHeaders headers = new HttpHeaders();
        headers.add("Set-Cookie", "Refresh-cookie=" + response.getRefreshToken() + ";Max-Age=" + refreshTokenExpire / 1000 + ";Secure;HttpOnly");
        response.setRefreshToken(null);
        return ResponseEntity.status(response.getHttpCode()).headers(headers).body(response);
    }

    @GetMapping("/refresh")
    public ResponseEntity<Response> refresh(@CookieValue(name = "Refresh-cookie") String refreshToken) {
        Response response = authenticationService.refresh(refreshToken);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Set-Cookie", "Refresh-cookie=" + response.getRefreshToken() + ";Max-Age=" + refreshTokenExpire / 1000 + ";Secure;HttpOnly");
        response.setRefreshToken(null);
        return ResponseEntity.status(response.getHttpCode()).headers(headers).body(response);
    }

    @GetMapping("/verifyUser/{email}")
    public ResponseEntity<Boolean> verifyUser(@PathVariable("email") String email) {
        Boolean exist = authenticationService.verifyUser(email);
        return ResponseEntity.ok().body(exist);
    }

    @GetMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        authenticationService.logout(request, response);
    }
}
