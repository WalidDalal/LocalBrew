package com.project.localbrew.dto.request;

import com.project.localbrew.entity.Role;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRoleUpdateRequest {

    @NotNull(message = "Role obbligatorio")
    private Role role;
}
