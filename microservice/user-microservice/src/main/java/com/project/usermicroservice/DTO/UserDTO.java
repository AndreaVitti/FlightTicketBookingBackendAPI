package com.project.usermicroservice.DTO;

import com.project.usermicroservice.type.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class UserDTO {
    private Long id;
    @NotBlank(message = "First name required")
    private String firstname;
    @NotBlank(message = "Last name required")
    private String lastname;
    @Email(message = "Email required")
    private String email;
    @NotBlank(message = "Phone number required")
    @Size(max = 18, message = "Invalid phone number")
    private String phone;
    private List<Role> roles;
}
