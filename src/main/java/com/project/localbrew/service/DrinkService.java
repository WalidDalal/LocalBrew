package com.project.localbrew.service;

import com.project.localbrew.entity.Drink;

import java.util.List;
import java.util.UUID;

public interface DrinkService {

    // CRUD
    List<Drink> findAllDrinks();

    Drink findDrinkById(UUID id);

    Drink saveDrink(Drink drink);

    Drink updateDrinkById(Drink drink, UUID id);

    void deleteDrinkById(UUID id);

    // Utility
    List<Drink> searchDrinksByName(String name);
}