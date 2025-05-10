package com.project.gatewayApi.handler;

import com.project.gatewayApi.DTO.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GatewayExceHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(HeaderNotValid.class)
    public ResponseEntity<Response> headerHandler(HeaderNotValid e){
        Response response = new Response();
        response.setHttpCode(400);
        response.setMessage(e.getMessage());
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(NotAuthorize.class)
    public ResponseEntity<Response> authHandler(NotAuthorize e){
        Response response = new Response();
        response.setHttpCode(400);
        response.setMessage(e.getMessage());
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }
}
