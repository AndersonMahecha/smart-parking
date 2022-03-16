package edu.co.sergio.arboleda.smartparkingapi.service;

import edu.co.sergio.arboleda.smartparkingapi.GenericException;

public interface ParkingService {

	void registerEntry(String licenseCode) throws GenericException;

	void registerExit(String licenseCode) throws GenericException;

}
