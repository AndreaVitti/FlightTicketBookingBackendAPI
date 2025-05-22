package com.project.usermicroservice.controller;

import com.project.usermicroservice.DTO.Response;
import com.project.usermicroservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> getAllUsers(@RequestParam(value = "PageNum", defaultValue = "0", required = false) int pageNum,
                                                @RequestParam(value = "PageSize", defaultValue = "15", required = false) int pageSize) {
        Response response = userService.getAllUsers(pageNum, pageSize);
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> getUserById(@PathVariable("id") Long id) {
        Response response = userService.getUserById(id);
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> deleteUserById(@PathVariable("id") Long id) {
        Response response = userService.deleteUserById(id);
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }

    @GetMapping("/loggedUser")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public ResponseEntity<Response> getLoggedUser() {
        Response response = userService.getLoggedUser();
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }
}
