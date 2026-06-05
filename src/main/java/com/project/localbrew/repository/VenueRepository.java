package com.project.localbrew.repository;

import com.project.localbrew.entity.Venue;
import com.project.localbrew.entity.VenueStatus;
import com.project.localbrew.entity.VenueType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VenueRepository extends JpaRepository<Venue, UUID> {

    List<Venue> findAllByStatus(VenueStatus venueStatus);

    List<Venue> findAllByTypeInAndStatus(List<VenueType> types, VenueStatus status);

    List<Venue> findAllByNameContainingIgnoreCaseAndStatus(String name, VenueStatus status);

    List<Venue> findAllByCityContainingIgnoreCaseAndStatus(String city, VenueStatus status);
    
    List<Venue> findAllByOwnerId(UUID ownerId);
}
