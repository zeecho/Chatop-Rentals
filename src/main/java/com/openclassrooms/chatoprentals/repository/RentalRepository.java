package com.openclassrooms.chatoprentals.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.openclassrooms.chatoprentals.model.Rental;

@Repository
public interface RentalRepository extends CrudRepository<Rental, Integer> {

}
