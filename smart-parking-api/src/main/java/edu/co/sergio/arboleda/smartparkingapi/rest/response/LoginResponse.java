package edu.co.sergio.arboleda.smartparkingapi.rest.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import edu.co.sergio.arboleda.smartparkingapi.rest.api.UserApi;

public class LoginResponse {

	@JsonProperty
	private String token;

	@JsonProperty
	private UserApi user;

	public LoginResponse(String token, UserApi user) {
		this.token = token;
		this.user = user;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public UserApi getUser() {
		return user;
	}

	public void setUser(UserApi user) {
		this.user = user;
	}

}
