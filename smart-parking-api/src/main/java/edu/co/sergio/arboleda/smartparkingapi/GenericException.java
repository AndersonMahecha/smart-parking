package edu.co.sergio.arboleda.smartparkingapi;

public class GenericException extends Exception{

	private String errorCode;

	public GenericException(String errorMessage, String errorCode) {
		super(errorMessage);
		this.errorCode = errorCode;
	}

}
