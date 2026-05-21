package com.project.localbrew.repository;

import com.project.localbrew.entity.VenueDrink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VenueDrinkRepository extends JpaRepository<VenueDrink, UUID> {

    List<VenueDrink> findByVenueId(UUID venueId);
}