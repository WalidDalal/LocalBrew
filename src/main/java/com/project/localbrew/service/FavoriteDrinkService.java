package com.project.localbrew.service;

import com.project.localbrew.dto.response.FavoriteDrinkResponse;

import java.util.List;
import java.util.UUID;

public interface FavoriteDrinkService {

    List<FavoriteDrinkResponse> findAllByCurrentUser();

    FavoriteDrinkResponse saveFavoriteDrink(UUID drinkId);

    void deleteFavoriteDrink(UUID drinkId);
}
