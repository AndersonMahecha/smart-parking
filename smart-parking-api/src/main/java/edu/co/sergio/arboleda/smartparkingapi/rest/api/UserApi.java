package edu.co.sergio.arboleda.smartparkingapi.rest.api;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserApi {

	@JsonProperty
	private UUID id;
	@JsonProperty
	private String username;
	@JsonProperty
	private String password;
	@JsonProperty
	private Boolean enabled;
	@JsonProperty
	private Integer userType;

	public UserApi() {
		super();
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

}
