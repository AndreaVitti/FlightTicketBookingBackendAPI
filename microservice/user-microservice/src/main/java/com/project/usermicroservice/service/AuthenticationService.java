package com.project.usermicroservice.service;

import com.project.usermicroservice.DTO.AuthRequest;
import com.project.usermicroservice.DTO.RegisterRequest;
import com.project.usermicroservice.DTO.Response;
import com.project.usermicroservice.authConfig.JwtService;
import com.project.usermicroservice.entity.User;
import com.project.usermicroservice.exception.UserNotFound;
import com.project.usermicroservice.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    @Value("${expire.refresh-token}")
    private int refreshTokenExpire;


    public Response register(RegisterRequest regRequest) {
        User user = new User();
        user.setFirstname(regRequest.firstname());
        user.setLastname(regRequest.lastname());
        user.setEmail(regRequest.email());

        user.setPassword(passwordEncoder.encode(regRequest.password()));
        user.setPhone(regRequest.phone());
        user.setRoles(regRequest.roles());
        userRepository.save(user);

        Response response = new Response();
        response.setHttpCode(200);
        response.setUserId(user.getId());
        return response;
    }

    public Response login(AuthRequest authRequest) {
        Response response = new Response();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authRequest.email(),
                authRequest.password())
        );
        User user = userRepository.findByEmail(authRequest.email()).orElseThrow(() -> new UserNotFound("User not found"));
        generateAccessAndRefreshTokens(user, response);
        return response;
    }

    public Response refresh(String refreshToken) {
        Response response = new Response();
        Long userId = Long.parseLong(jwtService.extractSubject(refreshToken));
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFound("User not found"));
        generateAccessAndRefreshTokens(user, response);
        return response;
    }

    public Boolean verifyUser(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        return user != null;
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextHolder.clearContext();
        Cookie cookie = new Cookie("Refresh-cookie", "");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        response.setStatus(HttpStatus.OK.value());
    }

    private void generateAccessAndRefreshTokens(User user, Response response) {
        String accessToken = jwtService.generateAccessToken(user, user.getId());
        String refreshToken = jwtService.generateRefreshToken(user.getId());
        response.setHttpCode(200);
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);
        response.setUserId(user.getId());
    }
}
