package com.project.localbrew.service;

import com.project.localbrew.dto.response.FavoriteDrinkResponse;
import com.project.localbrew.entity.Drink;
import com.project.localbrew.entity.FavoriteDrink;
import com.project.localbrew.entity.User;
import com.project.localbrew.repository.DrinkRepository;
import com.project.localbrew.repository.FavoriteDrinkRepository;
import com.project.localbrew.security.CurrentUserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class FavoriteDrinkServiceImpl implements FavoriteDrinkService {

    private final FavoriteDrinkRepository favoriteDrinkRepository;
    private final DrinkRepository drinkRepository;
    private final CurrentUserService currentUserService;

    public FavoriteDrinkServiceImpl(
            FavoriteDrinkRepository favoriteDrinkRepository,
            DrinkRepository drinkRepository,
            CurrentUserService currentUserService
    ) {
        this.favoriteDrinkRepository = favoriteDrinkRepository;
        this.drinkRepository = drinkRepository;
        this.currentUserService = currentUserService;
    }

    @Override
    public List<FavoriteDrinkResponse> findAllByCurrentUser() {
        User currentUser = currentUserService.getCurrentUser();

        return favoriteDrinkRepository.findAllByUserId(currentUser.getId())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public FavoriteDrinkResponse saveFavoriteDrink(UUID drinkId) {
        if (drinkId == null) {
            throw new IllegalArgumentException("Drink ID non puo essere null");
        }

        User currentUser = currentUserService.getCurrentUser();
        Drink drink = drinkRepository.findById(drinkId)
                .orElseThrow(() -> new EntityNotFoundException("Drink non trovato con ID: " + drinkId));

        boolean exists = favoriteDrinkRepository.existsByUserIdAndDrinkId(currentUser.getId(), drink.getId());
        if (exists) {
            throw new IllegalArgumentException("Hai gia aggiunto ai preferiti questo drink");
        }

        FavoriteDrink favoriteDrink = FavoriteDrink.builder()
                .user(currentUser)
                .drink(drink)
                .build();

        return toResponse(favoriteDrinkRepository.save(favoriteDrink));
    }

    @Override
    public void deleteFavoriteDrink(UUID drinkId) {
        if (drinkId == null) {
            throw new IllegalArgumentException("Drink ID non puo essere null");
        }

        User currentUser = currentUserService.getCurrentUser();
        FavoriteDrink favoriteDrink = favoriteDrinkRepository
                .findByUserIdAndDrinkId(currentUser.getId(), drinkId)
                .orElseThrow(() -> new EntityNotFoundException("Preferito non trovato per drinkId: " + drinkId));

        favoriteDrinkRepository.delete(favoriteDrink);
    }

    private FavoriteDrinkResponse toResponse(FavoriteDrink favoriteDrink) {
        Drink drink = favoriteDrink.getDrink();

        return FavoriteDrinkResponse.builder()
                .id(favoriteDrink.getId())
                .drinkId(drink.getId())
                .drinkName(drink.getName())
                .category(drink.getCategory())
                .abv(drink.getAbv())
                .origin(drink.getOrigin())
                .savedAt(favoriteDrink.getSavedAt())
                .build();
    }
}
