package com.openclassrooms.chatoprentals.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.openclassrooms.chatoprentals.model.DBUser;
import com.openclassrooms.chatoprentals.repository.DBUserRepository;

import lombok.Data;

@Data
@Service
public class DBUserService implements IDBUserService {
	@Autowired
	DBUserRepository dbUserRepository;

	public DBUser getDBUser(final int id) {
		return dbUserRepository.findById(id);
	}

	public DBUser getDBUserByEmail(final String username) {
		return dbUserRepository.findByEmail(username);
	}

	public Iterable<DBUser> getDBUsers() {
		return dbUserRepository.findAll();
	}

	public void deleteDBUser(final int id) {
		dbUserRepository.deleteById(id);
	}

	public DBUser saveDbUser(DBUser dbUser) {
		return dbUserRepository.save(dbUser);
	}

	@Override
	public DBUser getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication != null && authentication.isAuthenticated()) {
			return this.getDBUserByEmail(authentication.getName());
		} else {
			return null;
		}
	}

	@Override
	public DBUser getDBUserById(Integer id) {
		return dbUserRepository.getReferenceById(id);
	}
}
