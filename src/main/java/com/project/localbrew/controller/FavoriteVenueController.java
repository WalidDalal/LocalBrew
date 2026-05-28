package com.project.localbrew.controller;

import com.project.localbrew.dto.response.FavoriteVenueResponse;
import com.project.localbrew.entity.FavoriteVenue;
import com.project.localbrew.entity.Venue;
import com.project.localbrew.service.FavoriteVenueService;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/user/favorite-venues")
public class FavoriteVenueController {
    private final FavoriteVenueService favoriteVenueService;

    public FavoriteVenueController(FavoriteVenueService favoriteVenueService) {
        this.favoriteVenueService = favoriteVenueService;
    }

    @PostMapping("/{venueId}")
    public ResponseEntity<FavoriteVenueResponse> addFavoriteVenue(@PathVariable @NotNull(message = "Venue id non può essere nullo") UUID venueId){
        FavoriteVenue favoriteVenue = favoriteVenueService.saveFavoriteVenue(venueId);

        return  ResponseEntity.ok(toResponse(favoriteVenue));
    }

    @GetMapping
    public ResponseEntity<List<FavoriteVenueResponse>> findMyFavoriteVenues(){
        List<FavoriteVenueResponse> favoriteVenues = favoriteVenueService.findMyFavoriteVenues()
                .stream()
                .map(this::toResponse)
                .toList();

        return ResponseEntity.ok(favoriteVenues);
    }

    @DeleteMapping("/{venueId}")
    public ResponseEntity<Void> deleteFavoriteVenue(@PathVariable @NotNull(message = "Venue id non può essere nullo") UUID venueId){
        favoriteVenueService.deleteFavoriteVenueByVenueId(venueId);

        return ResponseEntity.noContent().build();
    }

    private FavoriteVenueResponse toResponse(FavoriteVenue favoriteVenue) {
        Venue venue = favoriteVenue.getVenue();

        return FavoriteVenueResponse.builder()
                .id(favoriteVenue.getId())
                .venueName(venue.getName())
                .address(venue.getAddress())
                .venueType(venue.getType())
                .build();
    }
}
