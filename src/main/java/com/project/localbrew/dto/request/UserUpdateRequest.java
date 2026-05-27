package com.project.localbrew.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateRequest {

    @Size(
            min = 3,
            max = 50,
            message = "Lo username deve essere compreso tra 3 e 50 caratteri"
    )
    private String username;

    @Email(
            message = "L'email deve essere valida"
    )
    private String email;

    @Size(
            min = 8,
            message = "La password deve essere almeno 8 caratteri"
    )
    private String password;
}