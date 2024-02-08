package com.openclassrooms.chatoprentals.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openclassrooms.chatoprentals.model.Message;
import com.openclassrooms.chatoprentals.repository.MessageRepository;

import lombok.Data;

@Data
@Service
public class MessageService implements IMessageService {
	@Autowired
	private MessageRepository messageRepository;
	
	public Optional<Message> getMessage(final int id) {
		return messageRepository.findById(id);
	}
	
	public Iterable<Message> getMessages() {
		return messageRepository.findAll();
	}
	
	public void deleteMessages(final int id) {
		messageRepository.deleteById(id);
	}
	
	public Message saveMessage(Message message) {
		return messageRepository.save(message);
	}
}
