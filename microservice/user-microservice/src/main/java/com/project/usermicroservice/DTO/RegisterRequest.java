package com.project.usermicroservice.DTO;

import com.project.usermicroservice.type.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record RegisterRequest(@NotBlank(message = "First name is required")
                              String firstname,
                              @NotBlank(message = "Last name is required")
                              String lastname,
                              @Email(message = "Email is required")
                              String email,
                              @NotBlank(message = "Password required")
                              @Size(min = 12, message = "Password too long")
                              @Size(max = 20, message = "Password too long")
                              String password,
                              @NotBlank(message = "Phone number required")
                              @Size(max = 18, message = "Invalid phone number")
                              String phone,
                              @Enumerated(EnumType.STRING)
                              List<Role> roles) {
}
