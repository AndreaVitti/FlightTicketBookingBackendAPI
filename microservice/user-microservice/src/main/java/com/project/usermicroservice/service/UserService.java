package com.project.usermicroservice.service;

import com.project.usermicroservice.DTO.Response;
import com.project.usermicroservice.entity.User;
import com.project.usermicroservice.exception.UserNotFound;
import com.project.usermicroservice.mapper.MapperUtils;
import com.project.usermicroservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final MapperUtils mapperUtils;

    public Response getAllUsers() {
        Response response = new Response();
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            throw new UserNotFound("Users not found");
        }
        response.setHttpCode(200);
        response.setUserDTOList(mapperUtils.mapUserListToUserDTOList(users));
        return response;
    }

    public Response getUserById(Long id) {
        Response response = new Response();
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFound("User not found"));
        response.setHttpCode(200);
        response.setUserDTO(mapperUtils.mapUserToUserDTO(user));
        return response;
    }

    public Response deleteUserById(Long id) {
        Response response = new Response();
        userRepository.findById(id).orElseThrow(() -> new UserNotFound("User not found"));
        response.setHttpCode(200);
        userRepository.deleteById(id);
        return response;
    }

    public Response getLoggedUser() {
        Response response = new Response();
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        response.setHttpCode(200);
        response.setUserDTO(mapperUtils.mapUserToUserDTO(user));
        return response;
    }
}
