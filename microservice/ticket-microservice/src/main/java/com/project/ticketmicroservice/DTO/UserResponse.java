package com.project.ticketmicroservice.DTO;

public record UserResponse(int httpCode, UserDTO userDTO) {
}
