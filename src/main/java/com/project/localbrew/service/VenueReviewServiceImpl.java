package com.project.localbrew.service;

import com.project.localbrew.entity.VenueReview;
import com.project.localbrew.repository.VenueReviewRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class VenueReviewServiceImpl implements VenueReviewService {

    private final VenueReviewRepository venueReviewRepository;

    public VenueReviewServiceImpl(VenueReviewRepository venueReviewRepository) {
        this.venueReviewRepository = venueReviewRepository;
    }

    @Override
    public List<VenueReview> findAllReviews() {
        return venueReviewRepository.findAll();
    }

    @Override
    public VenueReview findReviewById(UUID id) {

        if (id == null) {
            throw new IllegalArgumentException("ID non può essere null");
        }

        Optional<VenueReview> review = venueReviewRepository.findById(id);

        return review.orElseThrow(() ->
                new IllegalArgumentException("Review non trovata con ID: " + id));
    }

    @Transactional
    @Override
    public VenueReview saveReview(VenueReview review) {

        if (review == null) {
            throw new IllegalArgumentException("Review non può essere null");
        }

        if (review.getRating() == null ||
                review.getRating() < 1 ||
                review.getRating() > 5) {
            throw new IllegalArgumentException("Rating deve essere tra 1 e 5");
        }

        if (review.getUserId() == null || review.getVenueId() == null) {
            throw new IllegalArgumentException("User e Venue sono obbligatori");
        }

        return venueReviewRepository.save(review);
    }

    @Transactional
    @Override
    public VenueReview updateReviewById(VenueReview review, UUID id) {

        if (id == null) {
            throw new IllegalArgumentException("ID non può essere null");
        }

        if (review == null) {
            throw new IllegalArgumentException("Review non può essere null");
        }

        VenueReview existing = venueReviewRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Review non trovata con ID: " + id));

        if (review.getRating() != null &&
                review.getRating() >= 1 &&
                review.getRating() <= 5) {
            existing.setRating(review.getRating());
        }

        if (review.getComment() != null && !review.getComment().isBlank()) {
            existing.setComment(review.getComment());
        }

        return venueReviewRepository.save(existing);
    }

    @Override
    public void deleteReviewById(UUID id) {

        if (id == null) {
            throw new IllegalArgumentException("ID non può essere null");
        }

        VenueReview review = venueReviewRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Review non trovata con ID: " + id));

        venueReviewRepository.delete(review);
    }

    @Override
    public List<VenueReview> findReviewsByVenueId(UUID venueId) {

        if (venueId == null) {
            throw new IllegalArgumentException("Venue ID non può essere null");
        }

        return venueReviewRepository.findByVenueId_Id(venueId);
    }

    @Override
    public List<VenueReview> findReviewsByUserId(UUID userId) {

        if (userId == null) {
            throw new IllegalArgumentException("User ID non può essere null");
        }

        return venueReviewRepository.findByUserId_Id(userId);
    }
}