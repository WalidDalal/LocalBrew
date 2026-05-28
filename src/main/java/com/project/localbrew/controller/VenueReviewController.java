package com.project.localbrew.controller;

import com.project.localbrew.dto.request.VenueReviewRequest;
import com.project.localbrew.dto.response.VenueReviewResponse;
import com.project.localbrew.service.VenueReviewService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@Validated
public class VenueReviewController {

    private final VenueReviewService venueReviewService;

    public VenueReviewController(
            VenueReviewService venueReviewService
    ) {
        this.venueReviewService = venueReviewService;
    }

    @GetMapping("/admin/reviews")
    public ResponseEntity<List<VenueReviewResponse>>
    findAllReviews() {

        return ResponseEntity.ok(
                venueReviewService.findAllReviews()
        );
    }

    @GetMapping("/public/venues/{venueId}/reviews")
    public ResponseEntity<List<VenueReviewResponse>>
    findReviewsByVenueId(
            @PathVariable UUID venueId
    ) {

        return ResponseEntity.ok(
                venueReviewService
                        .findReviewsByVenueId(venueId)
        );
    }

    @PostMapping("/user/venue-reviews")
    public ResponseEntity<VenueReviewResponse>
    saveReview(
            @Valid @RequestBody
            VenueReviewRequest request,

            @AuthenticationPrincipal
            UserDetails userDetails
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        venueReviewService.saveReview(
                                request,
                                userDetails.getUsername()
                        )
                );
    }

    @PutMapping("/user/venue-reviews/{id}")
    public ResponseEntity<VenueReviewResponse>
    updateReview(
            @PathVariable UUID id,

            @Valid @RequestBody
            VenueReviewRequest request,

            @AuthenticationPrincipal
            UserDetails userDetails
    ) {

        return ResponseEntity.ok(
                venueReviewService.updateReviewById(
                        request,
                        id,
                        userDetails.getUsername()
                )
        );
    }

    @DeleteMapping("/user/venue-reviews/{id}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable UUID id,

            @AuthenticationPrincipal
            UserDetails userDetails
    ) {

        venueReviewService.deleteReview(
                id,
                userDetails.getUsername()
        );

        return ResponseEntity.noContent().build();
    }
}