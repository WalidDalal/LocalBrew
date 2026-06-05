package com.project.localbrew.repository;
import java.util.List;
import java.util.UUID;

import com.project.localbrew.entity.DrinkCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.localbrew.entity.Drink;

@Repository
public interface DrinkRepository extends JpaRepository<Drink, UUID> {

    List<Drink> findByNameContainingIgnoreCase(String name);

    List<Drink> findByCategoryIn(List<DrinkCategory> categories);
}
