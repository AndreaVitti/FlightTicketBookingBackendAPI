package com.project.ticketmicroservice.handler;

import com.project.ticketmicroservice.exception.FlightNotAvail;
import com.project.ticketmicroservice.exception.ServerErr;
import lombok.NonNull;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;
import java.net.URI;

@Component
public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(@NonNull ClientHttpResponse httpResponse) throws IOException {
        return httpResponse.getStatusCode().is5xxServerError() || httpResponse.getStatusCode().is4xxClientError();
    }

    @Override
    public void handleError(@NonNull URI url, @NonNull HttpMethod method, @NonNull ClientHttpResponse httpResponse) throws IOException {
        if (httpResponse.getStatusCode().is5xxServerError()) {
            throw new ServerErr("Server error " + httpResponse.getStatusCode());
        } else if (httpResponse.getStatusCode().is4xxClientError()) {
            if (httpResponse.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new FlightNotAvail("Flight not available");
            }
        }
    }
}