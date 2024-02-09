package com.openclassrooms.chatoprentals.service;

import java.util.List;

import com.openclassrooms.chatoprentals.model.Rental;

public interface IRentalService {
	List<Rental> getRentalsList();
	
	void updateRental(Rental rental);
	
	Rental createRental(Rental rental);
	
	Rental getRentalById(Integer id);
}
