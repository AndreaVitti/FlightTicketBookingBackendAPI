package com.project.usermicroservice.handler;

import com.project.usermicroservice.DTO.Response;
import com.project.usermicroservice.exception.UserNotFound;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserNotFoundHandler {

    @ExceptionHandler(UserNotFound.class)
    public ResponseEntity<Response> handlerUser(UserNotFound e) {
        Response response = new Response();
        response.setHttpCode(404);
        response.setMessage(e.getMessage());
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }
}
