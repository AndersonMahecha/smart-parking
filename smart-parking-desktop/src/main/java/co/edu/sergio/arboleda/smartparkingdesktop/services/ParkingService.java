package co.edu.sergio.arboleda.smartparkingdesktop.services;

import co.edu.sergio.arboleda.smartparkingdesktop.exceptions.GenericException;
import co.edu.sergio.arboleda.smartparkingdesktop.pojo.ParkingRegisterApi;

public interface ParkingService {

	ParkingRegisterApi registerEntry(String licenseCode, String token) throws GenericException;

}
