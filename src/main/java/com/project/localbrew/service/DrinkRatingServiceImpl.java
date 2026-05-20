package com.project.localbrew.service;

import com.project.localbrew.entity.Drink;
import com.project.localbrew.entity.DrinkRating;
import com.project.localbrew.entity.User;
import com.project.localbrew.repository.DrinkRatingRepository;
import com.project.localbrew.repository.DrinkRepository;
import com.project.localbrew.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

// TODO
// creare eccezioni personalizzate

@Service
public class DrinkRatingServiceImpl implements DrinkRatingService {
    private final DrinkRatingRepository drinkRatingRepository;
    private final UserRepository userRepository;
    private final DrinkRepository drinkRepository;

    public DrinkRatingServiceImpl(DrinkRatingRepository drinkRatingRepository, UserRepository userRepository, DrinkRepository drinkRepository) {
        this.drinkRatingRepository = drinkRatingRepository;
        this.userRepository = userRepository;
        this.drinkRepository = drinkRepository;
    }

    @Override
    @Transactional
    public DrinkRating saveDrinkRating(DrinkRating drinkRating) {
        if (drinkRating == null) {
            throw new IllegalArgumentException("DrinkRating nullo");
        }

        drinkRepository.findById(drinkRating.getDrink().getId())
                .orElseThrow(() -> new EntityNotFoundException("Drink non trovato"));

        // controllo che non ci sia già questa coppia di id sul DB
        boolean exists = drinkRatingRepository.existsByUserIdAndDrinkId(drinkRating.getUser().getId(), drinkRating.getDrink().getId());

        if (exists) {
            throw new IllegalArgumentException("Hai già valutato questo drink");
        }

        return drinkRatingRepository.save(drinkRating);
    }

    @Override
    public List<DrinkRating> findAllDrinkRating() {
        return drinkRatingRepository.findAll();
    }

    @Override
    public DrinkRating findDrinkRatingById(UUID id) {
        return drinkRatingRepository.findById(id).
                orElseThrow(() -> new EntityNotFoundException("DrinkRating non trovato con ID: " + id));
    }

    @Override
    @Transactional
    public DrinkRating updateDrinkRatingById(DrinkRating drinkRating, UUID id) {
        if (drinkRating == null) {
            throw new IllegalArgumentException("DrinkRating nullo");
        }
        if (id == null) {
            throw new IllegalArgumentException("ID nullo");
        }

        DrinkRating existing = drinkRatingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("DrinkRating non trovato con ID: " + id));

        if (drinkRating.getRating() != null) {
            existing.setRating(drinkRating.getRating());
        }

        return drinkRatingRepository.save(existing);
    }

    @Override
    @Transactional
    public void deleteDrinkRatingById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID nullo");
        }

        DrinkRating savedDrinkRating = drinkRatingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("DrinkRating non trovato con ID: " + id));

        drinkRatingRepository.delete(savedDrinkRating);
    }

    private Drink findDrinkInsideDrinkRating(DrinkRating drinkRating) {
        return drinkRepository.findById(drinkRating.getDrink().getId()).orElseThrow(() -> new EntityNotFoundException("DrinkRating non trovato con ID: " + drinkRating.getDrink().getId()));
    }

    private User findUserInsideDrinkRating(DrinkRating drinkRating) {
        return userRepository.findById(drinkRating.getUser().getId()).orElseThrow(() -> new EntityNotFoundException("DrinkRating non trovato con ID: " + drinkRating.getUser().getId()));
    }
}