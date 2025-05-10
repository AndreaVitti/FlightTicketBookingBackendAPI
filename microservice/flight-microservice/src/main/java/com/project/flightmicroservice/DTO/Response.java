package com.project.flightmicroservice.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {
    private int httpCode;
    private String message;

    private FlightDTO flightDTO;
    private List<FlightDTO> flightDTOList;

    private AvailFlightResponse availFlightResponse;
    private List<AvailFlightResponse> availFlightResponseList;
}
