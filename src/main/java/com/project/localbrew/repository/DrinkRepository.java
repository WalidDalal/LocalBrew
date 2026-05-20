package com.project.localbrew.repository;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.localbrew.entity.Drink;

@Repository
public interface DrinkRepository extends JpaRepository<Drink, UUID> {

    List<Drink> findByNameContainingIgnoreCase(String name);
}
