package com.civia.mandate.controller;

import com.civia.mandate.dto.inout.UserRequest;
import com.civia.mandate.dto.inout.UserResponse;
import com.civia.mandate.service.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@AllArgsConstructor
public class UserController {

    private UserService userService;

    @PostMapping("/users")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR')")
    public ResponseEntity<Void> createUser(@RequestBody UserRequest request) {
        try {
            boolean isUserCreated = userService.createUser(request);
            return new ResponseEntity<>(isUserCreated ? HttpStatus.CREATED : HttpStatus.ALREADY_REPORTED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/users")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'OFFICER', 'CLERK', 'INSPECTOR')")
    public ResponseEntity<UserResponse> getUser(@RequestHeader("Authorization") String token) {
        try {
            UserResponse userResponse = userService.getUser(token);
            return new ResponseEntity<>(userResponse, Objects.nonNull(userResponse) ? HttpStatus.FOUND : HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}