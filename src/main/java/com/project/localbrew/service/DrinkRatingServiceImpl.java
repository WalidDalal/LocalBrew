package com.project.localbrew.service;

import com.project.localbrew.entity.DrinkRating;
import com.project.localbrew.entity.User;
import com.project.localbrew.repository.DrinkRatingRepository;
import com.project.localbrew.repository.DrinkRepository;
import com.project.localbrew.security.CurrentUserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DrinkRatingServiceImpl implements DrinkRatingService {
    private final DrinkRatingRepository drinkRatingRepository;
    private final DrinkRepository drinkRepository;
    private final CurrentUserService currentUserService;

    public DrinkRatingServiceImpl(DrinkRatingRepository drinkRatingRepository, DrinkRepository drinkRepository, CurrentUserService currentUserService) {
        this.drinkRatingRepository = drinkRatingRepository;
        this.drinkRepository = drinkRepository;
        this.currentUserService = currentUserService;
    }

    @Override
    @Transactional
    public DrinkRating saveDrinkRating(DrinkRating drinkRating) {
        if (drinkRating == null) {
            throw new IllegalArgumentException("DrinkRating nullo");
        }

        drinkRepository.findById(drinkRating.getDrink().getId())
                .orElseThrow(() -> new EntityNotFoundException("Drink non trovato"));

        // utente recuperato dal JWT
        User user = currentUserService.getCurrentUser();
        drinkRating.setUser(user);

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
        return drinkRatingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("DrinkRating non trovato con ID: " + id));
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

        User currentUser = currentUserService.getCurrentUser();
        if (!existing.getUser().getId().equals(currentUser.getId())) {
            throw new IllegalArgumentException("Non puoi modificare il rating di un altro utente");
        }

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

        DrinkRating existing = drinkRatingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("DrinkRating non trovato con ID: " + id));

        User currentUser = currentUserService.getCurrentUser();
        if (!existing.getUser().getId().equals(currentUser.getId())) {
            throw new IllegalArgumentException("Non puoi eliminare il rating di un altro utente");
        }

        drinkRatingRepository.delete(existing);
    }

    @Override
    @Transactional
    public List<DrinkRating> findAllDrinkRatingByUserId() {
        User user = currentUserService.getCurrentUser();
        return drinkRatingRepository.findAllByUserId(user.getId());
    }
}