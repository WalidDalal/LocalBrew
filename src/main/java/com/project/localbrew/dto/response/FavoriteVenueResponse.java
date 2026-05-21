package com.project.localbrew.dto.response;

import com.project.localbrew.entity.VenueType;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FavoriteVenueResponse {
    private UUID id;
    private String venueName;
    private String address;
    private VenueType venueType;
}
