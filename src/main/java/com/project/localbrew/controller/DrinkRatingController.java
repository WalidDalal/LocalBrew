package com.project.localbrew.controller;

import com.project.localbrew.dto.request.DrinkRatingRequest;
import com.project.localbrew.dto.response.DrinkRatingResponse;
import com.project.localbrew.entity.DrinkRating;
import com.project.localbrew.service.DrinkRatingService;
import com.project.localbrew.service.DrinkService;
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
    private final DrinkService drinkService;

    public DrinkRatingController(DrinkRatingService drinkRatingService, DrinkService drinkService) {
        this.drinkRatingService = drinkRatingService;
        this.drinkService = drinkService;
    }


    @GetMapping("/public/drinks/{id}/ratings")
    public ResponseEntity<List<DrinkRatingResponse>> findAllRatingByDrinkId(@PathVariable @NotNull(message = "ID non può essere nullo") UUID id) {
        List<DrinkRatingResponse> drinkRatingResponses = drinkRatingService.findAllDrinkRatingsByDrinkId(id)
                .stream()
                .map(this::toResponse)
                .toList();

        return ResponseEntity.ok(drinkRatingResponses);
    }

    @GetMapping("/public/drinks/{id}/ratings/average")
    public ResponseEntity<Double> findAverageRatingByDrinkId(@PathVariable @NotNull(message = "ID non può essere nullo") UUID id) {
        double average = drinkRatingService.findAverageDrinkRatingByDrinkId(id);

        return ResponseEntity.ok(average);
    }

    @GetMapping("/user/drink-ratings")
    public ResponseEntity<List<DrinkRatingResponse>> findMyDrinkRatings() {
        List<DrinkRatingResponse> drinkRatings = drinkRatingService.findAllDrinkRatingByUserId()
                .stream()
                .map(this::toResponse)
                .toList();

        return ResponseEntity.ok(drinkRatings);
    }

    @PostMapping("/user/drinks/{id}/ratings")
    public ResponseEntity<DrinkRatingResponse> createDrinkRating(@PathVariable @NotNull(message = "ID non può essere nullo") UUID id,
                                                                 @Valid @RequestBody DrinkRatingRequest request) {
        DrinkRating drinkRating = DrinkRating.builder()
                .rating(request.getRating())
                .drink(drinkService.findDrinkById(id))
                .build();

        DrinkRating savedDrinkRating = drinkRatingService.saveDrinkRating(drinkRating);

        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(savedDrinkRating));
    }

    @PutMapping("/user/drink-ratings/{id}")
    public ResponseEntity<DrinkRatingResponse> updateDrinkRating(@PathVariable @NotNull(message = "ID non può essere nullo") UUID id,
                                                                 @RequestBody @Valid DrinkRatingRequest request) {
        DrinkRating drinkRating = DrinkRating.builder()
                .rating(request.getRating())
                .build();

        DrinkRating updatedDrinkRating = drinkRatingService.updateDrinkRatingById(drinkRating, id);

        return ResponseEntity.ok(toResponse(updatedDrinkRating));
    }

    @DeleteMapping("/user/drink-ratings/{id}")
    public ResponseEntity<Void> deleteDrinkRating(@PathVariable @NotNull(message = "ID non può essere nullo") UUID id) {
        drinkRatingService.deleteDrinkRatingById(id);

        return ResponseEntity.noContent().build();
    }

    private DrinkRatingResponse toResponse(DrinkRating drinkRating) {
        return DrinkRatingResponse.builder()
                .id(drinkRating.getId())
                .rating(drinkRating.getRating())
                .username(drinkRating.getUser().getUsername())
                .drinkName(drinkRating.getDrink().getName())
                .createdAt(drinkRating.getCreatedAt())
                .build();
    }
}
