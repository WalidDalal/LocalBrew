package com.project.localbrew.service;

import com.project.localbrew.dto.request.DrinkRequest;
import com.project.localbrew.dto.response.DrinkResponse;
import com.project.localbrew.entity.Drink;
import com.project.localbrew.entity.DrinkCategory;

import java.util.List;
import java.util.UUID;

public interface DrinkService {

    List<DrinkResponse> findDrinks(List<DrinkCategory> categories, String name);

    Drink findDrinkById(UUID id);

    DrinkResponse saveDrink(DrinkRequest request);

    DrinkResponse updateDrinkById(UUID id, DrinkRequest request);

    void deleteDrinkById(UUID id);
}
