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

    List<Venue> findAllByCity(String city);

    List<Venue> findAllByType(VenueType venueType);

    List<Venue> findAllByName(String name);
}
