package edu.co.sergio.arboleda.smartparkingapi.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.co.sergio.arboleda.smartparkingapi.delegate.ClientDelegate;
import edu.co.sergio.arboleda.smartparkingapi.delegate.UserDelegate;
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
	private final UserDelegate userDelegate;

	@Autowired
	public ClientServiceImpl(ModelMapper modelMapper,
							 ClientRepository clientRepository,
							 ClientDelegate clientDelegate,
							 UserDelegate userDelegate) {
		this.modelMapper = modelMapper;
		this.clientRepository = clientRepository;
		this.clientDelegate = clientDelegate;
		this.userDelegate = userDelegate;
	}

	@Override
	public List<ClientApi> findAll() {
		return clientRepository.findAll()
				.stream()
				.map(client -> modelMapper.map(client, ClientApi.class))
				.collect(Collectors.toList());
	}

	@Override
	public ClientApi create(ClientApi clientApi) throws GenericException {
		Optional<Client> byEmail = clientRepository.findByEmail(clientApi.getEmail());
		if (byEmail.isPresent()) {
			throw new GenericException("El usuario ya existe", "USERNAME_ALREADY_EXISTS");
		}
		Client client = modelMapper.map(clientApi, Client.class);
		userDelegate.create(client);
		return modelMapper.map(clientRepository.save(client), ClientApi.class);
	}

	@Override
	public ClientApi findByDocumentNumberOrLicenseCode(String documentNumber, String licenseCode)
			throws GenericException {
		if (documentNumber != null) {
			return modelMapper.map(clientDelegate.findClientByDocument(documentNumber), ClientApi.class);
		} else if (licenseCode != null) {
			return modelMapper.map(clientDelegate.findClientByLicenseCode(licenseCode), ClientApi.class);
		}
		throw new GenericException("Parametros invalidos", "INVALID_PARAMETERS");
	}

}
