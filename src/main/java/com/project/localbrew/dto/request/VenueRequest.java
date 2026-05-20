package com.project.localbrew.dto.request;

import com.project.localbrew.entity.VenueType;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VenueRequest {
    @NotBlank(message = "Il nome è obbligatorio")
    @Size(max = 50, message = "Il nome non può superare 50 caratteri")
    private String name;

    @Size(max = 500, message = "La descrizione non può superare 500 caratteri")
    private String description;

    @NotBlank(message = "L'indirizzo è obbligatorio")
    @Size(max = 70, message = "L'indirizzo non può superare 70 caratteri")
    private String address;

    @NotNull(message = "Il tipo è obbligatorio")
    private VenueType type;

}
