package com.project.localbrew.dto.response;

import com.project.localbrew.entity.DrinkCategory;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VenueDrinkResponse {
    private UUID id;
    private UUID venueId;
    private String venueName;
    private UUID drinkId;
    private String drinkName;
    private String drinkDescription;
    private DrinkCategory category;
    private Double abv;
    private String origin;
    private String imageUri;
    private BigDecimal price;
}
