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
public class UserUpdateRequest {
    
    @Size(min = 3, max = 50, message = "Lo Username deve essere compreso tra 3 e 50 caratteri")
    private String username;
    
    @Email(message = "L'Email deve essere valida")
    private String email;
    
    @Size(min = 8, message = "La Password deve essere almeno 8 caratteri")
    private String password;
}
