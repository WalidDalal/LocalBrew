package com.project.localbrew.service;

import com.project.localbrew.entity.Drink;
import com.project.localbrew.entity.DrinkCategory;

import java.util.List;
import java.util.UUID;

public interface DrinkService {

    // Create
    Drink saveDrink(Drink drink);

    // Read
    List<Drink> findAllDrinks();

    Drink findDrinkById(UUID id);

    // Update
    Drink updateDrinkById(UUID id, Drink drink);

    // Delete
    void deleteDrinkById(UUID id);

    // Utility
    List<Drink> searchDrinksByName(String name);
    List<Drink> findByCategories(List<DrinkCategory> categories);
}