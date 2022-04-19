package edu.co.sergio.arboleda.smartparkingapi.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.co.sergio.arboleda.smartparkingapi.delegate.UserDelegate;
import edu.co.sergio.arboleda.smartparkingapi.rest.api.UserApi;
import edu.co.sergio.arboleda.smartparkingapi.rest.request.LoginRequest;
import edu.co.sergio.arboleda.smartparkingapi.rest.response.LoginResponse;
import edu.co.sergio.arboleda.smartparkingapi.util.exceptions.GenericException;

@CrossOrigin(origins = "*", maxAge = 86400)
@RestController
@Validated
@RequestMapping(path = "/user")
public class UserController {

	private final UserDelegate userDelegate;

	@Autowired
	public UserController(UserDelegate userDelegate) {
		this.userDelegate = userDelegate;
	}

	@PostMapping(value = "/authenticate")
	public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest authenticationRequest)
			throws GenericException {
		return ResponseEntity.ok(userDelegate.login(authenticationRequest));
	}

	@PostMapping()
	public ResponseEntity<Void> register(@RequestBody UserApi userApi) throws GenericException {
		userDelegate.create(userApi);
		return ResponseEntity.ok().build();
	}

}