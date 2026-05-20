package com.project.localbrew.exception;

import java.util.UUID;

public class DrinkNotFoundException extends RuntimeException {

    public DrinkNotFoundException(UUID id) {
        super("Drink non trovato con ID: " + id);
    }
}