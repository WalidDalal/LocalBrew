package com.project.localbrew.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.project.localbrew.entity.User;
import com.project.localbrew.repository.UserRepository;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findUserById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID non può essere null");
        }
        Optional<User> optUser = userRepository.findById(id);
        return optUser.orElseThrow(() -> new IllegalArgumentException("Utente non trovato con ID: " + id));
    }
    @Override
    public User findByEmail(String email) {

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Utente non trovato"
                        )
                );
    }
    @Transactional
    @Override
    public User saveUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("Utente non può essere null");
        }
        if (user.getUsername() == null || user.getUsername().isBlank()) {
            throw new IllegalArgumentException("Username non può essere vuoto");
        }
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email non può essere vuota");
        }
        if (user.getPasswordHash() == null || user.getPasswordHash().isBlank()) {
            throw new IllegalArgumentException("Password non può essere vuota");
        }
        if (user.getRole() == null) {
            throw new IllegalArgumentException("Role non può essere null");
        }

        // Verifica username unico
        Optional<User> existingUsername = userRepository.findByUsername(user.getUsername());
        if (existingUsername.isPresent()) {
            throw new IllegalArgumentException("Username già esistente: " + user.getUsername());
        }

        // Verifica email unica
        Optional<User> existingEmail = userRepository.findByEmail(user.getEmail());
        if (existingEmail.isPresent()) {
            throw new IllegalArgumentException("Email già esistente: " + user.getEmail());
        }

        return userRepository.save(user);
    }

    @Transactional
    @Override
    public User updateUserById(User user, UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID non può essere null");
        }
        if (user == null) {
            throw new IllegalArgumentException("Utente non può essere null");
        }

        // Verifica se esiste l'utente
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato con ID: " + id));

        // Aggiornamento username
        if (user.getUsername() != null && !user.getUsername().isBlank()) {
            Optional<User> userWithUsername = userRepository.findByUsername(user.getUsername());
            if (userWithUsername.isPresent() && !userWithUsername.get().getId().equals(id)) {
                throw new IllegalArgumentException("Username già esistente: " + user.getUsername());
            }
            existingUser.setUsername(user.getUsername());
        }

        // Aggiornamento email
        if (user.getEmail() != null && !user.getEmail().isBlank()) {
            Optional<User> userWithEmail = userRepository.findByEmail(user.getEmail());
            if (userWithEmail.isPresent() && !userWithEmail.get().getId().equals(id)) {
                throw new IllegalArgumentException("Email già esistente: " + user.getEmail());
            }
            existingUser.setEmail(user.getEmail());
        }

        // Aggiornamento password
        if (user.getPasswordHash() != null && !user.getPasswordHash().isBlank()) {
            existingUser.setPasswordHash(user.getPasswordHash());
        }

        // Aggiornamento role
        if (user.getRole() != null) {
            existingUser.setRole(user.getRole());
        }

        return userRepository.save(existingUser);
    }

    @Transactional
    @Override
    public void deleteUserById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID non può essere null");
        }

        // Verifica se esiste l'utente
        User userToDelete = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato con ID: " + id));

        userRepository.delete(userToDelete);
    }

    @Override
    public User findUserByUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username non può essere vuoto");
        }
        Optional<User> optUser = userRepository.findByUsername(username);
        return optUser.orElseThrow(() -> new IllegalArgumentException("Utente non trovato con username: " + username));
    }

    @Override
    public User findUserByEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email non può essere vuota");
        }
        Optional<User> optUser = userRepository.findByEmail(email);
        return optUser.orElseThrow(() -> new IllegalArgumentException("Utente non trovato con email: " + email));
    }
}
