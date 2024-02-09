package com.openclassrooms.chatoprentals.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.openclassrooms.chatoprentals.model.Rental;
import com.openclassrooms.chatoprentals.repository.RentalRepository;

import lombok.Data;

@Data
@Service
public class RentalService implements IRentalService {
	@Autowired
	private RentalRepository rentalRepository;
    
	public Optional<Rental> getRental(final int id) {
		return rentalRepository.findById(id);
	}
	
	public List<Rental> getRentalsList() {
	    return rentalRepository.findAll();
	}
	
	public void deleteRental(final int id) {
		rentalRepository.deleteById(id);
	}

	@Override
	public void updateRental(Rental rental) {
		rentalRepository.save(rental);
	}

	@Override
	public Rental createRental(Rental rental) {
		return rentalRepository.save(rental);
	}

	@Override
	public Rental getRentalById(Integer id) {
		return rentalRepository.getReferenceById(id);
	}
}
