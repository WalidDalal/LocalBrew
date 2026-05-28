package com.project.localbrew.controller;

import com.project.localbrew.dto.request.DrinkRatingRequest;
import com.project.localbrew.dto.response.DrinkRatingResponse;
import com.project.localbrew.service.DrinkRatingService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequestMapping("/api/v1")
public class DrinkRatingController {

    private final DrinkRatingService drinkRatingService;

    public DrinkRatingController(DrinkRatingService drinkRatingService) {
        this.drinkRatingService = drinkRatingService;
    }

    @GetMapping("/public/drinks/{id}/ratings")
    public ResponseEntity<List<DrinkRatingResponse>> findAllRatingByDrinkId(
            @PathVariable @NotNull(message = "ID non puo essere nullo") UUID id
    ) {
        return ResponseEntity.ok(drinkRatingService.findAllDrinkRatingsByDrinkId(id));
    }

    @GetMapping("/public/drinks/{id}/ratings/average")
    public ResponseEntity<Double> findAverageRatingByDrinkId(
            @PathVariable @NotNull(message = "ID non puo essere nullo") UUID id
    ) {
        return ResponseEntity.ok(drinkRatingService.findAverageDrinkRatingByDrinkId(id));
    }

    @GetMapping("/user/drink-ratings")
    public ResponseEntity<List<DrinkRatingResponse>> findMyDrinkRatings() {
        return ResponseEntity.ok(drinkRatingService.findAllDrinkRatingsByCurrentUser());
    }

    @PostMapping("/user/drinks/{id}/ratings")
    public ResponseEntity<DrinkRatingResponse> createDrinkRating(
            @PathVariable @NotNull(message = "ID non puo essere nullo") UUID id,
            @Valid @RequestBody DrinkRatingRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(drinkRatingService.saveDrinkRating(id, request));
    }

    @PutMapping("/user/drink-ratings/{id}")
    public ResponseEntity<DrinkRatingResponse> updateDrinkRating(
            @PathVariable @NotNull(message = "ID non puo essere nullo") UUID id,
            @Valid @RequestBody DrinkRatingRequest request
    ) {
        return ResponseEntity.ok(drinkRatingService.updateDrinkRatingById(id, request));
    }

    @DeleteMapping("/user/drink-ratings/{id}")
    public ResponseEntity<Void> deleteDrinkRating(
            @PathVariable @NotNull(message = "ID non puo essere nullo") UUID id
    ) {
        drinkRatingService.deleteDrinkRatingById(id);
        return ResponseEntity.noContent().build();
    }
}
