package edu.co.sergio.arboleda.smartparkingapi.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.co.sergio.arboleda.smartparkingapi.repository.ClientRepository;
import edu.co.sergio.arboleda.smartparkingapi.repository.entity.Client;
import edu.co.sergio.arboleda.smartparkingapi.rest.api.ClientApi;
import edu.co.sergio.arboleda.smartparkingapi.service.ClientService;
import edu.co.sergio.arboleda.smartparkingapi.util.exceptions.GenericException;

@Service
public class ClientServiceImpl implements ClientService {

	private final ModelMapper modelMapper;
	private final ClientRepository clientRepository;

	@Autowired
	public ClientServiceImpl(ModelMapper modelMapper,
							 ClientRepository clientRepository) {
		this.modelMapper = modelMapper;
		this.clientRepository = clientRepository;
	}

	@Override
	public List<ClientApi> findAll() {
		return clientRepository.findAll()
				.stream()
				.map(client -> modelMapper.map(client, ClientApi.class))
				.collect(Collectors.toList());
	}

	@Override
	public ClientApi create(ClientApi clientApi) {
		Client client = modelMapper.map(clientApi, Client.class);
		return modelMapper.map(clientRepository.save(client), ClientApi.class);
	}

	@Override
	public ClientApi findBuDocumentNumberOrLicenseCode(String documentNumber, String licenseCode)
			throws GenericException {
		if (documentNumber != null) {
			return searchByDocumentNumber(documentNumber);
		}else if (licenseCode != null){
			return searchByLicense(licenseCode);
		}
		throw new GenericException("Parametros invalidos", "INVALID_PARAMETERS");
	}

	private ClientApi searchByDocumentNumber(String documentNumber) throws GenericException {
		Optional<Client> byUserDocumentNumber = clientRepository.findByUserDocumentNumber(documentNumber);
		if (byUserDocumentNumber.isPresent()) {
			return modelMapper.map(byUserDocumentNumber.get(), ClientApi.class);
		}
		throw new GenericException(String.format("El numero de documento : %s no fue encontrado", documentNumber),
				"DOCUMENT_NUMBER_NOT_FOUND");
	}

	private ClientApi searchByLicense(String licenseCode) throws GenericException {
		Optional<Client> byLicenseCode = clientRepository.findByLicenseCode(licenseCode);
		if (byLicenseCode.isPresent()) {
			return modelMapper.map(byLicenseCode.get(), ClientApi.class);
		}
		throw new GenericException("El carnet no se encuentra registrado", "LICENSE_NOT_FOUND");
	}

}
