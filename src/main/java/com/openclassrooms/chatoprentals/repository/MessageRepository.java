package com.openclassrooms.chatoprentals.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.openclassrooms.chatoprentals.model.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {

}
