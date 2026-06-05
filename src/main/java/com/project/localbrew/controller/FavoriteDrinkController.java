package com.project.localbrew.controller;

import com.project.localbrew.dto.response.FavoriteDrinkResponse;
import com.project.localbrew.service.FavoriteDrinkService;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequestMapping("/api/v1/user/favorite-drinks")
public class FavoriteDrinkController {

    private final FavoriteDrinkService favoriteDrinkService;

    public FavoriteDrinkController(FavoriteDrinkService favoriteDrinkService) {
        this.favoriteDrinkService = favoriteDrinkService;
    }

    @GetMapping
    public ResponseEntity<List<FavoriteDrinkResponse>> findMyFavoriteDrinks() {
        return ResponseEntity.ok(favoriteDrinkService.findAllByCurrentUser());
    }

    @PostMapping
    public ResponseEntity<FavoriteDrinkResponse> addFavoriteDrink(
            @RequestParam @NotNull(message = "Drink ID non puo essere nullo") UUID drinkId
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(favoriteDrinkService.saveFavoriteDrink(drinkId));
    }

    @DeleteMapping("/{drinkId}")
    public ResponseEntity<Void> removeFavoriteDrink(
            @PathVariable @NotNull(message = "Drink ID non puo essere nullo") UUID drinkId
    ) {
        favoriteDrinkService.deleteFavoriteDrink(drinkId);
        return ResponseEntity.noContent().build();
    }
}
