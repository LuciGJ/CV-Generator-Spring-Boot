package com.luci.cvgenerator.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luci.cvgenerator.account.EmailDetails;
import com.luci.cvgenerator.dao.RoleDAO;
import com.luci.cvgenerator.dao.UserDAO;
import com.luci.cvgenerator.entity.Role;
import com.luci.cvgenerator.entity.User;
import com.luci.cvgenerator.entity.UserDetail;
import com.luci.cvgenerator.user.CVUser;
import com.luci.cvgenerator.utility.LinkUtility;
import com.luci.cvgenerator.utility.RandomStringBuilder;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private RoleDAO roleDAO;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Override
	@Transactional
	public User findUserByUsername(String username) {
		return userDAO.findUserByUsername(username);
	}

	@Override
	@Transactional
	public void saveUser(CVUser user, EmailService emailService, HttpServletRequest request) {
		User newUser = new User();
		newUser.setUsername(user.getUsername());
		newUser.setPassword(passwordEncoder.encode(user.getPassword()));
		newUser.setEmail(user.getEmail());
		UserDetail userDetail = new UserDetail();
		userDetail.setEmail(user.getEmail());
		userDetail.setImage("default.png");
		newUser.setUserDetail(userDetail);
		newUser.setEnabled(0);
		newUser.setSuspended(0);

		newUser.setRoles(Arrays.asList(roleDAO.findRoleByName("ROLE_USER")));

		String token;
		do {
			token = RandomStringBuilder.buildRandomString(30);
		} while (findByConfirmationToken(token) != null);
		newUser.setConfirmationToken(token);
		String confirmationLink = LinkUtility.getSiteURL(request) + "/confirmAccount?token=" + token;
		EmailDetails details = new EmailDetails();

		details.setRecipient(user.getEmail());
		details.setMsgBody("Click the following link to confirm your account: \n " + confirmationLink);
		details.setSubject("CV Generator account confirmation");
		emailService.sendEmail(details);

		userDAO.save(newUser);

	}

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userDAO.findUserByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("Invalid username or password");
		}

		if (user.getEnabled() == 0) {
			throw new DisabledException("Account not activated");
		}

		if (user.getSuspended() == 1) {
			throw new LockedException("Account suspended");
		}

		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
				mapRolesToAuthorities(user.getRoles()));
	}

	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
	}

	@Override
	@Transactional
	public void updateUser(User user) {
		userDAO.save(user);
	}

	@Override
	@Transactional
	public User findUserByEmail(String email) {
		return userDAO.findUserByEmail(email);
	}

	@Transactional
	@Override
	public void updateResetPasswordToken(String token, String email) {
		User user = findUserByEmail(email);
		user.setPasswordToken(token);
		updateUser(user);
	}

	@Transactional
	@Override
	public void updateChangeEmailToken(String token, String email) {
		User user = findUserByEmail(email);
		user.setEmailToken(token);
		updateUser(user);

	}

	@Transactional
	@Override
	public void updateConfirmationToken(String token, String username) {
		User user = findUserByEmail(username);
		user.setConfirmationToken(token);
		updateUser(user);

	}

	@Override
	public User findByResetPasswordToken(String token) {
		return userDAO.findByResetPasswordToken(token);
	}

	@Override
	public User findByChangeEmailToken(String token) {
		return userDAO.findByChangeEmailToken(token);
	}

	@Override
	public User findByConfirmationToken(String token) {
		return userDAO.findByConfirmationToken(token);
	}

	@Transactional
	@Override
	public void updatePassword(String username, String password) {
		User user = findUserByUsername(username);
		String encodedPassword = passwordEncoder.encode(password);
		user.setPassword(encodedPassword);

		user.setPasswordToken(null);
		updateUser(user);

	}

	@Transactional
	@Override
	public void remove(User user) {
		userDAO.delete(user);

	}

	@Transactional
	@Override
	public List<User> findAllUsers(String username) {
		return userDAO.findAllUsers(findUserByUsername(username).getId());
	}

	@Transactional
	@Override
	public User findUserById(int id) {
		return userDAO.findUserById(id);
	}

	@Override
	public List<User> searchUser(String currentUsername, String username) {

		List<User> results = null;

		if (username != null && (username.trim().length() > 0)) {
			results = userDAO.searchUser(username);
		} else {
			results = findAllUsers(currentUsername);
		}

		return results;
	}

}
