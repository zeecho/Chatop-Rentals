package com.openclassrooms.chatoprentals.controller;

import java.text.ParseException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
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
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public LoginController(JWTService jwtService) {
		this.jwtService = jwtService;
	}
	
	@PostMapping("/login")
	public String getToken(Authentication authentication) {
		String token = jwtService.generateToken(authentication);
		return token;
	}
	
	@GetMapping("/me")
	public DBUserDto me() throws ParseException {
		return convertToDto(dbUserService.getCurrentUser());
	}
	
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> registerUser(@RequestBody DBUserDto userDto) {
        DBUser existingUser = dbUserService.getDBUserByEmail(userDto.getEmail());
        if (existingUser != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{}");
        }
        
        DBUser newUser = modelMapper.map(userDto, DBUser.class);
        newUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        
        DBUser savedUser = dbUserService.saveDbUser(newUser);
        if (savedUser != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body("{}");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{}");
        }
    }
	
    private DBUserDto convertToDto(DBUser dbUser) {
        DBUserDto dbUserDto = modelMapper.map(dbUser, DBUserDto.class);
        dbUserDto.setCreatedAt(dbUser.getCreatedAt());
        dbUserDto.setUpdatedAt(dbUser.getUpdatedAt());
        return dbUserDto;
    }
}
