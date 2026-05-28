package com.project.localbrew.controller;

import com.project.localbrew.dto.response.FavoriteDrinkResponse;
import com.project.localbrew.entity.Drink;
import com.project.localbrew.entity.FavoriteDrink;
import com.project.localbrew.entity.User;
import com.project.localbrew.repository.FavoriteDrinkRepository;
import com.project.localbrew.security.CurrentUserService;
import com.project.localbrew.service.DrinkService;
import com.project.localbrew.service.FavoriteDrinkService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/user")
public class FavoriteDrinkController {

    private final FavoriteDrinkService favoriteDrinkService;
    private final CurrentUserService currentUserService;
    private final DrinkService drinkService;

    public FavoriteDrinkController(FavoriteDrinkService favoriteDrinkService,
                                   CurrentUserService currentUserService,
                                   DrinkService drinkService) {
        this.favoriteDrinkService = favoriteDrinkService;
        this.currentUserService = currentUserService;
        this.drinkService = drinkService;
    }

    @GetMapping("/favorite-drinks")
    public ResponseEntity<List<FavoriteDrinkResponse>> getFavoriteDrinks() {

        User currentUser = currentUserService.getCurrentUser();

        List<FavoriteDrinkResponse> response = favoriteDrinkService
                .findAllByUserId(currentUser.getId())
                .stream()
                .map(this::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/favorite-drinks")
    public ResponseEntity<FavoriteDrinkResponse> addFavoriteDrink(@RequestParam UUID drinkId) {

        User currentUser = currentUserService.getCurrentUser();
        Drink drink = drinkService.findDrinkById(drinkId);

        FavoriteDrink favoriteDrink = FavoriteDrink.builder()
                .user(currentUser)
                .drink(drink)
                .build();

        FavoriteDrink saved = favoriteDrinkService.saveFavoriteDrink(favoriteDrink);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(toResponse(saved));
    }

    @DeleteMapping("/favorite-drinks/{drinkId}")
    public ResponseEntity<Void> removeFavoriteDrink(@PathVariable UUID drinkId) {
        User currentUser = currentUserService.getCurrentUser();
        favoriteDrinkService.deleteFavoriteDrinkByUserIdAndDrinkId(currentUser.getId(), drinkId);
        return ResponseEntity.noContent().build();
    }

    private FavoriteDrinkResponse toResponse(FavoriteDrink favoriteDrink) {
        return FavoriteDrinkResponse.builder()
                .id(favoriteDrink.getId())
                .drinkId(favoriteDrink.getDrink().getId())
                .drinkName(favoriteDrink.getDrink().getName())
                .category(favoriteDrink.getDrink().getCategory())
                .abv(favoriteDrink.getDrink().getAbv())
                .origin(favoriteDrink.getDrink().getOrigin())
                .savedAt(favoriteDrink.getSavedAt())
                .build();
    }
}