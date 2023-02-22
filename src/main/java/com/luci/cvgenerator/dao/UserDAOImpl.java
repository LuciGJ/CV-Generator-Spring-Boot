package com.luci.cvgenerator.dao;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.luci.cvgenerator.entity.User;
import jakarta.persistence.EntityManager;

@Repository
public class UserDAOImpl implements UserDAO {

	@Autowired
	EntityManager entityManager;

	@Override
	public User findUserByUsername(String username) {
		Session session = entityManager.unwrap(Session.class);
		Query<User> query = session.createQuery("from User where username = :username", User.class);
		query.setParameter("username", username);
		User user;
		try {
			user = query.getSingleResult();
		} catch (Exception e) {
			user = null;
		}
		return user;
	}

	@Override
	public void save(User user) {
		Session session = entityManager.unwrap(Session.class);

		session.merge(user);

	}

	@Override
	public User findUserByEmail(String email) {
		Session session = entityManager.unwrap(Session.class);
		Query<User> query = session.createQuery("from User where email = :email", User.class);
		query.setParameter("email", email);
		User user;
		try {
			user = query.getSingleResult();
		} catch (Exception e) {
			user = null;
		}

		return user;
	}

	@Override
	public User findByResetPasswordToken(String token) {
		Session session = entityManager.unwrap(Session.class);
		Query<User> query = session.createQuery("from User where passwordToken = :token", User.class);
		query.setParameter("token", token);
		User user;
		try {
			user = query.getSingleResult();
		} catch (Exception e) {
			user = null;
		}

		return user;
	}

	@Override
	public User findByChangeEmailToken(String token) {
		Session session = entityManager.unwrap(Session.class);
		Query<User> query = session.createQuery("from User where emailToken = :token", User.class);
		query.setParameter("token", token);
		User user;
		try {
			user = query.getSingleResult();
		} catch (Exception e) {
			user = null;
		}

		return user;
	}

	@Override
	public User findByConfirmationToken(String token) {
		Session session = entityManager.unwrap(Session.class);
		Query<User> query = session.createQuery("from User where confirmationToken = :token", User.class);
		query.setParameter("token", token);
		User user;
		try {
			user = query.getSingleResult();
		} catch (Exception e) {
			user = null;
		}

		return user;
	}

	@Override
	public void delete(User user) {
		String picturesDir = "user-photos/";
		String cvsDir = "user-cvs/";

		try {
			Path deleteFile;
			deleteFile = Paths.get(picturesDir + user.getId() + ".png");
			Files.deleteIfExists(deleteFile);
			deleteFile = Paths.get(picturesDir + user.getId() + ".jpg");
			Files.deleteIfExists(deleteFile);
			deleteFile = Paths.get(cvsDir + user.getId() + ".pdf");
			Files.deleteIfExists(deleteFile);
		} catch (IOException e) {
			e.printStackTrace();
		}

		Session session = entityManager.unwrap(Session.class);

		session.remove(user);

	}

	@Override
	public List<User> findAllUsers(int id) {
		Session session = entityManager.unwrap(Session.class);
		Query<User> query = session.createQuery("from User where id != :id", User.class);
		query.setParameter("id", id);
		List<User> users;
		try {
			users = query.getResultList();
		} catch (Exception e) {
			users = new ArrayList<>();
		}
		return users;
	}

	@Override
	public User findUserById(int id) {
		Session session = entityManager.unwrap(Session.class);
		Query<User> query = session.createQuery("from User where id = :id", User.class);
		query.setParameter("id", id);
		User user;
		try {
			user = query.getSingleResult();
		} catch (Exception e) {
			user = null;
		}
		return user;
	}

	@Override
	public List<User> searchUser(String username) {
		username = "%" + username.toLowerCase() + "%";
		Session session = entityManager.unwrap(Session.class);
		Query<User> query = session.createQuery("from User where lower(username) like :username", User.class);
		query.setParameter("username", username);
		List<User> users;
		try {
			users = query.getResultList();
		} catch (Exception e) {
			users = new ArrayList<>();
		}
		return users;
	}

}
