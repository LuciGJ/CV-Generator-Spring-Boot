package com.luci.cvgenerator.dao;

import java.util.List;

import com.luci.cvgenerator.entity.User;

public interface UserDAO {

	public User findUserByUsername(String username);

	public User findUserById(int id);

	public void save(User user);

	public void delete(User user);

	public User findUserByEmail(String email);

	public User findByResetPasswordToken(String token);

	public User findByChangeEmailToken(String token);

	public User findByConfirmationToken(String token);

	public List<User> findAllUsers(int id);

	public List<User> searchUser(String username);

}
