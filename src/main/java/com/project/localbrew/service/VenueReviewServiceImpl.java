package com.project.localbrew.service;

import com.project.localbrew.dto.request.VenueReviewRequest;
import com.project.localbrew.dto.response.VenueReviewResponse;
import com.project.localbrew.entity.User;
import com.project.localbrew.entity.Venue;
import com.project.localbrew.entity.VenueReview;
import com.project.localbrew.repository.UserRepository;
import com.project.localbrew.repository.VenueRepository;
import com.project.localbrew.repository.VenueReviewRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class VenueReviewServiceImpl
        implements VenueReviewService {

    private final VenueReviewRepository venueReviewRepository;
    private final UserRepository userRepository;
    private final VenueRepository venueRepository;

    public VenueReviewServiceImpl(
            VenueReviewRepository venueReviewRepository,
            UserRepository userRepository,
            VenueRepository venueRepository
    ) {
        this.venueReviewRepository = venueReviewRepository;
        this.userRepository = userRepository;
        this.venueRepository = venueRepository;
    }

    @Override
    public List<VenueReviewResponse> findAllReviews() {

        return venueReviewRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public VenueReviewResponse findReviewById(UUID id) {

        if (id == null) {
            throw new IllegalArgumentException(
                    "ID non può essere null");
        }

        VenueReview review =
                venueReviewRepository.findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Recensione non trovata"));

        return toResponse(review);
    }

    @Transactional
    @Override
    public VenueReviewResponse saveReview(
            VenueReviewRequest request,
            String username
    ) {

        if (request == null) {
            throw new IllegalArgumentException(
                    "La review request non può essere null");
        }

        User user = userRepository.findByEmail(username)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Utente non trovato"));

        Venue venue = venueRepository.findById(
                        request.getVenueId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Venue non trovata"));

        boolean alreadyReviewed =
                venueReviewRepository
                        .existsByUserId_IdAndVenueId_Id(
                                user.getId(),
                                venue.getId()
                        );

        if (alreadyReviewed) {

            throw new IllegalArgumentException(
                    "Hai già recensito questa venue");
        }

        VenueReview review = VenueReview.builder()
                .rating(request.getRating())
                .comment(request.getComment())
                .createdAt(LocalDateTime.now())
                .user(user)
                .venue(venue)
                .build();

        VenueReview savedReview =
                venueReviewRepository.save(review);

        return toResponse(savedReview);
    }

    @Transactional
    @Override
    public VenueReviewResponse updateReviewById(
            VenueReviewRequest request,
            UUID id,
            String username
    ) {

        if (id == null) {
            throw new IllegalArgumentException(
                    "ID non può essere null");
        }

        if (request == null) {
            throw new IllegalArgumentException(
                    "La request non può essere null");
        }

        VenueReview existingReview =
                venueReviewRepository.findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Recensione non trovata"));

        // Ownership check
        if (!existingReview.getUser()
                .getEmail()
                .equals(username)) {

            throw new IllegalArgumentException(
                    "Non puoi modificare recensioni di altri utenti");
        }

        // Rating
        if (request.getRating() != null &&
                request.getRating() >= 1 &&
                request.getRating() <= 5) {

            existingReview.setRating(
                    request.getRating());
        }

        // Comment
        if (request.getComment() != null &&
                !request.getComment().isBlank()) {

            existingReview.setComment(
                    request.getComment());
        }

        VenueReview updatedReview =
                venueReviewRepository.save(existingReview);

        return toResponse(updatedReview);
    }

    @Transactional
    @Override
    public void deleteReview(
            UUID id,
            String username
    ) {

        if (id == null) {
            throw new IllegalArgumentException(
                    "ID non può essere null");
        }

        VenueReview review =
                venueReviewRepository.findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Recensione non trovata"));

        // Ownership check
        if (!review.getUser()
                .getEmail()
                .equals(username)) {

            throw new IllegalArgumentException(
                    "Non puoi eliminare recensioni di altri utenti");
        }

        venueReviewRepository.delete(review);
    }

    @Override
    public List<VenueReviewResponse> findReviewsByVenueId(
            UUID venueId
    ) {

        if (venueId == null) {
            throw new IllegalArgumentException(
                    "Venue ID non può essere null");
        }

        return venueReviewRepository
                .findByVenueId_Id(venueId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<VenueReviewResponse> findReviewsByUserId(
            UUID userId
    ) {

        if (userId == null) {
            throw new IllegalArgumentException(
                    "User ID non può essere null");
        }

        return venueReviewRepository
                .findByUserId_Id(userId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private VenueReviewResponse toResponse(
            VenueReview review
    ) {

        return VenueReviewResponse.builder()
                .id(review.getId())
                .rating(review.getRating())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .username(
                        review.getUser() != null
                                ? review.getUser()
                                .getUsername()
                                : null
                )
                .venueName(
                        review.getVenue() != null
                                ? review.getVenue()
                                .getName()
                                : null
                )
                .build();
    }
}