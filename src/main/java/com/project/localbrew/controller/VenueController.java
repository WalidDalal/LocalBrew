package com.project.localbrew.controller;

import com.project.localbrew.dto.request.VenueRequest;
import com.project.localbrew.dto.response.VenueResponse;
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
        return ResponseEntity.ok(venueService.findAllVenues());
    }

    @GetMapping("/admin/venues/{id}")
    public ResponseEntity<VenueResponse> findVenueById(@PathVariable @NotNull(message = "ID non puo essere nullo") UUID id) {
        return ResponseEntity.ok(venueService.findVenueById(id));
    }

    @GetMapping("/admin/venues/pending")
    public ResponseEntity<List<VenueResponse>> findAllPendingVenues() {
        return ResponseEntity.ok(venueService.findAllPendingVenues());
    }

    @GetMapping("/admin/venues/suspended")
    public ResponseEntity<List<VenueResponse>> findAllSuspendedVenues() {
        return ResponseEntity.ok(venueService.findAllSuspendedVenues());
    }

    @GetMapping("/public/venues/active")
    public ResponseEntity<List<VenueResponse>> findAllActiveVenues() {
        return ResponseEntity.ok(venueService.findAllActiveVenues());
    }

    @GetMapping("/public/venues/{id}")
    public ResponseEntity<VenueResponse> findActiveVenueById(@PathVariable @NotNull(message = "ID non puo essere nullo") UUID id) {
        return ResponseEntity.ok(venueService.findActiveVenueById(id));
    }

    @GetMapping("/public/venues/search/city")
    public ResponseEntity<List<VenueResponse>> findAllActiveVenuesByCity(
            @RequestParam @NotBlank(message = "City non puo essere vuota") String city
    ) {
        return ResponseEntity.ok(venueService.findAllActiveVenuesByCity(city));
    }

    @GetMapping("/public/venues/search/name")
    public ResponseEntity<List<VenueResponse>> findAllActiveVenuesByName(
            @RequestParam @NotBlank(message = "Name non puo essere vuoto") String name
    ) {
        return ResponseEntity.ok(venueService.findAllActiveVenuesByName(name));
    }

    @GetMapping("/public/venues/search/type")
    public ResponseEntity<List<VenueResponse>> findAllActiveVenuesByType(
            @RequestParam @NotEmpty(message = "Types non puo essere vuota") List<VenueType> types
    ) {
        return ResponseEntity.ok(venueService.findAllActiveVenuesByType(types));
    }

    @GetMapping("/owner/venues")
    public ResponseEntity<List<VenueResponse>> findAllVenuesByCurrentOwner() {
        return ResponseEntity.ok(venueService.findAllVenuesByCurrentOwner());
    }

    @PostMapping("/owner/venues")
    public ResponseEntity<VenueResponse> createVenue(@Valid @RequestBody VenueRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(venueService.saveVenue(request));
    }

    @PutMapping("/owner/venues/{id}")
    public ResponseEntity<VenueResponse> updateVenue(
            @PathVariable @NotNull(message = "ID non puo essere nullo") UUID id,
            @Valid @RequestBody VenueRequest request
    ) {
        return ResponseEntity.ok(venueService.updateVenueById(request, id));
    }

    @PatchMapping("/admin/venues/{id}/activate")
    public ResponseEntity<VenueResponse> activateVenue(@PathVariable @NotNull(message = "ID non puo essere nullo") UUID id) {
        return ResponseEntity.ok(venueService.activateVenue(id));
    }

    @PatchMapping("/admin/venues/{id}/suspend")
    public ResponseEntity<VenueResponse> suspendVenue(@PathVariable @NotNull(message = "ID non puo essere nullo") UUID id) {
        return ResponseEntity.ok(venueService.suspendVenue(id));
    }

    @DeleteMapping("/owner/venues/{id}")
    public ResponseEntity<Void> deleteVenue(@PathVariable @NotNull(message = "ID non puo essere nullo") UUID id) {
        venueService.deleteVenueById(id);
        return ResponseEntity.noContent().build();
    }
}
