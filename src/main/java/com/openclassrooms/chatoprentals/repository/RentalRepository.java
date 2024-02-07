package com.openclassrooms.chatoprentals.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.openclassrooms.chatoprentals.model.DBUser;
import com.openclassrooms.chatoprentals.model.Rental;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Integer> {
	Page<Rental> findByOwner_Id(Integer ownerId, Pageable pageReq);
	
    default Page<Rental> findByOwner_Id(DBUser user, Pageable pageReq) {
        Page<Rental> rentals = findByOwner_Id(user.getId(), pageReq);

        return rentals;
    }
}
