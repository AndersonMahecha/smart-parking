package edu.co.sergio.arboleda.smartparkingapi.delegate.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.co.sergio.arboleda.smartparkingapi.delegate.ClientDelegate;
import edu.co.sergio.arboleda.smartparkingapi.repository.ClientRepository;
import edu.co.sergio.arboleda.smartparkingapi.repository.entity.Client;
import edu.co.sergio.arboleda.smartparkingapi.util.exceptions.GenericException;

@Component
public class ClientDelegateImpl implements ClientDelegate {

	private final ClientRepository clientRepository;

	@Autowired
	public ClientDelegateImpl(ClientRepository clientRepository) {
		this.clientRepository = clientRepository;
	}

	@Override
	public Client findClientByDocument(String documentNumber) throws GenericException {
		Optional<Client> byUserDocumentNumber = clientRepository.findByDocumentNumber(documentNumber);
		if (byUserDocumentNumber.isPresent()) {
			return byUserDocumentNumber.get();
		}
		throw new GenericException(String.format("El numero de documento : %s no fue encontrado", documentNumber),
				"DOCUMENT_NUMBER_NOT_FOUND");
	}

	@Override
	public Client findClientByLicenseCode(String licenseCode) throws GenericException {
		Optional<Client> byLicenseCode = clientRepository.findByLicenseCode(licenseCode);
		if (byLicenseCode.isPresent()) {
			return byLicenseCode.get();
		}
		throw new GenericException(String.format("El carnet %s no se encuentra registrado", licenseCode),
				"LICENSE_NOT_FOUND");
	}

}
