package com.openclassrooms.chatoprentals.controller;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.chatoprentals.dto.RentalDto;
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
	
	@GetMapping
	public List<RentalDto> getRentals() {
//		List<Rental> rentals = rentalService.getRentalsList(page, size, sortDir, sort);
		List<Rental> rentals = rentalService.getRentalsList(0, 10, "ASC", "id");
		return rentals.stream()
				.map(this::convertToDto)
				.collect(Collectors.toList());
	}
	
	@GetMapping(value = "/{id}")
	public RentalDto getRental(@PathVariable("id") final int id) {
		return convertToDto(rentalService.getRentalById(id));
	}
	
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
	public RentalDto createRental(RentalDto rentalDto) throws ParseException {
        Rental rental = convertToEntity(rentalDto);
        Rental rentalCreated = rentalService.createRental(rental);
        return convertToDto(rentalCreated);
    }
    
    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updateRental(@PathVariable("id") int id, RentalDto rentalDto) throws ParseException {
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
    }
	
    private RentalDto convertToDto(Rental rental) {
        RentalDto rentalDto = modelMapper.map(rental, RentalDto.class);
        rentalDto.setCreatedAt(rental.getCreatedAt());
        rentalDto.setUpdatedAt(rental.getUpdatedAt());
        return rentalDto;
    }
    
    private Rental convertToEntity(RentalDto rentalDto) throws ParseException {
        Rental rental = modelMapper.map(rentalDto, Rental.class);

        Date date = new Date();
        rental.setUpdatedAt(new Timestamp(date.getTime()));
        DBUser dbUser = dbUserService.getCurrentUser();
        rental.setOwner(dbUser);
      
        if (rentalDto.getId() != null && rentalDto.getId() != 0) {
        	rental.setId(rentalDto.getId());
            Rental oldRental = rentalService.getRentalById(rentalDto.getId());
            rental.setCreatedAt(oldRental.getCreatedAt());
        }
        return rental;
    }
}
