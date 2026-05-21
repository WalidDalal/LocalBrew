package com.project.localbrew.dto.response;

import com.project.localbrew.entity.DrinkCategory;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FavoriteDrinkResponse {
    private UUID id;
    private String drinkName;
    private DrinkCategory category;
    private Float abv;
    private String origin;
}
