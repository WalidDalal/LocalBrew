package com.project.localbrew.service;

import com.project.localbrew.dto.request.DrinkRatingRequest;
import com.project.localbrew.dto.response.DrinkRatingResponse;
import com.project.localbrew.entity.Drink;
import com.project.localbrew.entity.DrinkRating;
import com.project.localbrew.entity.Role;
import com.project.localbrew.entity.User;
import com.project.localbrew.repository.DrinkRatingRepository;
import com.project.localbrew.repository.DrinkRepository;
import com.project.localbrew.security.CurrentUserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class DrinkRatingServiceImpl implements DrinkRatingService {

    private final DrinkRatingRepository drinkRatingRepository;
    private final DrinkRepository drinkRepository;
    private final CurrentUserService currentUserService;

    public DrinkRatingServiceImpl(
            DrinkRatingRepository drinkRatingRepository,
            DrinkRepository drinkRepository,
            CurrentUserService currentUserService
    ) {
        this.drinkRatingRepository = drinkRatingRepository;
        this.drinkRepository = drinkRepository;
        this.currentUserService = currentUserService;
    }

    @Override
    public List<DrinkRatingResponse> findAllDrinkRatingsByDrinkId(UUID drinkId) {
        if (drinkId == null) {
            throw new IllegalArgumentException("Drink ID non puo essere null");
        }

        return drinkRatingRepository.findAllByDrinkId(drinkId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public double findAverageDrinkRatingByDrinkId(UUID drinkId) {
        if (drinkId == null) {
            throw new IllegalArgumentException("Drink ID non puo essere null");
        }

        return drinkRatingRepository.findAllByDrinkId(drinkId)
                .stream()
                .mapToInt(DrinkRating::getRating)
                .average()
                .orElse(0.0);
    }

    @Override
    public List<DrinkRatingResponse> findAllDrinkRatingsByCurrentUser() {
        User currentUser = currentUserService.getCurrentUser();

        return drinkRatingRepository.findAllByUserId(currentUser.getId())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public DrinkRatingResponse saveDrinkRating(UUID drinkId, DrinkRatingRequest request) {
        if (drinkId == null) {
            throw new IllegalArgumentException("Drink ID non puo essere null");
        }
        if (request == null) {
            throw new IllegalArgumentException("Request non puo essere null");
        }

        Drink drink = drinkRepository.findById(drinkId)
                .orElseThrow(() -> new EntityNotFoundException("Drink non trovato con ID: " + drinkId));
        User currentUser = currentUserService.getCurrentUser();

        boolean exists = drinkRatingRepository.existsByUserIdAndDrinkId(currentUser.getId(), drink.getId());
        if (exists) {
            throw new IllegalArgumentException("Hai gia valutato questo drink");
        }

        DrinkRating drinkRating = DrinkRating.builder()
                .rating(request.getRating())
                .drink(drink)
                .user(currentUser)
                .build();

        return toResponse(drinkRatingRepository.save(drinkRating));
    }

    @Override
    public DrinkRatingResponse updateDrinkRatingById(UUID id, DrinkRatingRequest request) {
        if (id == null) {
            throw new IllegalArgumentException("ID non puo essere null");
        }
        if (request == null) {
            throw new IllegalArgumentException("Request non puo essere null");
        }

        DrinkRating existing = findEntityById(id);
        User currentUser = currentUserService.getCurrentUser();

        if (!existing.getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Non puoi modificare il rating di un altro utente");
        }

        existing.setRating(request.getRating());

        return toResponse(drinkRatingRepository.save(existing));
    }

    @Override
    public void deleteDrinkRatingById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID non puo essere null");
        }

        DrinkRating existing = findEntityById(id);
        User currentUser = currentUserService.getCurrentUser();
        boolean isAdmin = currentUser.getRole() == Role.ADMIN;
        boolean isOwnerOfRating = existing.getUser().getId().equals(currentUser.getId());

        if (!isAdmin && !isOwnerOfRating) {
            throw new AccessDeniedException("Non puoi eliminare il rating di un altro utente");
        }

        drinkRatingRepository.delete(existing);
    }

    private DrinkRating findEntityById(UUID id) {
        return drinkRatingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("DrinkRating non trovato con ID: " + id));
    }

    private DrinkRatingResponse toResponse(DrinkRating drinkRating) {
        return DrinkRatingResponse.builder()
                .id(drinkRating.getId())
                .rating(drinkRating.getRating())
                .username(drinkRating.getUser().getUsername())
                .drinkName(drinkRating.getDrink().getName())
                .createdAt(drinkRating.getCreatedAt())
                .build();
    }
}
