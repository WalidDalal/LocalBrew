package com.project.localbrew.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequest {
    
    @NotBlank(message = "L'Email è obbligatoria")
    @Email(message = "L'Email deve essere valida")
    private String email;
    
    @NotBlank(message = "La Password è obbligatoria")
    private String password;
}
