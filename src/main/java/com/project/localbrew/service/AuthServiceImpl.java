package com.project.localbrew.service;

import java.util.Optional;

import com.project.localbrew.entity.Role;
import com.project.localbrew.entity.User;
import com.project.localbrew.repository.UserRepository;

import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    
    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    
    // Regex per validazione email
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$";

    public AuthServiceImpl(UserRepository userRepository, UserService userService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public User register(String username, String email, String rawPassword) {
        // Validazioni
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username non può essere vuoto");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email non può essere vuota");
        }
        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("Email non è valida");
        }
        if (rawPassword == null || rawPassword.isBlank()) {
            throw new IllegalArgumentException("Password non può essere vuota");
        }
        if (rawPassword.length() < 8) {
            throw new IllegalArgumentException("Password deve essere almeno 8 caratteri");
        }
        
        // Verifica disponibilità username
        if (!isUsernameAvailable(username)) {
            throw new IllegalArgumentException("Username già in uso");
        }
        
        // Verifica disponibilità email
        if (!isEmailAvailable(email)) {
            throw new IllegalArgumentException("Email già registrata");
        }

        // Creazione nuovo utente
        String hashedPassword = encodePassword(rawPassword);
        User newUser = User.builder()
                .username(username)
                .email(email)
                .passwordHash(hashedPassword)
                .role(Role.USER)  // Ruolo default per nuovi utenti
                .build();

        return userService.saveUser(newUser);
    }

    @Override
    public User login(String email, String rawPassword) {
        // Validazioni
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email non può essere vuota");
        }
        if (rawPassword == null || rawPassword.isBlank()) {
            throw new IllegalArgumentException("Password non può essere vuota");
        }

        // Cerca utente per email
        Optional<User> optUser = userRepository.findByEmail(email);
        if (optUser.isEmpty()) {
            throw new IllegalArgumentException("Credenziali non valide");
        }

        User user = optUser.get();

        // Verifica password
        if (!isValidPassword(rawPassword, user.getPasswordHash())) {
            throw new IllegalArgumentException("Credenziali non valide");
        }

        return user;
    }

    @Override
    public boolean isValidPassword(String rawPassword, String hashedPassword) {
        if (rawPassword == null || hashedPassword == null) {
            return false;
        }
        return passwordEncoder.matches(rawPassword, hashedPassword);
    }

    @Override
    public String encodePassword(String rawPassword) {
        if (rawPassword == null || rawPassword.isBlank()) {
            throw new IllegalArgumentException("Password non può essere vuota");
        }
        return passwordEncoder.encode(rawPassword);
    }

    @Override
    public boolean isValidEmail(String email) {
        if (email == null || email.isBlank()) {
            return false;
        }
        return email.matches(EMAIL_REGEX);
    }

    @Override
    public boolean isUsernameAvailable(String username) {
        if (username == null || username.isBlank()) {
            return false;
        }
        Optional<User> user = userRepository.findByUsername(username);
        return user.isEmpty();
    }

    @Override
    public boolean isEmailAvailable(String email) {
        if (email == null || email.isBlank()) {
            return false;
        }
        Optional<User> user = userRepository.findByEmail(email);
        return user.isEmpty();
    }
}
