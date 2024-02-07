package com.openclassrooms.chatoprentals.dto;

import java.util.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class RentalDto {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    
	private int id;
	
	private String name;
	
	private Double surface;
	
	private Double price;
	
	private String picture;
	
	private String description;
	
	private DBUserDto owner;
	
	private Timestamp createdAt = new Timestamp(new Date().getTime());
	
	private Timestamp updatedAt = new Timestamp(new Date().getTime());
	
    public Timestamp getCreatedAtConverted() throws ParseException {
        dateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));
        return new Timestamp(dateFormat.parse(dateFormat.format(this.createdAt)).getTime());
    }

    public void setCreatedAt(Timestamp date) {
        dateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));
        
        this.createdAt = new Timestamp(date.getTime());
    }
    
    public Timestamp getUpdatedAtConverted() throws ParseException {
        dateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));
        return new Timestamp(dateFormat.parse(dateFormat.format(this.updatedAt)).getTime());
    }

    public void setUpdatedAt(Date date) {
        dateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));
        this.updatedAt = new Timestamp(date.getTime());
    }

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getSurface() {
		return surface;
	}

	public void setSurface(Double surface) {
		this.surface = surface;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public DBUserDto getOwner() {
		return owner;
	}

	public void setOwner(DBUserDto owner) {
		this.owner = owner;
	}
}
