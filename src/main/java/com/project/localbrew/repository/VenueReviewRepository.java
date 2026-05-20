package com.project.localbrew.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.localbrew.entity.VenueReview;

@Repository
public interface VenueReviewRepository extends JpaRepository<VenueReview, UUID> {

    List<VenueReview> findByVenueId_Id(UUID venueId);

    List<VenueReview> findByUserId_Id(UUID userId);
}
