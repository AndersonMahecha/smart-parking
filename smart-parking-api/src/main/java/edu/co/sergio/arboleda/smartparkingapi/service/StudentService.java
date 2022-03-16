package edu.co.sergio.arboleda.smartparkingapi.service;

import java.util.List;

import edu.co.sergio.arboleda.smartparkingapi.GenericException;
import edu.co.sergio.arboleda.smartparkingapi.rest.api.ClientApi;

public interface StudentService {

	List<ClientApi> findAll();

	ClientApi create(ClientApi clientApi);

	ClientApi findBuDocumentNumberOrLicenseCode(String documentNumber, String licenseCode) throws GenericException;

	ClientApi searchByDocumentNumber(String documentNumber) throws GenericException;

	ClientApi searchByLicense(String licenseCode) throws GenericException;

}
