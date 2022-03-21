package edu.co.sergio.arboleda.smartparkingapi.service;

import edu.co.sergio.arboleda.smartparkingapi.util.exceptions.GenericException;

public interface ParkingService {

	void registerEntry(String licenseCode, String vehicleType) throws GenericException;

	void registerExit(String licenseCode) throws GenericException;

}