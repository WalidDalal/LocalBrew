package com.project.localbrew.dto.response;

import com.project.localbrew.entity.VenueStatus;
import com.project.localbrew.entity.VenueType;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VenueResponse {
    private UUID id;
    private String name;
    private String description;
    private String address;
    private Double latitude;
    private Double longitude;
    private VenueType type;
    private VenueStatus status;
    private LocalDate createdAt;
    private UUID ownerId;

    private String ownerUsername;
}
