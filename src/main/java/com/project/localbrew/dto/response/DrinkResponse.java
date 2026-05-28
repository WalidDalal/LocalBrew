package com.project.localbrew.dto.response;

import com.project.localbrew.entity.DrinkCategory;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DrinkResponse {

    private UUID id;

    private String name;

    private String description;

    private DrinkCategory category;

    private String imageUri;

    private Double abv;

    private String origin;
}