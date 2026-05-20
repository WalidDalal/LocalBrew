package com.project.localbrew.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {
    
    private UserResponse user;
    private String message;
    private boolean success;
}
