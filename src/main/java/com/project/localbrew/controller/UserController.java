package com.project.localbrew.controller;

import com.project.localbrew.dto.request.UserUpdateRequest;
import com.project.localbrew.dto.response.UserResponse;
import com.project.localbrew.entity.User;
import com.project.localbrew.security.CurrentUserService;
import com.project.localbrew.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;
    private final CurrentUserService currentUserService;

    public UserController(UserService userService, CurrentUserService currentUserService) {
        this.userService = userService;
        this.currentUserService = currentUserService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> me() {
        User currentUser = currentUserService.getCurrentUser();
        return ResponseEntity.ok(userService.toResponse(currentUser));
    }

    @PutMapping("/me")
    public ResponseEntity<UserResponse> updateMe(@Valid @RequestBody UserUpdateRequest request) {
        User currentUser = currentUserService.getCurrentUser();
        User updatedUser = userService.updateUser(currentUser.getId(), request);
        return ResponseEntity.ok(userService.toResponse(updatedUser));
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMe() {
        User currentUser = currentUserService.getCurrentUser();
        userService.deleteUserById(currentUser.getId());
        return ResponseEntity.noContent().build();
      
}
