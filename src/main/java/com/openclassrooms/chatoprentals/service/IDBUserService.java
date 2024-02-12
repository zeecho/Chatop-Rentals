package com.openclassrooms.chatoprentals.service;

import com.openclassrooms.chatoprentals.model.DBUser;

public interface IDBUserService {
	DBUser getCurrentUser();

	DBUser getDBUserById(Integer id);

	DBUser getDBUserByEmail(String email);

	DBUser saveDbUser(DBUser dbUser);
}
