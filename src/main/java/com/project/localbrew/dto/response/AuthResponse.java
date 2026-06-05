package com.project.localbrew.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {

    private UserResponse user;

    // JWT token
    private String token;

    // Messaggio di risposta
    private String message;

    // Stato operazione
    private boolean success;
}
