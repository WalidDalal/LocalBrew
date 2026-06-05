package com.project.localbrew.dto.response;

import com.project.localbrew.entity.VenueType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FavoriteVenueResponse {
    private UUID id;
    private UUID venueId;
    private String venueName;
    private String address;
    private String city;
    private VenueType venueType;
    private LocalDateTime savedAt;
}
