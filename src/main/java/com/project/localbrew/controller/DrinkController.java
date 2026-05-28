package com.project.localbrew.controller;

import com.project.localbrew.dto.request.DrinkRequest;
import com.project.localbrew.dto.response.DrinkResponse;
import com.project.localbrew.entity.DrinkCategory;
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
public class DrinkController {

    private final DrinkService drinkService;

    public DrinkController(DrinkService drinkService) {
        this.drinkService = drinkService;
    }

    @GetMapping("/public/drinks")
    public ResponseEntity<List<DrinkResponse>> findDrinks(
            @RequestParam(required = false) List<DrinkCategory> categories,
            @RequestParam(required = false) String name
    ) {
        return ResponseEntity.ok(drinkService.findDrinks(categories, name));
    }

    @PostMapping("/owner/drinks")
    public ResponseEntity<DrinkResponse> createDrink(@Valid @RequestBody DrinkRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(drinkService.saveDrink(request));
    }

    @PutMapping("/owner/drinks/{id}")
    public ResponseEntity<DrinkResponse> updateDrink(
            @PathVariable @NotNull(message = "ID non puo essere nullo") UUID id,
            @Valid @RequestBody DrinkRequest request
    ) {
        return ResponseEntity.ok(drinkService.updateDrinkById(id, request));
    }

    @DeleteMapping("/owner/drinks/{id}")
    public ResponseEntity<Void> deleteDrink(@PathVariable @NotNull(message = "ID non puo essere nullo") UUID id) {
        drinkService.deleteDrinkById(id);
        return ResponseEntity.noContent().build();
    }
}
