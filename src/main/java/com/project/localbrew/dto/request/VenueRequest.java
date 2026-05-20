package com.project.localbrew.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VenueRequest {
    @NotBlank
    @Size(max = 50)
    private String name;

    @Size(max = 500)
    private String description;

    @NotBlank
    @Size(max = 70)
    private String address;

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;

    @NotNull
    private String type;

    @NotNull
    private String status;
}
