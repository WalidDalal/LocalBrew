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

    @NotNull(message = "La latitudine è obbligatoria")
    @DecimalMin(value = "-90.0", message = "Latitudine minima -90")
    @DecimalMax(value = "90.0", message = "Latitudine massima 90")
    private Double latitude;

    @NotNull(message = "La longitudine è obbligatoria")
    @DecimalMin(value = "-180.0", message = "Longitudine minima -180")
    @DecimalMax(value = "180.0", message = "Longitudine massima 180")
    private Double longitude;
}
