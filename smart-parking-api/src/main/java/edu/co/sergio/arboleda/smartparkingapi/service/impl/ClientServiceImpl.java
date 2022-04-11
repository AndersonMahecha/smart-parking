package edu.co.sergio.arboleda.smartparkingapi.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.co.sergio.arboleda.smartparkingapi.delegate.ClientDelegate;
import edu.co.sergio.arboleda.smartparkingapi.repository.ClientRepository;
import edu.co.sergio.arboleda.smartparkingapi.repository.entity.Client;
import edu.co.sergio.arboleda.smartparkingapi.rest.api.ClientApi;
import edu.co.sergio.arboleda.smartparkingapi.service.ClientService;
import edu.co.sergio.arboleda.smartparkingapi.util.exceptions.GenericException;

@Service
public class ClientServiceImpl implements ClientService {

	private final ModelMapper modelMapper;
	private final ClientRepository clientRepository;
	private final ClientDelegate clientDelegate;

	@Autowired
	public ClientServiceImpl(ModelMapper modelMapper,
							 ClientRepository clientRepository,
							 ClientDelegate clientDelegate) {
		this.modelMapper = modelMapper;
		this.clientRepository = clientRepository;
		this.clientDelegate = clientDelegate;
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
		clientApi.setLicenseCode(clientApi.getLicenseCode().trim());
		Client client = modelMapper.map(clientApi, Client.class);
		return modelMapper.map(clientRepository.save(client), ClientApi.class);
	}

	@Override
	public ClientApi findByDocumentNumberOrLicenseCode(String documentNumber, String licenseCode)
			throws GenericException {
		if (documentNumber != null) {
			return modelMapper.map(clientDelegate.findClientByDocument(documentNumber), ClientApi.class);
		}else if (licenseCode != null){
			return modelMapper.map(clientDelegate.findClientByLicenseCode(licenseCode), ClientApi.class);
		}
		throw new GenericException("Parametros invalidos", "INVALID_PARAMETERS");
	}

}
