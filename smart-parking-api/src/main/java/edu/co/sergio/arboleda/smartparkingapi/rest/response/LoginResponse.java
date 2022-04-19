package edu.co.sergio.arboleda.smartparkingapi.rest.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginResponse {

	@JsonProperty
	private String token;

	public LoginResponse(String token) {
		this.token = token;
	}

}
