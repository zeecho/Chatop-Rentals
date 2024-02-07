package com.openclassrooms.chatoprentals.model;

import java.sql.Timestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Entity
@Table(name = "MESSAGES")
@Getter @Setter
public class Message {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne(
			cascade = CascadeType.ALL
			)
	@JoinColumn(name = "rental_id")
	private Rental rental;
	
	@ManyToOne(
			cascade = CascadeType.ALL
			)
	@JoinColumn(name="user_id")
	private DBUser dbUser;
	
	public DBUser getDbUser() {
		return dbUser;
	}

	public void setDbUser(DBUser dbUser) {
		this.dbUser = dbUser;
	}

	public Rental getRental() {
		return rental;
	}

	public void setRental(Rental rental) {
		this.rental = rental;
	}

	private String message;
	
	@Column(name = "created_at")
	private Timestamp createdAt;
	
	@Column(name = "updated_at")
	private Timestamp updatedAt;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
