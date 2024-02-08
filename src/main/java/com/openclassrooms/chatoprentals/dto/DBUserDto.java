package com.openclassrooms.chatoprentals.dto;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DBUserDto {
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	private int id;
	
	private String email;
	
    private String name;
    
    private String password;
    
    @JsonProperty("created_at")
	private Timestamp createdAt = new Timestamp(new Date().getTime());
	
    @JsonProperty("updated_at")
	private Timestamp updatedAt = new Timestamp(new Date().getTime());
	
    public Timestamp getCreatedAt() throws ParseException {
        dateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));
        return new Timestamp(dateFormat.parse(dateFormat.format(this.createdAt)).getTime());
    }

    public void setCreatedAt(Timestamp date) {
        dateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));
        
        this.createdAt = new Timestamp(date.getTime());
    }
    
    public Timestamp getUpdatedAt() throws ParseException {
        dateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));
        return new Timestamp(dateFormat.parse(dateFormat.format(this.updatedAt)).getTime());
    }

    public void setUpdatedAt(Date date) {
        dateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));
        this.updatedAt = new Timestamp(date.getTime());
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
