package com.project.localbrew.service;

import com.project.localbrew.entity.Drink;
import com.project.localbrew.exception.DrinkNotFoundException;
import com.project.localbrew.repository.DrinkRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class DrinkServiceImpl implements DrinkService {

    private final DrinkRepository drinkRepository;

    public DrinkServiceImpl(DrinkRepository drinkRepository) {
        this.drinkRepository = drinkRepository;
    }

    // =========================
    // CREATE
    // =========================

    @Override
    public Drink saveDrink(Drink drink) {

        validateDrink(drink);

        if (drink.getId() != null) {
            throw new IllegalArgumentException("Un nuovo drink non deve avere ID");
        }

        return drinkRepository.save(drink);
    }

    // =========================
    // READ
    // =========================

    @Transactional(Transactional.TxType.SUPPORTS)
    @Override
    public List<Drink> findAllDrinks() {
        return drinkRepository.findAll();
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    @Override
    public Drink findDrinkById(UUID id) {

        validateId(id);

        return drinkRepository.findById(id)
                .orElseThrow(() -> new DrinkNotFoundException(id));
    }

    // =========================
    // UPDATE
    // =========================

    @Override
    public Drink updateDrinkById(UUID id, Drink updatedDrink) {

        validateId(id);

        if (updatedDrink == null) {
            throw new IllegalArgumentException("Drink non può essere null");
        }

        Drink existingDrink = drinkRepository.findById(id)
                .orElseThrow(() -> new DrinkNotFoundException(id));

        updateFields(existingDrink, updatedDrink);

        return drinkRepository.save(existingDrink);
    }

    // =========================
    // DELETE
    // =========================

    @Override
    public void deleteDrinkById(UUID id) {

        validateId(id);

        if (!drinkRepository.existsById(id)) {
            throw new DrinkNotFoundException(id);
        }

        drinkRepository.deleteById(id);
    }

    // =========================
    // SEARCH
    // =========================

    @Transactional(Transactional.TxType.SUPPORTS)
    @Override
    public List<Drink> searchDrinksByName(String name) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Il nome non può essere vuoto");
        }

        return drinkRepository.findByNameContainingIgnoreCase(name);
    }

    // =========================
    // PRIVATE METHODS
    // =========================

    private void validateDrink(Drink drink) {

        if (drink == null) {
            throw new IllegalArgumentException("Drink non può essere null");
        }

        if (drink.getName() == null ||
                drink.getName().isBlank()) {

            throw new IllegalArgumentException("Il nome non può essere vuoto");
        }

        if (drink.getCategory() == null) {
            throw new IllegalArgumentException("La categoria non può essere null");
        }

        validateAbv(drink.getAbv());
    }

    private void validateAbv(Double abv) {

        if (abv != null && (abv < 0 || abv > 100)) {

            throw new IllegalArgumentException("ABV deve essere tra 0 e 100");
        }
    }

    private void validateId(UUID id) {

        if (id == null) {
            throw new IllegalArgumentException("ID non può essere null");
        }
    }

    private void updateFields(Drink existingDrink, Drink updatedDrink) {

        if (updatedDrink.getName() != null && !updatedDrink.getName().isBlank()) {
            existingDrink.setName(updatedDrink.getName());
        }

        if (updatedDrink.getDescription() != null && !updatedDrink.getDescription().isBlank()) {
            existingDrink.setDescription(updatedDrink.getDescription());
        }

        if (updatedDrink.getCategory() != null) {
            existingDrink.setCategory(updatedDrink.getCategory());
        }

        if (updatedDrink.getAbv() != null) {
            validateAbv(updatedDrink.getAbv());

            existingDrink.setAbv(updatedDrink.getAbv());
        }

        if (updatedDrink.getOrigin() != null && !updatedDrink.getOrigin().isBlank()) {
            existingDrink.setOrigin(updatedDrink.getOrigin());
        }
    }
}