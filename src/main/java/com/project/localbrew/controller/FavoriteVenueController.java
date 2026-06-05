package com.project.localbrew.controller;

import com.project.localbrew.dto.response.FavoriteVenueResponse;
import com.project.localbrew.service.FavoriteVenueService;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequestMapping("/api/v1/user/favorite-venues")
public class FavoriteVenueController {

    private final FavoriteVenueService favoriteVenueService;

    public FavoriteVenueController(FavoriteVenueService favoriteVenueService) {
        this.favoriteVenueService = favoriteVenueService;
    }

    @GetMapping
    public ResponseEntity<List<FavoriteVenueResponse>> findMyFavoriteVenues() {
        return ResponseEntity.ok(favoriteVenueService.findAllByCurrentUser());
    }

    @PostMapping
    public ResponseEntity<FavoriteVenueResponse> addFavoriteVenue(
            @RequestParam @NotNull(message = "Venue ID non puo essere nullo") UUID venueId
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(favoriteVenueService.saveFavoriteVenue(venueId));
    }

    @DeleteMapping("/{venueId}")
    public ResponseEntity<Void> removeFavoriteVenue(
            @PathVariable @NotNull(message = "Venue ID non puo essere nullo") UUID venueId
    ) {
        favoriteVenueService.deleteFavoriteVenue(venueId);
        return ResponseEntity.noContent().build();
    }
}
