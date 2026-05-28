package com.project.localbrew.service;

import com.project.localbrew.entity.FavoriteVenue;
import com.project.localbrew.entity.User;
import com.project.localbrew.entity.Venue;
import com.project.localbrew.exception.DrinkNotFoundException;
import com.project.localbrew.repository.FavoriteVenueRepository;
import com.project.localbrew.security.CurrentUserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class FavoriteVenueServiceImpl implements  FavoriteVenueService{
    private final FavoriteVenueRepository favoriteVenueRepository;
    private final VenueService venueService;
    private final CurrentUserService currentUserService;

    public FavoriteVenueServiceImpl(FavoriteVenueRepository favoriteVenueRepository, VenueService venueService, CurrentUserService currentUserService) {
        this.favoriteVenueRepository = favoriteVenueRepository;
        this.venueService = venueService;
        this.currentUserService = currentUserService;
    }

    @Override
    public FavoriteVenue saveFavoriteVenue(UUID venueId) {
        if(venueId == null)
            throw new DrinkNotFoundException("VenueId nullo");
        User user = currentUserService.getCurrentUser();
        Venue venue = venueService.findVenueById(venueId);
        LocalDateTime savedAt = LocalDateTime.now();

        FavoriteVenue favoriteVenue = FavoriteVenue.builder()
                .user(user)
                .venue(venue)
                .build();

        return favoriteVenueRepository.save(favoriteVenue);
    }

    @Override
    public List<FavoriteVenue> findByFavoriteVenues() {
        return List.of();
    }

    @Override
    public FavoriteVenue findByFavoriteVenuesId(UUID id) {
        return null;
    }

    @Override
    public void deleteFavoriteVenueByVenueId(UUID venueId) {

    }
}