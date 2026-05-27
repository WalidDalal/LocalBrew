package com.project.localbrew.controller;

import com.project.localbrew.dto.response.UserResponse;
import com.project.localbrew.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> me(
            @AuthenticationPrincipal UserDetails userDetails
    ) {

        return ResponseEntity.ok(
                userService.getCurrentUser(
                        userDetails.getUsername()
                )
        );
    }
}