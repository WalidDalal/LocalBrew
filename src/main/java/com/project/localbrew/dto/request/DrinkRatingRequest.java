package com.project.localbrew.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DrinkRatingRequest {
    @NotNull(message = "Rating obbligatorio")
    @Min(value = 1, message = "Rating minimo deve essere 1")
    @Max(value = 5, message = "Rating massimo deve essere 5")
    private Integer rating;
}
