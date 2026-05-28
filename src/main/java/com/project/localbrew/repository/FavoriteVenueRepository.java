package com.project.localbrew.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.localbrew.entity.FavoriteVenue;

@Repository
public interface FavoriteVenueRepository extends JpaRepository<FavoriteVenue, UUID> {
    boolean existsByUser_idAndVenue_id(UUID userId, UUID venueId);
    List<FavoriteVenue> findAllByUser_id(UUID userId);
    Optional<FavoriteVenue> findByUser_idAndVenue_id(UUID userId, UUID venueId);
}
