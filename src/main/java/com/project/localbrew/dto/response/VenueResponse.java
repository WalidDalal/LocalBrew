package com.project.localbrew.dto.response;

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
    private String type;
    private String status;
    private LocalDate createdAt;
    private UUID ownerId;
    private String ownerUsername;
}
