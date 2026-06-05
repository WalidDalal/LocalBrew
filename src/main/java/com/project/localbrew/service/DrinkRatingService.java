package com.project.localbrew.service;

import com.project.localbrew.dto.request.DrinkRatingRequest;
import com.project.localbrew.dto.response.DrinkRatingResponse;

import java.util.List;
import java.util.UUID;

public interface DrinkRatingService {

    List<DrinkRatingResponse> findAllDrinkRatingsByDrinkId(UUID drinkId);

    double findAverageDrinkRatingByDrinkId(UUID drinkId);

    List<DrinkRatingResponse> findAllDrinkRatingsByCurrentUser();

    DrinkRatingResponse saveDrinkRating(UUID drinkId, DrinkRatingRequest request);

    DrinkRatingResponse updateDrinkRatingById(UUID id, DrinkRatingRequest request);

    void deleteDrinkRatingById(UUID id);
}
