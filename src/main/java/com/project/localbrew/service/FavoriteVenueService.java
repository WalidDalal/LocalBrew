package com.project.localbrew.service;

import com.project.localbrew.dto.response.FavoriteVenueResponse;

import java.util.List;
import java.util.UUID;

public interface FavoriteVenueService {

    List<FavoriteVenueResponse> findAllByCurrentUser();

    FavoriteVenueResponse saveFavoriteVenue(UUID venueId);

    void deleteFavoriteVenue(UUID venueId);
}
