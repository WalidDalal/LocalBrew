package com.project.localbrew.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DrinkRatingResponse {
    private UUID id;
    private Integer rating;
    private String username;
    private String drinkName;
    private LocalDateTime createdAt;
}
