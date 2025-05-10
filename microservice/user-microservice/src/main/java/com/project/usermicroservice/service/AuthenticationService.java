package com.project.usermicroservice.service;

import com.project.usermicroservice.DTO.AuthRequest;
import com.project.usermicroservice.DTO.RegisterRequest;
import com.project.usermicroservice.DTO.Response;
import com.project.usermicroservice.authConfig.JwtService;
import com.project.usermicroservice.entity.User;
import com.project.usermicroservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public Response register(RegisterRequest regRequest) {
        User user = new User();
        user.setFirstname(regRequest.getFirstname());
        user.setLastname(regRequest.getLastname());
        user.setEmail(regRequest.getEmail());

        /*Encode the password*/
        user.setPassword(passwordEncoder.encode(regRequest.getPassword()));
        user.setPhone(regRequest.getPhone());
        user.setRoles(regRequest.getRoles());
        userRepository.save(user);

        Response response = new Response();
        response.setHttpCode(200);
        response.setUserId(user.getId());
        return response;
    }

    public Response login(AuthRequest authRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authRequest.getEmail(),
                authRequest.getPassword())
        );
        User user = userRepository.findByEmail(authRequest.getEmail()).orElseThrow();

        /*Generate both refresh and access tokens*/
        String jwt = jwtService.generateToken(user, user.getId());

        Response response = new Response();
        response.setHttpCode(200);
        response.setToken(jwt);
        response.setUserId(user.getId());
        return response;
    }
}
