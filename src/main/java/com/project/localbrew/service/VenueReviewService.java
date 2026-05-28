package com.project.localbrew.service;

import com.project.localbrew.dto.request.VenueReviewRequest;
import com.project.localbrew.dto.response.VenueReviewResponse;

import java.util.List;
import java.util.UUID;

public interface VenueReviewService {

    // CRUD

    List<VenueReviewResponse> findAllReviews();

    VenueReviewResponse findReviewById(UUID id);

    VenueReviewResponse saveReview(
            VenueReviewRequest request,
            String username
    );

    VenueReviewResponse updateReviewById(
            VenueReviewRequest request,
            UUID id,
            String username
    );

    void deleteReview(
            UUID id,
            String username
    );
    // Utility

    List<VenueReviewResponse> findReviewsByVenueId(
            UUID venueId
    );

    List<VenueReviewResponse> findReviewsByUserId(
            UUID userId
    );
}