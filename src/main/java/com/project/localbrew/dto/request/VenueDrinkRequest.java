package com.project.localbrew.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public class VenueDrinkRequest {

    @NotNull(message = "Drink ID obbligatorio")
    private UUID drinkId;

    @DecimalMin(value = "0.0", message = "Prezzo non può essere negativo")
    private BigDecimal price;
}
