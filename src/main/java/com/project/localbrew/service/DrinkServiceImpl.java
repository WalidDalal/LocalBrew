package com.project.localbrew.service;

import com.project.localbrew.entity.Drink;
import com.project.localbrew.repository.DrinkRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DrinkServiceImpl implements DrinkService {

    private final DrinkRepository drinkRepository;

    public DrinkServiceImpl(DrinkRepository drinkRepository) {
        this.drinkRepository = drinkRepository;
    }

    @Override
    public List<Drink> findAllDrinks() {
        return drinkRepository.findAll();
    }

    @Override
    public Drink findDrinkById(UUID id) {

        if (id == null) {
            throw new IllegalArgumentException("ID non può essere null");
        }

        Optional<Drink> optDrink = drinkRepository.findById(id);

        return optDrink.orElseThrow(() ->
                new IllegalArgumentException("Drink non trovato con ID: " + id));
    }

    @Transactional
    @Override
    public Drink saveDrink(Drink drink) {

        if (drink == null) {
            throw new IllegalArgumentException("Drink non può essere null");
        }

        if (drink.getName() == null || drink.getName().isBlank()) {
            throw new IllegalArgumentException("Il nome non può essere vuoto");
        }

        if (drink.getCategory() == null) {
            throw new IllegalArgumentException("La categoria non può essere null");
        }

        if (drink.getAbv() != null &&
                (drink.getAbv() < 0 || drink.getAbv() > 100)) {
            throw new IllegalArgumentException("ABV deve essere tra 0 e 100");
        }

        return drinkRepository.save(drink);
    }

    @Transactional
    @Override
    public Drink updateDrinkById(Drink drink, UUID id) {

        if (id == null) {
            throw new IllegalArgumentException("ID non può essere null");
        }

        if (drink == null) {
            throw new IllegalArgumentException("Drink non può essere null");
        }

        Drink existingDrink = drinkRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Drink non trovato con ID: " + id));

        if (drink.getName() != null && !drink.getName().isBlank()) {
            existingDrink.setName(drink.getName());
        }

        if (drink.getDescription() != null && !drink.getDescription().isBlank()) {
            existingDrink.setDescription(drink.getDescription());
        }

        if (drink.getCategory() != null) {
            existingDrink.setCategory(drink.getCategory());
        }

        if (drink.getAbv() != null) {

            if (drink.getAbv() < 0 || drink.getAbv() > 100) {
                throw new IllegalArgumentException("ABV deve essere tra 0 e 100");
            }

            existingDrink.setAbv(drink.getAbv());
        }

        if (drink.getOrigin() != null && !drink.getOrigin().isBlank()) {
            existingDrink.setOrigin(drink.getOrigin());
        }

        return drinkRepository.save(existingDrink);
    }

    @Override
    public void deleteDrinkById(UUID id) {

        if (id == null) {
            throw new IllegalArgumentException("ID non può essere null");
        }

        Drink drinkToDelete = drinkRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Drink non trovato con ID: " + id));

        drinkRepository.delete(drinkToDelete);
    }

    @Override
    public List<Drink> searchDrinksByName(String name) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Nome non può essere vuoto");
        }
        return drinkRepository.findByNameContainingIgnoreCase(name);
    }
}