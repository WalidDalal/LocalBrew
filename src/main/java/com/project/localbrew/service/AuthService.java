package com.project.localbrew.service;

import com.project.localbrew.entity.User;

public interface AuthService {
    
    // Registrazione
    User register(String username, String email, String rawPassword);
    
    // Login
    User login(String email, String rawPassword);
    
    // Validazione password
    boolean isValidPassword(String rawPassword, String hashedPassword);
    
    // Encoding password
    String encodePassword(String rawPassword);
    
    // Validazione email
    boolean isValidEmail(String email);
    
    // Verifica unicità username
    boolean isUsernameAvailable(String username);
    
    // Verifica unicità email
    boolean isEmailAvailable(String email);
}
