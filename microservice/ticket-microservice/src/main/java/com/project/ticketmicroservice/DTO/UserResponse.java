package com.project.ticketmicroservice.DTO;

import lombok.Data;

@Data
public class UserResponse {
    private int httpCode;
    private UserDTO userDTO;
}
