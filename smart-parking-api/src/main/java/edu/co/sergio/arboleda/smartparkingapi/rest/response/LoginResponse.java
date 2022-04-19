package edu.co.sergio.arboleda.smartparkingapi.rest.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import edu.co.sergio.arboleda.smartparkingapi.rest.api.UserApi;

public class LoginResponse {

	@JsonProperty
	private String token;

	private UserApi userApi;

	public LoginResponse(String token, UserApi userApi) {
		this.token = token;
		this.userApi = userApi;
	}

}
