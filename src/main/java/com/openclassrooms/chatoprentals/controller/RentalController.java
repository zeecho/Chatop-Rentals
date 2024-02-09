package com.openclassrooms.chatoprentals.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.openclassrooms.chatoprentals.dto.RentalDto;
import com.openclassrooms.chatoprentals.dto.RentalShowDto;
import com.openclassrooms.chatoprentals.model.DBUser;
import com.openclassrooms.chatoprentals.model.Rental;
import com.openclassrooms.chatoprentals.service.IDBUserService;
import com.openclassrooms.chatoprentals.service.IRentalService;

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {
	@Autowired
	private IRentalService rentalService;
	
	@Autowired
	private IDBUserService dbUserService;
	
	@Autowired 
	private ModelMapper modelMapper;
	
	Logger logger = LoggerFactory.getLogger(RentalController.class);
	
	@GetMapping
	public ResponseEntity<Map<String, List<RentalShowDto>>> getRentals() {
		List<Rental> rentals = rentalService.getRentalsList();
		List<RentalShowDto> rentalDtos = rentals.stream()
				.map(this::convertToRentalShowDto)
				.collect(Collectors.toList());
	    Map<String, List<RentalShowDto>> response = new HashMap<>();
	    response.put("rentals", rentalDtos);
	    return ResponseEntity.ok(response);
	}
	
	@GetMapping(value = "/{id}")
	public RentalShowDto getRental(@PathVariable("id") final int id) {
		RentalShowDto rentalShowDto = convertToRentalShowDto(rentalService.getRentalById(id));

		return rentalShowDto;
	}
	
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Object> createRental(RentalDto rentalDto) throws ParseException {
        String messageString = "";
        HttpStatus status = HttpStatus.CREATED;
    	try {
	        Rental rental = convertToEntity(rentalDto);
	        rentalService.createRental(rental);

	        messageString = "Rental created !";
	        return getResponseEntityWithMessage(messageString, status);
        } catch (Exception e) {
        	messageString = "Failed to create rental: " + e.getMessage();
        	return getResponseEntityWithMessage(messageString, status);
		}
    }
    
    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> updateRental(@PathVariable("id") int id, RentalDto rentalDto) throws ParseException {
        String messageString = "";
        HttpStatus status = HttpStatus.OK;
    	try {
	    	Rental rental = rentalService.getRentalById(id);
	        DBUser currentUser = dbUserService.getCurrentUser();
	        
	        if (rental == null) {
	            throw new EntityNotFoundException("Rental not found");
	        }
	        
	        if (!Objects.equals(rental.getOwner().getId(), currentUser.getId())) {
	            throw new ParseException("You are not authorized to update this rental", 401);
	        }
	        
	    	if(!Objects.equals(id, rentalDto.getId())){
	            throw new IllegalArgumentException("IDs don't match");
	        }
	        rental = convertToEntity(rentalDto);
	        rentalService.updateRental(rental);
	        
	        messageString = "Rental updated !";
	        return getResponseEntityWithMessage(messageString, status);
        } catch (Exception e) {
            messageString = "Failed to update rental: " + e.getMessage();
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            return getResponseEntityWithMessage(messageString, status);
		}
    }
    
    private ResponseEntity<Object> getResponseEntityWithMessage(String message, HttpStatus status) {
    	Map<String, String> response = new HashMap<>();
        response.put("message", message);
        return ResponseEntity.status(status).body(response);
	}
//	
//    private RentalDto convertToDto(Rental rental) {
//        RentalDto rentalDto = modelMapper.map(rental, RentalDto.class);
//        rentalDto.setCreatedAt(rental.getCreatedAt());
//        rentalDto.setUpdatedAt(rental.getUpdatedAt());
//        
//        return rentalDto;
//    }
    
    private RentalShowDto convertToRentalShowDto(Rental rental) {
        RentalShowDto rentalShowDto = modelMapper.map(rental, RentalShowDto.class);
        rentalShowDto.setCreatedAt(rental.getCreatedAt());
        rentalShowDto.setUpdatedAt(rental.getUpdatedAt());
        if (StringUtils.hasText(rental.getPicture())) {
            rentalShowDto.setPicture("/assets/" + rental.getPicture());
        }

        return rentalShowDto;
    }
    
    private Rental convertToEntity(RentalDto rentalDto) throws ParseException, IOException {
        Rental rental = modelMapper.map(rentalDto, Rental.class);

        Date date = new Date();
        rental.setUpdatedAt(new Timestamp(date.getTime()));
        DBUser dbUser = dbUserService.getCurrentUser();
        rental.setOwner(dbUser);
        
        String filename = handleFileUpload(rentalDto);
        rental.setPicture(filename);
      
        if (rentalDto.getId() != null && rentalDto.getId() != 0) {
        	rental.setId(rentalDto.getId());
            Rental oldRental = rentalService.getRentalById(rentalDto.getId());
            rental.setCreatedAt(oldRental.getCreatedAt());
            rental.setPicture(rentalDto.getPicture().getOriginalFilename());
        }
        return rental;
    }
    
    private String handleFileUpload(RentalDto rentalDto) throws IOException {
        String fileName = getFileName(rentalDto);
        byte[] bytes = rentalDto.getPicture().getBytes();
        Path path = Paths.get("../P3-Full-Stack-portail-locataire/src/assets/" + fileName);
        Files.write(path, bytes);
        
        return fileName;
	}
    
    private String getFileName(RentalDto rentalDto) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-HHmmss");
        String timestamp = dateFormat.format(new Date());
        String originalFilename = rentalDto.getPicture().getOriginalFilename();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf('.'));

        return timestamp + fileExtension;
	}
}
