package com.project.usermicroservice.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {
    private int httpCode;
    private String message;

    private Long userId;
    private String accessToken;
    private String refreshToken;

    private UserDTO userDTO;
    private List<UserDTO> userDTOList;
}
