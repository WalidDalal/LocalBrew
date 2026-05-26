package com.project.localbrew.controller;

import com.project.localbrew.dto.request.VenueRequest;
import com.project.localbrew.dto.response.VenueResponse;
import com.project.localbrew.entity.Venue;
import com.project.localbrew.service.UserService;
import com.project.localbrew.service.VenueService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class VenueController {

    private final VenueService venueService;
    private final UserService userService;

    public VenueController(
            VenueService venueService,
            UserService userService
    ) {
        this.venueService = venueService;
        this.userService = userService;
    }

    @GetMapping("/public/venues/active")
    public ResponseEntity<List<VenueResponse>> findActiveVenues() {
        List<VenueResponse> venues = venueService.findActiveVenues()
                .stream()
                .map(this::toResponse)
                .toList();

        return ResponseEntity.ok(venues);
    }

    @GetMapping("/public/venues/{id}")
    public ResponseEntity<VenueResponse> findPublicVenueById(@PathVariable UUID id) {
        Venue venue = venueService.findActiveVenueById(id);

        return ResponseEntity.ok(toResponse(venue));
    } //fa si che le venue PENDING o SUSPENDED non siano visibili pubblicamente

    @PostMapping("/venues")
    public ResponseEntity<VenueResponse> createVenue(@Valid @RequestBody VenueRequest request) {
        Venue venue = Venue.builder()
                .name(request.getName())
                .description(request.getDescription())
                .city(request.getCity())
                .address(request.getAddress())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .type(request.getType())
                .build();

        Venue savedVenue = venueService.saveVenue(venue);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(toResponse(savedVenue));
    }

    @PutMapping("/owner/venues/{id}")
    public ResponseEntity<VenueResponse> updateVenue(
            @PathVariable UUID id,
            @Valid @RequestBody VenueRequest request
    ) {
        Venue venue = Venue.builder()
                .name(request.getName())
                .description(request.getDescription())
                .city(request.getCity())
                .address(request.getAddress())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .type(request.getType())
                .build();

        Venue updatedVenue = venueService.updateVenueById(
                venue,
                id
        );

        return ResponseEntity.ok(toResponse(updatedVenue));
    }

    @PatchMapping("/admin/venues/{id}/activate")
    public ResponseEntity<VenueResponse> activateVenue(
            @PathVariable UUID id
    ) {
        Venue venue = venueService.activateVenue(id);

        return ResponseEntity.ok(toResponse(venue));
    }

    @PatchMapping("/admin/venues/{id}/suspend")
    public ResponseEntity<VenueResponse> suspendVenue(
            @PathVariable UUID id
    ) {
        Venue venue = venueService.suspendVenue(id);

        return ResponseEntity.ok(toResponse(venue));
    }

    @DeleteMapping("/admin/venues/{id}")
    public ResponseEntity<Void> deleteVenue(
            @PathVariable UUID id
    ) {
        venueService.deleteVenueById(id);

        return ResponseEntity.noContent().build();
    }

    private VenueResponse toResponse(Venue venue) {
        return VenueResponse.builder()
                .id(venue.getId())
                .name(venue.getName())
                .description(venue.getDescription())
                .city(venue.getCity())
                .address(venue.getAddress())
                .latitude(venue.getLatitude())
                .longitude(venue.getLongitude())
                .type(venue.getType())
                .status(venue.getStatus())
                .createdAt(LocalDateTime.from(venue.getCreatedAt()))
                .ownerUsername(
                        venue.getOwner() != null
                                ? venue.getOwner().getUsername()
                                : null
                )
                .build();
    }
}

