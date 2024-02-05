package com.openclassrooms.chatoprentals.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.openclassrooms.chatoprentals.model.Message;

@Repository
public interface MessageRepository extends CrudRepository<Message, Integer> {

}
