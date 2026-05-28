package com.project.localbrew.service;

import com.project.localbrew.entity.FavoriteDrink;

import java.util.List;
import java.util.UUID;

public interface FavoriteDrinkService {
    FavoriteDrink saveFavoriteDrink(FavoriteDrink favoriteDrink);

    List<FavoriteDrink> findAllByUserId(UUID userId);

    FavoriteDrink findFavoriteDrinkById(UUID id);

    void deleteFavoriteDrinkById(UUID id);
    void deleteFavoriteDrinkByUserIdAndDrinkId(UUID userId, UUID drinkId);
}
