package com.project.localbrew.controller;

import com.project.localbrew.dto.request.DrinkRequest;
import com.project.localbrew.dto.response.DrinkResponse;
import com.project.localbrew.entity.Drink;
import com.project.localbrew.entity.DrinkCategory;
import com.project.localbrew.service.DrinkService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class DrinkController {

    private final DrinkService drinkService;

    public DrinkController(DrinkService drinkService) {
        this.drinkService = drinkService;
    }

    @GetMapping("/public/drinks")
    public ResponseEntity<List<DrinkResponse>> findAllDrinks(
            @RequestParam(required = false) List<DrinkCategory> categories,
            @RequestParam(required = false) String name) {

        List<Drink> drinks;

        if (name != null && !name.isBlank()) {
            drinks = drinkService.searchDrinksByName(name);
        } else if (categories != null && !categories.isEmpty()) {
            drinks = drinkService.findByCategories(categories);
        } else {
            drinks = drinkService.findAllDrinks();
        }

        List<DrinkResponse> response = drinks.stream()
                .map(this::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/owner/drinks")
    public ResponseEntity<DrinkResponse> createDrink(@Valid @RequestBody DrinkRequest request) {

        Drink drink = Drink.builder()
                .name(request.getName())
                .description(request.getDescription())
                .category(request.getCategory())
                .abv(request.getAbv())
                .origin(request.getOrigin())
                .imageUri(request.getImageUri())
                .build();

        Drink savedDrink = drinkService.saveDrink(drink);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(toResponse(savedDrink));
    }

    @PutMapping("/owner/drinks/{id}")
    public ResponseEntity<DrinkResponse> updateDrink(@PathVariable UUID id,
                                                     @Valid @RequestBody DrinkRequest request) {

        Drink drink = Drink.builder()
                .name(request.getName())
                .description(request.getDescription())
                .category(request.getCategory())
                .abv(request.getAbv())
                .origin(request.getOrigin())
                .imageUri(request.getImageUri())
                .build();

        Drink updatedDrink = drinkService.updateDrinkById(id, drink);

        return ResponseEntity.ok(toResponse(updatedDrink));
    }

    @DeleteMapping("/owner/drinks/{id}")
    public ResponseEntity<Void> deleteDrink(@PathVariable UUID id) {
        drinkService.deleteDrinkById(id);
        return ResponseEntity.noContent().build();
    }

    private DrinkResponse toResponse(Drink drink) {
        return DrinkResponse.builder()
                .id(drink.getId())
                .name(drink.getName())
                .description(drink.getDescription())
                .category(drink.getCategory())
                .abv(drink.getAbv())
                .origin(drink.getOrigin())
                .imageUri(drink.getImageUri())
                .build();
    }
}