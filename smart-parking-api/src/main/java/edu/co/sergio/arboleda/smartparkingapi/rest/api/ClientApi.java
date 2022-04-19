package edu.co.sergio.arboleda.smartparkingapi.rest.api;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ClientApi {

	@JsonProperty
	private UUID id;
	@JsonProperty
	private String name;
	@JsonProperty
	private String documentType;
	@JsonProperty
	private String documentNumber;
	@JsonProperty
	private String mobileNumber;
	@JsonProperty
	private String email;
	@JsonProperty
	private String licenseCode;

	public ClientApi() {
		super();
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public String getDocumentNumber() {
		return documentNumber;
	}

	public void setDocumentNumber(String documentNumber) {
		this.documentNumber = documentNumber;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLicenseCode() {
		return licenseCode;
	}

	public void setLicenseCode(String licenseCode) {
		this.licenseCode = licenseCode;
	}

}
