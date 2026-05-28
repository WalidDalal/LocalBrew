package com.project.localbrew.controller;

import com.project.localbrew.dto.request.VenueRequest;
import com.project.localbrew.dto.response.VenueResponse;
import com.project.localbrew.entity.Venue;
import com.project.localbrew.entity.VenueType;
import com.project.localbrew.service.VenueService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequestMapping("/api/v1")
public class VenueController {

    private final VenueService venueService;

    public VenueController(VenueService venueService) {
        this.venueService = venueService;
    }

    @GetMapping("/admin/venues")
    public ResponseEntity<List<VenueResponse>> findAllVenues() {
        List<VenueResponse> venues = venueService.findAllVenues()
                .stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(venues);
    }

    @GetMapping("/admin/venues/{id}")
    public ResponseEntity<VenueResponse> findVenueById(@PathVariable @NotNull(message = "ID non può essere nullo") UUID id) {
        Venue venue = venueService.findVenueById(id);

        return ResponseEntity.ok(toResponse(venue));
    }

    @GetMapping("/public/venues/active")
    public ResponseEntity<List<VenueResponse>> findAllActiveVenues() {
        List<VenueResponse> venues = venueService.findAllActiveVenues()
                .stream()
                .map(this::toResponse)
                .toList();

        return ResponseEntity.ok(venues);
    }

    @GetMapping("/admin/venues/pending")
    public ResponseEntity<List<VenueResponse>> findAllPendingVenues() {
        List<VenueResponse> venues = venueService.findAllPendingVenues()
                .stream()
                .map(this::toResponse)
                .toList();

        return ResponseEntity.ok(venues);
    }

    @GetMapping("/admin/venues/suspended")
    public ResponseEntity<List<VenueResponse>> findAllSuspendedVenues() {
        List<VenueResponse> venues = venueService.findAllSuspendedVenues()
                .stream()
                .map(this::toResponse)
                .toList();

        return ResponseEntity.ok(venues);
    }

    @GetMapping("/public/venues/{id}")
    public ResponseEntity<VenueResponse> findActiveVenueById(@PathVariable @NotNull(message = "ID non puo essere nullo") UUID id) {
        return ResponseEntity.ok(venueService.findActiveVenueById(id));
    }

    @GetMapping("/public/venues/search/city")
    public ResponseEntity<List<VenueResponse>> findAllActiveVenuesByCity(@RequestParam @NotBlank(message = "City non può essere null") String city) {
        List<VenueResponse> venues = venueService.findAllActiveVenuesByCity(city)
                .stream()
                .map(this::toResponse)
                .toList();

        return ResponseEntity.ok(venues);
    }

    @GetMapping("/public/venues/search/name")
    public ResponseEntity<List<VenueResponse>> findAllActiveVenuesByName(@RequestParam @NotBlank(message = "Name non può essere null") String name) {
        List<VenueResponse> venues = venueService.findAllActiveVenuesByName(name)
                .stream()
                .map(this::toResponse)
                .toList();

        return ResponseEntity.ok(venues);
    }

    @GetMapping("/public/venues/search/type")
    public ResponseEntity<List<VenueResponse>> findAllActiveVenuesByType(@RequestParam @NotEmpty(message = "Types non può essere vuota") List<VenueType> types) {
        List<VenueResponse> venues = venueService.findAllActiveVenuesByType(types)
                .stream()
                .map(this::toResponse)
                .toList();

        return ResponseEntity.ok(venues);
    }

    @PostMapping("/owner/venues/create")
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

    @GetMapping("/owner/venues")
    public ResponseEntity<List<VenueResponse>> findAllVenuesByCurrentOwner() {
        List<VenueResponse> venues = venueService.findAllVenuesByCurrentOwner()
                .stream()
                .map(this::toResponse)
                .toList();

        return ResponseEntity.ok(venues);
    }

    @PutMapping("/owner/venues/{id}")
    public ResponseEntity<VenueResponse> updateVenue(@PathVariable UUID id,
                                                     @Valid @RequestBody VenueRequest request) {
        Venue venue = Venue.builder()
                .name(request.getName())
                .description(request.getDescription())
                .city(request.getCity())
                .address(request.getAddress())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .type(request.getType())
                .build();

        Venue updatedVenue = venueService.updateVenueById(venue, id);

        return ResponseEntity.ok(toResponse(updatedVenue));
    }

    @PatchMapping("/admin/venues/{id}/activate")
    public ResponseEntity<VenueResponse> activateVenue(@PathVariable UUID id) {
        Venue venue = venueService.activateVenue(id);
        return ResponseEntity.ok(toResponse(venue));
    }

    @PatchMapping("/admin/venues/{id}/suspend")
    public ResponseEntity<VenueResponse> suspendVenue(@PathVariable UUID id) {
        Venue venue = venueService.suspendVenue(id);

        return ResponseEntity.ok(toResponse(venue));
    }

    @DeleteMapping("/owner/venues/{id}/delete")
    public ResponseEntity<Void> deleteVenue(@PathVariable UUID id) {
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
                .createdAt(venue.getCreatedAt())
                .ownerUsername(venue.getOwner().getUsername())
                .build();
    }
}


