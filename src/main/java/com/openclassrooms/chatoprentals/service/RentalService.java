package com.openclassrooms.chatoprentals.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openclassrooms.chatoprentals.model.Rental;
import com.openclassrooms.chatoprentals.repository.RentalRepository;

import lombok.Data;

@Data
@Service
public class RentalService {
	@Autowired
	private RentalRepository rentalRepository;
	
	public Optional<Rental> getRental(final int id) {
		return rentalRepository.findById(id);
	}
	
	public Iterable<Rental> getRentals() {
		return rentalRepository.findAll();
	}
	
	public void deleteRental(final int id) {
		rentalRepository.deleteById(id);
	}
	
	public Rental saveRental(Rental rental) {
		return rentalRepository.save(rental);
	}
}
