package com.civia.mandate.controller;

import com.civia.mandate.service.user.UserService;
import com.google.firebase.auth.UserRecord;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class UserController {

    private UserService userService;

    @PostMapping("/users")
    public ResponseEntity<UserRecord> createUser(@RequestBody CreateUserRequest request) {
        try {
            UserRecord userRecord = userService.createUser(request.email(), request.password(), request.displayName());
            return new ResponseEntity<>(userRecord, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

record CreateUserRequest(String email, String password, String displayName) {}