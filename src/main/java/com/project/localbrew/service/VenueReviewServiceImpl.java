package com.project.localbrew.service;

import com.project.localbrew.dto.request.VenueReviewRequest;
import com.project.localbrew.dto.response.VenueReviewResponse;
import com.project.localbrew.entity.Role;
import com.project.localbrew.entity.User;
import com.project.localbrew.entity.Venue;
import com.project.localbrew.entity.VenueReview;
import com.project.localbrew.entity.VenueStatus;
import com.project.localbrew.repository.VenueRepository;
import com.project.localbrew.repository.VenueReviewRepository;
import com.project.localbrew.security.CurrentUserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class VenueReviewServiceImpl implements VenueReviewService {

    private final VenueReviewRepository venueReviewRepository;
    private final VenueRepository venueRepository;
    private final CurrentUserService currentUserService;

    public VenueReviewServiceImpl(
            VenueReviewRepository venueReviewRepository,
            VenueRepository venueRepository,
            CurrentUserService currentUserService
    ) {
        this.venueReviewRepository = venueReviewRepository;
        this.venueRepository = venueRepository;
        this.currentUserService = currentUserService;
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
            throw new IllegalArgumentException("ID non puo essere null");
        }

        return toResponse(findEntityById(id));
    }

    @Override
    public List<VenueReviewResponse> findReviewsByVenueId(UUID venueId) {
        if (venueId == null) {
            throw new IllegalArgumentException("Venue ID non puo essere null");
        }

        return venueReviewRepository.findAllByVenue_Id(venueId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<VenueReviewResponse> findMyReviews() {
        User currentUser = currentUserService.getCurrentUser();

        return venueReviewRepository.findAllByUser_Id(currentUser.getId())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public VenueReviewResponse saveReview(VenueReviewRequest request) {
        validateRequest(request);

        User currentUser = currentUserService.getCurrentUser();
        Venue venue = findVenueById(request.getVenueId());

        if (venue.getStatus() != VenueStatus.ACTIVE) {
            throw new IllegalArgumentException("Puoi recensire solo locali attivi");
        }

        if (venueReviewRepository.existsByUser_IdAndVenue_Id(currentUser.getId(), venue.getId())) {
            throw new IllegalArgumentException("Hai gia recensito questo locale");
        }

        VenueReview review = VenueReview.builder()
                .rating(request.getRating())
                .comment(request.getComment())
                .createdAt(LocalDateTime.now())
                .user(currentUser)
                .venue(venue)
                .build();

        return toResponse(venueReviewRepository.save(review));
    }

    @Override
    public VenueReviewResponse updateReviewById(VenueReviewRequest request, UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID non puo essere null");
        }
        validateRequest(request);

        VenueReview existingReview = findEntityById(id);
        User currentUser = currentUserService.getCurrentUser();

        if (!existingReview.getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Non puoi modificare recensioni di altri utenti");
        }

        existingReview.setRating(request.getRating());
        existingReview.setComment(request.getComment());

        return toResponse(venueReviewRepository.save(existingReview));
    }

    @Override
    public void deleteReviewById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID non puo essere null");
        }

        VenueReview review = findEntityById(id);
        User currentUser = currentUserService.getCurrentUser();

        boolean isAdmin = currentUser.getRole() == Role.ADMIN;
        boolean isReviewOwner = review.getUser().getId().equals(currentUser.getId());

        if (!isAdmin && !isReviewOwner) {
            throw new AccessDeniedException("Non puoi eliminare recensioni di altri utenti");
        }

        venueReviewRepository.delete(review);
    }

    private VenueReview findEntityById(UUID id) {
        return venueReviewRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Recensione non trovata con ID: " + id));
    }

    private Venue findVenueById(UUID venueId) {
        return venueRepository.findById(venueId)
                .orElseThrow(() -> new EntityNotFoundException("Venue non trovata con ID: " + venueId));
    }

    private void validateRequest(VenueReviewRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request non puo essere null");
        }
        if (request.getVenueId() == null) {
            throw new IllegalArgumentException("Venue ID non puo essere null");
        }
        if (request.getRating() == null || request.getRating() < 1 || request.getRating() > 5) {
            throw new IllegalArgumentException("Rating deve essere tra 1 e 5");
        }
    }

    private VenueReviewResponse toResponse(VenueReview review) {
        return VenueReviewResponse.builder()
                .id(review.getId())
                .rating(review.getRating())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .username(review.getUser().getUsername())
                .venueName(review.getVenue().getName())
                .build();
    }
}
