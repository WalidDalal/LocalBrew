package com.project.localbrew.service;

import com.project.localbrew.entity.VenueReview;

import java.util.List;
import java.util.UUID;

public interface VenueReviewService {

    // CRUD
    List<VenueReview> findAllReviews();

    VenueReview findReviewById(UUID id);

    VenueReview saveReview(VenueReview review);

    VenueReview updateReviewById(VenueReview review, UUID id);

    void deleteReviewById(UUID id);

    // Utility
    List<VenueReview> findReviewsByVenueId(UUID venueId);

    List<VenueReview> findReviewsByUserId(UUID userId);
}
