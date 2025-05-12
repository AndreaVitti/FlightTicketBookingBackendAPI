package com.project.usermicroservice.DTO;

import com.project.usermicroservice.type.Role;

import java.util.List;

public record UserDTO(Long id, String firstname, String lastname, String email, String phone, List<Role> roles) {
}
