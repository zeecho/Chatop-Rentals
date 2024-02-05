package com.openclassrooms.chatoprentals.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openclassrooms.chatoprentals.model.DBUser;
import com.openclassrooms.chatoprentals.repository.DBUserRepository;

import lombok.Data;

@Data
@Service
public class DBUserService {
	@Autowired
	DBUserRepository dbUserRepository;

	public Optional<DBUser> getDBUser(final int id) {
		return dbUserRepository.findById(id);
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
}
