package com.project.localbrew.service;

import com.project.localbrew.entity.FavoriteVenue;

import java.util.List;
import java.util.UUID;

public interface FavoriteVenueService {
    FavoriteVenue saveFavoriteVenue(UUID venueId);
    List<FavoriteVenue> findByFavoriteVenues();
    FavoriteVenue findByFavoriteVenuesId(UUID id);
    void deleteFavoriteVenueByVenueId(UUID venueId);
}
