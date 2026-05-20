package com.project.localbrew.dto.request;

import com.project.localbrew.entity.VenueStatus;
import com.project.localbrew.entity.VenueType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VenueCreateRequest {
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
    private VenueType type;

    @NotNull
    private VenueStatus status;
}
