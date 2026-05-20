package com.project.localbrew.service;

import java.util.List;
import java.util.UUID;

import com.project.localbrew.entity.User;

public interface UserService {
	// CRUD
	List<User> findAllUsers();
	User findUserById(UUID id);
	User saveUser(User user);
	User updateUserById(User user, UUID id);
	void deleteUserById(UUID id);
	
	// Utility methods
	User findUserByUsername(String username);
	User findUserByEmail(String email);
}
