package com.project.localbrew.dto.response;

import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VenueReviewResponse {

    private UUID id;

    private Integer rating;

    private String comment;

    private LocalDate createdAt;

    private UUID userId;

    private String username;

    private UUID venueId;
}