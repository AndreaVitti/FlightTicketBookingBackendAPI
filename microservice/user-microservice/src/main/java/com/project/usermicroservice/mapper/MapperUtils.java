package com.project.usermicroservice.mapper;

import com.project.usermicroservice.DTO.UserDTO;
import com.project.usermicroservice.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MapperUtils {

    public UserDTO mapUserToUserDTO(User user) {
        return new UserDTO(user.getId(), user.getFirstname(), user.getLastname(), user.getEmail(), user.getPhone(), user.getRoles());
    }

    public List<UserDTO> mapUserListToUserDTOList(List<User> users) {
        return users.stream().map(user -> mapUserToUserDTO(user)).toList();
    }
}
