package com.project.gatewayApi.DTO;

import lombok.Data;

public record Response (int httpCode, String message){
}
