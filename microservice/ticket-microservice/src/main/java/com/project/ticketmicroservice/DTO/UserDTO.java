package com.project.ticketmicroservice.DTO;

import com.project.ticketmicroservice.type.Role;

import java.util.List;

public record UserDTO(Long id, String firstname, String lastname, String email, String phone, List<Role> roles) {
}
