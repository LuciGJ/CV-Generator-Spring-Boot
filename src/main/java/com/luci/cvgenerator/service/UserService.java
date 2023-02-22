package com.luci.cvgenerator.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.luci.cvgenerator.entity.User;
import com.luci.cvgenerator.user.CVUser;

import jakarta.servlet.http.HttpServletRequest;

public interface UserService extends UserDetailsService {

	public User findUserByUsername(String username);

	public User findUserById(int id);

	public User findUserByEmail(String email);

	public void saveUser(CVUser user, EmailService emailService, HttpServletRequest request);

	public void updateUser(User user);

	public void updateResetPasswordToken(String token, String email);

	public void updateChangeEmailToken(String token, String email);

	public void updateConfirmationToken(String token, String username);

	public User findByResetPasswordToken(String token);

	public User findByChangeEmailToken(String token);

	public User findByConfirmationToken(String token);

	public void updatePassword(String username, String password);

	public void remove(User user);

	public List<User> findAllUsers(String username);

	public List<User> searchUser(String currentUsername, String username);
}
