package com.project.localbrew.service;

import com.project.localbrew.entity.Drink;
import com.project.localbrew.entity.FavoriteDrink;
import com.project.localbrew.entity.User;
import com.project.localbrew.repository.DrinkRepository;
import com.project.localbrew.repository.FavoriteDrinkRepository;
import com.project.localbrew.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class FavoriteDrinkServiceImpl implements FavoriteDrinkService {
    private final FavoriteDrinkRepository favoriteDrinkRepository;
    private final UserRepository userRepository;
    private final DrinkRepository drinkRepository;

    public FavoriteDrinkServiceImpl(FavoriteDrinkRepository favoriteDrinkRepository, UserRepository userRepository, DrinkRepository drinkRepository) {
        this.favoriteDrinkRepository = favoriteDrinkRepository;
        this.userRepository = userRepository;
        this.drinkRepository = drinkRepository;
    }


    @Override
    public FavoriteDrink saveFavoriteDrink(FavoriteDrink favoriteDrink) {
        if (favoriteDrink == null) {
            throw new IllegalArgumentException("FavoriteDrink nullo");
        }
        return favoriteDrinkRepository.save(favoriteDrink);
    }

    @Override
    public List<FavoriteDrink> findAllFavoriteDrinks() {
        return favoriteDrinkRepository.findAll();
    }

    @Override
    public FavoriteDrink findFavoriteDrinkById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("Id nullo");
        }
        return favoriteDrinkRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("FavoriteDrink non trovato con id: " + id));
    }

    @Override
    @Transactional
    public FavoriteDrink updateFavoriteDrinkById(FavoriteDrink favoriteDrink, UUID id) {
        if (favoriteDrink == null) {
            throw new IllegalArgumentException("FavoriteDrink nullo");
        }

        if (id == null) {
            throw new IllegalArgumentException("Id nullo");
        }

        FavoriteDrink savedFavoriteDrink = favoriteDrinkRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("FavoriteDrink non trovato con id: " + id));


        if (favoriteDrink.getDrink() != null) {
            Drink drink = findDrinkInsideFavoriteDrink(favoriteDrink);
            savedFavoriteDrink.setDrink(drink);
        }

        if (favoriteDrink.getUser() != null) {
            User user = findUserInsideFavoriteDrink(favoriteDrink);
            savedFavoriteDrink.setUser(user);
        }

        return favoriteDrinkRepository.save(savedFavoriteDrink);
    }

    @Override
    public FavoriteDrink replaceFavoriteDrinkById(FavoriteDrink favoriteDrink, UUID id) {
        return null;
    }

    @Override
    public void deleteFavoriteDrinkById(UUID id) {

    }

    private User findUserInsideFavoriteDrink(FavoriteDrink favoriteDrink) {
        return userRepository.findById(favoriteDrink.getUser().getId())
                .orElseThrow(() -> new EntityNotFoundException("User non trovato con id: " + favoriteDrink.getUser().getId()));
    }

    private Drink findDrinkInsideFavoriteDrink(FavoriteDrink favoriteDrink) {
        return drinkRepository.findById(favoriteDrink.getDrink().getId())
                .orElseThrow(() -> new EntityNotFoundException("Drink non trovato con id: " + favoriteDrink.getDrink().getId()));
    }
}
