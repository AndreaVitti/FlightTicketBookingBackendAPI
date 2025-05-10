package com.project.usermicroservice.mapper;

import com.project.usermicroservice.DTO.UserDTO;
import com.project.usermicroservice.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MapperUtils {

    public UserDTO mapUserToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFirstname(user.getFirstname());
        userDTO.setLastname(user.getLastname());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhone(user.getPhone());
        userDTO.setRoles(user.getRoles());
        return userDTO;
    }

    public List<UserDTO> mapUserListToUserDTOList(List<User> users) {
        return users.stream().map(user -> mapUserToUserDTO(user)).toList();
    }
}
