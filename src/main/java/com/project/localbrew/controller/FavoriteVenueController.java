package com.project.localbrew.controller;

import com.project.localbrew.service.FavoriteDrinkService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/V1")
public class FavoriteVenueController {
    private final FavoriteDrinkService favoriteDrinkService;

    public FavoriteVenueController(FavoriteDrinkService favoriteDrinkService) {
        this.favoriteDrinkService = favoriteDrinkService;
    }


}
