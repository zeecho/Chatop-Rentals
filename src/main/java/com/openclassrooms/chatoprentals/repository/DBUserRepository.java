package com.openclassrooms.chatoprentals.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.openclassrooms.chatoprentals.model.DBUser;

@Repository
public interface DBUserRepository extends JpaRepository<DBUser, Integer> {
	public DBUser findByEmail(String username);

	public DBUser getDBUserById(int id);

	public DBUser findById(int id);
}
