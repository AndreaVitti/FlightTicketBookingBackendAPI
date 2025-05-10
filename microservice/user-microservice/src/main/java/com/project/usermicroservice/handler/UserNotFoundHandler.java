package com.project.usermicroservice.handler;

import com.project.usermicroservice.DTO.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserNotFoundHandler {

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Response> handlerUsername(UsernameNotFoundException e){
        Response response = new Response();
        response.setHttpCode(404);
        response.setMessage(e.getMessage());
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }

    @ExceptionHandler(UserNotFound.class)
    public ResponseEntity<Response> handlerUser(UserNotFound e){
        Response response = new Response();
        response.setHttpCode(404);
        response.setMessage(e.getMessage());
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }
}
