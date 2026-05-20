package com.project.localbrew.service;

import com.project.localbrew.dto.request.LoginRequest;
import com.project.localbrew.dto.request.RegisterRequest;
import com.project.localbrew.dto.response.AuthResponse;
import com.project.localbrew.dto.response.UserResponse;
import com.project.localbrew.entity.Role;
import com.project.localbrew.entity.User;
import com.project.localbrew.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    // Regex validazione email
    private static final String EMAIL_REGEX =
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$";

    public AuthServiceImpl(
            UserRepository userRepository,
            UserService userService,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    @Override
    public AuthResponse register(RegisterRequest request) {

        String username = request.getUsername();
        String email = request.getEmail();
        String rawPassword = request.getPassword();

        // Validazioni
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username non può essere vuoto");
        }

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email non può essere vuota");
        }

        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("Email non valida");
        }

        if (rawPassword == null || rawPassword.isBlank()) {
            throw new IllegalArgumentException("Password non può essere vuota");
        }

        if (rawPassword.length() < 8) {
            throw new IllegalArgumentException(
                    "La password deve contenere almeno 8 caratteri"
            );
        }

        if (!isUsernameAvailable(username)) {
            throw new IllegalArgumentException("Username già in uso");
        }

        if (!isEmailAvailable(email)) {
            throw new IllegalArgumentException("Email già registrata");
        }

        // Creazione utente
        User newUser = User.builder()
                .username(username)
                .email(email)
                .passwordHash(encodePassword(rawPassword))
                .role(Role.USER)
                .build();

        User savedUser = userService.saveUser(newUser);

        // Mapping User -> UserResponse
        UserResponse userResponse = UserResponse.builder()
                .id(savedUser.getId())
                .username(savedUser.getUsername())
                .email(savedUser.getEmail())
                .role(savedUser.getRole())
                .createdAt(savedUser.getCreatedAt())
                .build();

        // JWT
        String token = jwtService.generateToken(savedUser);

        return AuthResponse.builder()
                .user(userResponse)
                .token(token)
                .message("Registrazione completata con successo")
                .success(true)
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        String email = request.getEmail();
        String rawPassword = request.getPassword();

        // Validazioni
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email non può essere vuota");
        }

        if (rawPassword == null || rawPassword.isBlank()) {
            throw new IllegalArgumentException("Password non può essere vuota");
        }

        // Cerca utente
        Optional<User> optUser = userRepository.findByEmail(email);

        if (optUser.isEmpty()) {
            throw new IllegalArgumentException("Credenziali non valide");
        }

        User user = optUser.get();

        // Verifica password
        if (!isValidPassword(rawPassword, user.getPasswordHash())) {
            throw new IllegalArgumentException("Credenziali non valide");
        }

        // Mapping User -> UserResponse
        UserResponse userResponse = UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .build();

        // JWT
        String token = jwtService.generateToken(user);

        return AuthResponse.builder()
                .user(userResponse)
                .token(token)
                .message("Login effettuato con successo")
                .success(true)
                .build();
    }

    @Override
    public boolean isValidPassword(
            String rawPassword,
            String hashedPassword
    ) {

        if (rawPassword == null || hashedPassword == null) {
            return false;
        }

        return passwordEncoder.matches(rawPassword, hashedPassword);
    }

    @Override
    public String encodePassword(String rawPassword) {

        if (rawPassword == null || rawPassword.isBlank()) {
            throw new IllegalArgumentException(
                    "Password non può essere vuota"
            );
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

        return userRepository.findByUsername(username).isEmpty();
    }

    @Override
    public boolean isEmailAvailable(String email) {

        if (email == null || email.isBlank()) {
            return false;
        }

        return userRepository.findByEmail(email).isEmpty();
    }
}