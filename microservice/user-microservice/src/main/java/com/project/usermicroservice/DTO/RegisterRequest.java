package com.project.usermicroservice.DTO;

import com.project.usermicroservice.type.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @NotBlank(message = "First name is required")
    private String firstname;
    @NotBlank(message = "Last name is required")
    private String lastname;
    @Email(message = "Email is required")
    private String email;
    @NotBlank(message = "Password required")
    @Size(min = 12, message = "Password too long")
    @Size(max = 20, message = "Password too long")
    private String password;
    @NotBlank(message = "Phone number required")
    @Size(max = 18, message = "Invalid phone number")
    private String phone;
    private List<Role> roles;
}
