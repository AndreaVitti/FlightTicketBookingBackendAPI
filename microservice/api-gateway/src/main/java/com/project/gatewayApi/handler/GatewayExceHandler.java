package com.project.gatewayApi.handler;

import com.project.gatewayApi.DTO.Response;
import com.project.gatewayApi.exception.HeaderNotValid;
import com.project.gatewayApi.exception.NotAuthorize;
import com.project.gatewayApi.exception.TokenNotFound;
import com.project.gatewayApi.exception.UserToAuhtNotFound;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GatewayExceHandler {

    @ExceptionHandler(HeaderNotValid.class)
    public ResponseEntity<Response> headerHandler(HeaderNotValid e) {
        Response response = new Response(403, e.getMessage());
        return ResponseEntity.status(response.httpCode()).body(response);
    }

    @ExceptionHandler(TokenNotFound.class)
    public ResponseEntity<Response> tokenHandler(TokenNotFound e) {
        Response response = new Response(403, e.getMessage());
        return ResponseEntity.status(response.httpCode()).body(response);
    }

    @ExceptionHandler(NotAuthorize.class)
    public ResponseEntity<Response> authHandler(NotAuthorize e) {
        Response response = new Response(403, e.getMessage());
        return ResponseEntity.status(response.httpCode()).body(response);
    }

    @ExceptionHandler(UserToAuhtNotFound.class)
    public ResponseEntity<Response> userHandler(UserToAuhtNotFound e) {
        Response response = new Response(404, e.getMessage());
        return ResponseEntity.status(response.httpCode()).body(response);
    }
}
