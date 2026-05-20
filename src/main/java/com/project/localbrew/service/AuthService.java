package com.project.localbrew.service;

import com.project.localbrew.dto.request.LoginRequest;
import com.project.localbrew.dto.request.RegisterRequest;
import com.project.localbrew.dto.response.AuthResponse;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    boolean isValidPassword(
            String rawPassword,
            String hashedPassword
    );

    String encodePassword(String rawPassword);

    boolean isValidEmail(String email);

    boolean isUsernameAvailable(String username);

    boolean isEmailAvailable(String email);
}