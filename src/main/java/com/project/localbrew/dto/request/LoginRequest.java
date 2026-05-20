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
    
    @NotBlank(message = "Email è obbligatoria")
    @Email(message = "Email deve essere valida")
    private String email;
    
    @NotBlank(message = "Password è obbligatoria")
    private String password;
}
