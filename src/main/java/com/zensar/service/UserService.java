package com.zensar.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.zensar.model.User;

public interface UserService extends UserDetailsService {
	public User findByEmail(String email);

	public User findByUsername(String username);

	public User saveUser(User user);

	public User updateUser(User user);

	public boolean checkUserExists(String username, String email);

	public boolean checkUsernameExists(String username);

	public boolean checkEmailExists(String email);

	public User findById(int id);

}
