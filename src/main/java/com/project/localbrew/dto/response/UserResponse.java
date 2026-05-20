package com.project.localbrew.dto.response;

import com.project.localbrew.entity.Role;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    
    private UUID id;
    private String username;
    private String email;
    private Role role;
    private LocalDateTime createdAt;
}
