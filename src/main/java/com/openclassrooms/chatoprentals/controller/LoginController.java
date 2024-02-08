package com.openclassrooms.chatoprentals.controller;

import java.text.ParseException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.chatoprentals.dto.DBUserDto;
import com.openclassrooms.chatoprentals.model.DBUser;
import com.openclassrooms.chatoprentals.service.IDBUserService;
import com.openclassrooms.chatoprentals.service.JWTService;

@RestController
@RequestMapping("/api/auth")
public class LoginController {
	public JWTService jwtService;
	
	@Autowired 
	private ModelMapper modelMapper;
	
	@Autowired
	private IDBUserService dbUserService;
	
	public LoginController(JWTService jwtService) {
		this.jwtService = jwtService;
	}
	
	@PostMapping("/login")
	public String getToken(Authentication authentication) {
		String token = jwtService.generateToken(authentication);
		return token;
	}
	
	@GetMapping("/me")
	public DBUserDto me(Authentication authentication) throws ParseException {
		return convertToDto(dbUserService.getCurrentUser());
	}
	
    private DBUserDto convertToDto(DBUser dbUser) {
        DBUserDto dbUserDto = modelMapper.map(dbUser, DBUserDto.class);
        dbUserDto.setCreatedAt(dbUser.getCreatedAt());
        dbUserDto.setUpdatedAt(dbUser.getUpdatedAt());
        return dbUserDto;
    }
}
