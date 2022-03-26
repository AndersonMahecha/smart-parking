package edu.co.sergio.arboleda.smartparkingapi.service;

import edu.co.sergio.arboleda.smartparkingapi.rest.api.ParkingRegisterApi;
import edu.co.sergio.arboleda.smartparkingapi.util.exceptions.GenericException;

public interface ParkingService {

	ParkingRegisterApi registerEntry(String licenseCode, String vehicleType) throws GenericException;

	ParkingRegisterApi registerExit(String licenseCode) throws GenericException;

}
