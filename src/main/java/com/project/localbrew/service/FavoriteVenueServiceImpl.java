package com.project.localbrew.service;

import com.project.localbrew.entity.FavoriteVenue;
import com.project.localbrew.entity.User;
import com.project.localbrew.entity.Venue;
import com.project.localbrew.entity.VenueStatus;
import com.project.localbrew.exception.DuplicatedResourceException;
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
public class FavoriteVenueServiceImpl implements  FavoriteVenueService{
    private final FavoriteVenueRepository favoriteVenueRepository;
    private final VenueRepository venueRepository;
    private final CurrentUserService currentUserService;

    public FavoriteVenueServiceImpl(FavoriteVenueRepository favoriteVenueRepository, VenueRepository venueRepository, CurrentUserService currentUserService) {
        this.favoriteVenueRepository = favoriteVenueRepository;
        this.venueRepository = venueRepository;
        this.currentUserService = currentUserService;
    }

    @Override
    public FavoriteVenue saveFavoriteVenue(UUID venueId) {
        if(venueId == null)
            throw new IllegalArgumentException("VenueId nullo");

        User user = currentUserService.getCurrentUser();
        Venue venue = venueRepository.findById(venueId).orElseThrow(() -> new EntityNotFoundException("Negozio non trovato"));

        if (venue.getStatus() != VenueStatus.ACTIVE) {
            throw new IllegalArgumentException("Puoi aggiungere ai preferiti solo locali attivi");
        }

        if(favoriteVenueRepository.existsByUser_IdAndVenue_Id(user.getId(), venueId))
            throw new DuplicatedResourceException("Locale già presente nei preferiti");

        FavoriteVenue favoriteVenue = FavoriteVenue.builder()
                .user(user)
                .venue(venue)
                .build();

        return favoriteVenueRepository.save(favoriteVenue);
    }

    @Override
    public List<FavoriteVenue> findMyFavoriteVenues() {
        User user = currentUserService.getCurrentUser();

        return favoriteVenueRepository.findAllByUser_Id(user.getId());
    }

    @Override
    public FavoriteVenue findFavoriteVenueById(UUID id) {
        return favoriteVenueRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Preferito non trovato"));
    }

    @Override
    public void deleteFavoriteVenueByVenueId(UUID venueId) {
        User user = currentUserService.getCurrentUser();

        FavoriteVenue favoriteVenue = favoriteVenueRepository.findByUser_IdAndVenue_Id(user.getId(), venueId).orElseThrow(() -> new EntityNotFoundException("Locale preferito non trovato"));

        favoriteVenueRepository.delete(favoriteVenue);
    }
}