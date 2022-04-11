package edu.co.sergio.arboleda.smartparkingapi.service;

import java.util.List;

import edu.co.sergio.arboleda.smartparkingapi.rest.api.ClientApi;
import edu.co.sergio.arboleda.smartparkingapi.util.exceptions.GenericException;

public interface ClientService {

	List<ClientApi> findAll();

	ClientApi create(ClientApi clientApi);

	ClientApi findByDocumentNumberOrLicenseCode(String documentNumber, String licenseCode) throws GenericException;

}
