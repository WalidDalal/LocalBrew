package com.project.localbrew.service;

import com.project.localbrew.entity.FavoriteDrink;

import java.util.List;
import java.util.UUID;

public interface FavoriteDrinkService {
    FavoriteDrink saveFavoriteDrink(FavoriteDrink favoriteDrink);

    List<FavoriteDrink> findAllFavoriteDrinks();

    FavoriteDrink findFavoriteDrinkById(UUID id);

    FavoriteDrink updateFavoriteDrinkById(FavoriteDrink favoriteDrink, UUID id);

    FavoriteDrink replaceFavoriteDrinkById(FavoriteDrink favoriteDrink, UUID id);

    void deleteFavoriteDrinkById(UUID id);
}
