package com.openclassrooms.chatoprentals.service;

import com.openclassrooms.chatoprentals.model.Message;

public interface IMessageService {
	Message saveMessage(Message dbUser);
}
