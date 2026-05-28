package com.project.localbrew.dto.response;

import lombok.*;

import java.time.LocalDateTime;
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
    private LocalDateTime createdAt;
    private String username;
    private String venueName;
}
