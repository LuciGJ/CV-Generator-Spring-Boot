package com.luci.cvgenerator.dao;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.luci.cvgenerator.entity.Role;

import jakarta.persistence.EntityManager;

@Repository
public class RoleDAOImpl implements RoleDAO {

	@Autowired
	EntityManager entityManager;

	@Override
	public Role findRoleByName(String role) {
		Session session = entityManager.unwrap(Session.class);
		Query<Role> query = session.createQuery("from Role where name = :name", Role.class);
		query.setParameter("name", role);

		Role theRole;

		try {
			theRole = query.getSingleResult();
		} catch (Exception e) {
			theRole = null;
		}

		return theRole;
	}

}
