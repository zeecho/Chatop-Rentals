package com.openclassrooms.chatoprentals.controller;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.chatoprentals.dto.DBUserDto;
import com.openclassrooms.chatoprentals.dto.LoginDto;
import com.openclassrooms.chatoprentals.dto.RegisterDto;
import com.openclassrooms.chatoprentals.model.DBUser;
import com.openclassrooms.chatoprentals.service.IDBUserService;
import com.openclassrooms.chatoprentals.service.JWTService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Account", description = "Account management (from a user point of view)")
public class LoginController {
	public JWTService jwtService;

	@Autowired 
	private ModelMapper modelMapper;

	@Autowired
	private IDBUserService dbUserService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private AuthenticationManager authenticationManager;

	Logger logger = LoggerFactory.getLogger(LoginController.class);

	public LoginController(JWTService jwtService) {
		this.jwtService = jwtService;
	}

	@PostMapping("/login")
	@Operation(summary = "Logs user in")
	public ResponseEntity<Object> login(@RequestBody LoginDto loginDto) {
		logger.info("login attempt with email: {}", loginDto.getEmail());

		String token = getToken(loginDto); 
		return getTokenResponseEntity(token);
	}

	private String getToken(LoginDto loginDto) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						loginDto.getEmail(), 
						loginDto.getPassword()
						)
				);

		return jwtService.generateToken(authentication);
	}

	private ResponseEntity<Object> getTokenResponseEntity(String token) {
		Map<String, String> response = new HashMap<>();
		response.put("token", token);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/me")
	@Operation(summary = "Get informations about currently logged in user", description = "Returns a token")
    @SecurityRequirement(name = "bearerAuth")
	public DBUserDto me() throws ParseException {
		return convertToDto(dbUserService.getCurrentUser());
	}

	@PostMapping("/register")
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Create a new user")
	public ResponseEntity<Object> registerUser(@RequestBody RegisterDto registerDto) {
		//    	try {
		DBUser existingUser = dbUserService.getDBUserByEmail(registerDto.getEmail());
		if (existingUser != null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User already exists");
		}

		DBUser savedUser = dbUserService.saveDbUser(createDBUserFromRegisterDto(registerDto));
		if (savedUser != null) {
			// if the user is created successfully, we log them in
			LoginDto loginDto = new LoginDto();
			loginDto.setEmail(registerDto.getEmail());
			loginDto.setPassword(registerDto.getPassword());
			String token = getToken(loginDto); 
			return getTokenResponseEntity(token);
			//        	login(email, password);
			//            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
			//        } else {
			//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to register user");
		}
		//    	} catch (Exception e) {
		//    		logger.error("error while trying to register a new user: " + e.getMessage());
		////            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to register user");
		//		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to register user");
	}

	private DBUserDto convertToDto(DBUser dbUser) {
		DBUserDto dbUserDto = modelMapper.map(dbUser, DBUserDto.class);
		dbUserDto.setCreatedAt(dbUser.getCreatedAt());
		dbUserDto.setUpdatedAt(dbUser.getUpdatedAt());
		return dbUserDto;
	}

	private DBUser createDBUserFromRegisterDto(RegisterDto registerDto) {
		DBUser newUser = modelMapper.map(registerDto, DBUser.class);
		newUser.setPassword(passwordEncoder.encode(registerDto.getPassword()));
		Date date = new Date();
		newUser.setCreatedAt(new Timestamp(date.getTime()));
		newUser.setUpdatedAt(new Timestamp(date.getTime()));

		return newUser;
	}
}
