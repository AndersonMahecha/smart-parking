package edu.co.sergio.arboleda.smartparkingapi.rest.api;

import java.util.UUID;

public class StudentApi {

	private UUID id;
	private UserApi user;
	private String licenseCode;

	public StudentApi() {
		super();
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public UserApi getUser() {
		return user;
	}

	public void setUser(UserApi user) {
		this.user = user;
	}

	public String getLicenseCode() {
		return licenseCode;
	}

	public void setLicenseCode(String licenseCode) {
		this.licenseCode = licenseCode;
	}

}
