package com.openclassrooms.chatoprentals.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.chatoprentals.model.Rental;
import com.openclassrooms.chatoprentals.service.RentalService;

@RestController
public class RentalController {
	@Autowired
	private RentalService rentalService;
	
	@GetMapping("/api/rentals/{id}")
	public Rental getRental(@PathVariable("id") final int id) {
		Optional<Rental> rental = rentalService.getRental(id);
		if(rental.isPresent()) {
			return rental.get();
		} else {
			return null;
		}
	}
	
	@GetMapping("/api/rentals")
	public Iterable<Rental> getRentals() {
		return rentalService.getRentals();
	}
	
	// TODO
	@PostMapping("/api/rentals")
	public Rental createRental(@RequestBody Rental rental) {
		return rentalService.saveRental(rental);
	}
}
