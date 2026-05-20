package com.project.localbrew.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {
    
    @NotBlank(message = "Username è obbligatorio")
    @Size(min = 3, max = 50, message = "Username deve essere tra 3 e 50 caratteri")
    private String username;
    
    @NotBlank(message = "Email è obbligatoria")
    @Email(message = "Email deve essere valida")
    private String email;
    
    @NotBlank(message = "Password è obbligatoria")
    @Size(min = 8, message = "Password deve essere almeno 8 caratteri")
    private String password;
    
    @NotBlank(message = "Conferma password è obbligatoria")
    private String confirmPassword;
}
