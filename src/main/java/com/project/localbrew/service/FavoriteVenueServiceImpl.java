package com.project.localbrew.service;

import com.project.localbrew.dto.response.FavoriteVenueResponse;
import com.project.localbrew.entity.FavoriteVenue;
import com.project.localbrew.entity.User;
import com.project.localbrew.entity.Venue;
import com.project.localbrew.repository.FavoriteVenueRepository;
import com.project.localbrew.repository.VenueRepository;
import com.project.localbrew.security.CurrentUserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class FavoriteVenueServiceImpl implements FavoriteVenueService {

    private final FavoriteVenueRepository favoriteVenueRepository;
    private final VenueRepository venueRepository;
    private final CurrentUserService currentUserService;

    public FavoriteVenueServiceImpl(
            FavoriteVenueRepository favoriteVenueRepository,
            VenueRepository venueRepository,
            CurrentUserService currentUserService
    ) {
        this.favoriteVenueRepository = favoriteVenueRepository;
        this.venueRepository = venueRepository;
        this.currentUserService = currentUserService;
    }

    @Override
    public List<FavoriteVenueResponse> findAllByCurrentUser() {
        User currentUser = currentUserService.getCurrentUser();

        return favoriteVenueRepository.findAllByUserId(currentUser.getId())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public FavoriteVenueResponse saveFavoriteVenue(UUID venueId) {
        if (venueId == null) {
            throw new IllegalArgumentException("Venue ID non puo essere null");
        }

        User currentUser = currentUserService.getCurrentUser();
        Venue venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new EntityNotFoundException("Venue non trovata con ID: " + venueId));

        boolean exists = favoriteVenueRepository.existsByUserIdAndVenueId(currentUser.getId(), venue.getId());
        if (exists) {
            throw new IllegalArgumentException("Hai gia aggiunto ai preferiti questa venue");
        }

        FavoriteVenue favoriteVenue = FavoriteVenue.builder()
                .user(currentUser)
                .venue(venue)
                .build();

        return toResponse(favoriteVenueRepository.save(favoriteVenue));
    }

    @Override
    public void deleteFavoriteVenue(UUID venueId) {
        if (venueId == null) {
            throw new IllegalArgumentException("Venue ID non puo essere null");
        }

        User currentUser = currentUserService.getCurrentUser();
        FavoriteVenue favoriteVenue = favoriteVenueRepository
                .findByUserIdAndVenueId(currentUser.getId(), venueId)
                .orElseThrow(() -> new EntityNotFoundException("Preferito non trovato per venueId: " + venueId));

        favoriteVenueRepository.delete(favoriteVenue);
    }

    private FavoriteVenueResponse toResponse(FavoriteVenue favoriteVenue) {
        Venue venue = favoriteVenue.getVenue();

        return FavoriteVenueResponse.builder()
                .id(favoriteVenue.getId())
                .venueId(venue.getId())
                .venueName(venue.getName())
                .address(venue.getAddress())
                .city(venue.getCity())
                .venueType(venue.getType())
                .savedAt(favoriteVenue.getSavedAt())
                .build();
    }
}
