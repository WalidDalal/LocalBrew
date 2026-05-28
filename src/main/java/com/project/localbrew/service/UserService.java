package com.project.localbrew.service;

import com.project.localbrew.dto.request.UserUpdateRequest;
import com.project.localbrew.dto.response.UserResponse;
import com.project.localbrew.entity.User;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

public interface UserService {

	// CRUD
	List<User> findAllUsers();

	User findById(UUID id);

	User findByEmail(String email);

	User findByUsername(String username);

	User saveUser(User user);

	User updateUser(UUID id, UserUpdateRequest request);

	void deleteUserById(UUID id);

	UserResponse getCurrentUser(String email);

	UserResponse toResponse(User user);
}
