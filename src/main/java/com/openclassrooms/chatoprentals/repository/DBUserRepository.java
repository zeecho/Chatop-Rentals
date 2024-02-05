package com.openclassrooms.chatoprentals.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.openclassrooms.chatoprentals.model.DBUser;

@Repository
public interface DBUserRepository extends CrudRepository<DBUser, Integer> {
	public DBUser findByEmail(String username);
}
