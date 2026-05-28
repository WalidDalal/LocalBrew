package com.project.localbrew.repository;

import com.project.localbrew.entity.VenueReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VenueReviewRepository extends JpaRepository<VenueReview, UUID> {

    boolean existsByUser_IdAndVenue_Id(UUID userId, UUID venueId);

    List<VenueReview> findAllByVenue_Id(UUID venueId);

    List<VenueReview> findAllByUser_Id(UUID userId);
}
