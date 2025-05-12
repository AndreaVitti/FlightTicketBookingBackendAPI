package com.project.usermicroservice.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AuthRequest(@Email(message = "Email name required")
                          String email,
                          @NotBlank(message = "Password required")
                          @Size(min = 12, message = "Password too short")
                          @Size(max = 20, message = "Password too long")
                          String password) {
}
