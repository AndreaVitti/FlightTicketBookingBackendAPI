package com.project.ticketmicroservice.DTO;

import com.project.ticketmicroservice.type.Role;
import lombok.Data;

import java.util.List;

@Data
public class UserDTO {
    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private List<Role> roles;
}
