package edu.co.sergio.arboleda.smartparkingapi.util.exceptions;

public class GenericException extends Exception {

	private final String errorCode;

	public GenericException(String errorMessage, String errorCode) {
		super(errorMessage);
		this.errorCode = errorCode;
	}

	public String getErrorCode() {
		return errorCode;
	}

}
