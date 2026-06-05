package com.project.localbrew.dto.response;

import com.project.localbrew.entity.DrinkCategory;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FavoriteDrinkResponse {
    private UUID id;
    private UUID drinkId;
    private String drinkName;
    private DrinkCategory category;
    private Double abv;
    private String origin;
    private LocalDateTime savedAt;
}