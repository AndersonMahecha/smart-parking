package edu.co.sergio.arboleda.smartparkingapi.delegate;

import edu.co.sergio.arboleda.smartparkingapi.repository.entity.Client;
import edu.co.sergio.arboleda.smartparkingapi.util.exceptions.GenericException;

public interface ClientDelegate {

	Client findClientByDocument(String documentNumber) throws GenericException;

	Client findClientByLicenseCode(String licenseCode) throws GenericException;

}
