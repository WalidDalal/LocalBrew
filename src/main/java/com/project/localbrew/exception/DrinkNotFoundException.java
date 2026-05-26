package com.project.localbrew.exception;

public class DrinkNotFoundException extends RuntimeException {

    public DrinkNotFoundException(String message) {
        super(message);
    }
}