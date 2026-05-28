package com.project.localbrew.controller;

import com.project.localbrew.dto.request.UserRoleUpdateRequest;
import com.project.localbrew.dto.response.UserResponse;
import com.project.localbrew.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Validated
@RestController
@RequestMapping("/api/v1/admin/users")
public class AdminUserController {

    private final UserService userService;

    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    @PatchMapping("/{id}/role")
    public ResponseEntity<UserResponse> updateUserRole(
            @PathVariable @NotNull(message = "ID non puo essere nullo") UUID id,
            @Valid @RequestBody UserRoleUpdateRequest request
    ) {
        return ResponseEntity.ok(userService.updateUserRole(id, request.getRole()));
    }
}
