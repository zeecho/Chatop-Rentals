package com.openclassrooms.chatoprentals.controller;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.chatoprentals.dto.MessageDto;
import com.openclassrooms.chatoprentals.model.DBUser;
import com.openclassrooms.chatoprentals.model.Message;
import com.openclassrooms.chatoprentals.model.Rental;
import com.openclassrooms.chatoprentals.service.IDBUserService;
import com.openclassrooms.chatoprentals.service.IMessageService;
import com.openclassrooms.chatoprentals.service.IRentalService;
import com.openclassrooms.chatoprentals.service.JWTService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/messages/**")
@Tag(name = "Messages", description = "Endpoints to handle messages left on rentals")
public class MessageController {
	public JWTService jwtService;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private IDBUserService dbUserService;

	@Autowired
	private IMessageService messageService;

	@Autowired
	private IRentalService rentalService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Add a new message to a rental")
	@SecurityRequirement(name = "bearerAuth")
	public ResponseEntity<Object> createMessage(@RequestBody MessageDto messageDto) throws ParseException {
		String messageString = "";
		HttpStatus status = HttpStatus.CREATED;
		try {
			Message message = convertToEntity(messageDto);
			messageService.saveMessage(message);

			messageString = "Message sent with success!";
			return getResponseEntityWithMessage(messageString, status);
		} catch (Exception e) {
			messageString = "Failed to send message: " + e.getMessage();
			return getResponseEntityWithMessage(messageString, status);
		}
	}

	private ResponseEntity<Object> getResponseEntityWithMessage(String message, HttpStatus status) {
		Map<String, String> response = new HashMap<>();
		response.put("message", message);
		return ResponseEntity.status(status).body(response);
	}


	private Message convertToEntity(MessageDto messageDto) throws ParseException {
		Message message = modelMapper.map(messageDto, Message.class);

		Date date = new Date();
		message.setUpdatedAt(new Timestamp(date.getTime()));

		DBUser dbUser = dbUserService.getCurrentUser();
		message.setDbUser(dbUser);
		Rental rental = rentalService.getRentalById(messageDto.getRentalId());
		message.setRental(rental);

		return message;
	}
}
