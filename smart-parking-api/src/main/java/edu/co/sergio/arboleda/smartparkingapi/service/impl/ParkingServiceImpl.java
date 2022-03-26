package edu.co.sergio.arboleda.smartparkingapi.service.impl;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.co.sergio.arboleda.smartparkingapi.repository.ClientRepository;
import edu.co.sergio.arboleda.smartparkingapi.repository.ParkingRegisterRepository;
import edu.co.sergio.arboleda.smartparkingapi.repository.entity.Client;
import edu.co.sergio.arboleda.smartparkingapi.repository.entity.ParkingRegister;
import edu.co.sergio.arboleda.smartparkingapi.rest.api.ParkingRegisterApi;
import edu.co.sergio.arboleda.smartparkingapi.service.ParkingService;
import edu.co.sergio.arboleda.smartparkingapi.util.exceptions.GenericException;

@Service
public class ParkingServiceImpl implements ParkingService {

	private final ClientRepository clientRepository;
	private final ParkingRegisterRepository parkingRegisterRepository;
	private final Clock clock;
	private final ModelMapper modelMapper;

	@Autowired
	public ParkingServiceImpl(ClientRepository clientRepository,
							  ParkingRegisterRepository parkingRegisterRepository,
							  Clock clock,
							  ModelMapper modelMapper) {
		this.clientRepository = clientRepository;
		this.parkingRegisterRepository = parkingRegisterRepository;
		this.clock = clock;
		this.modelMapper = modelMapper;
	}

	@Override
	public ParkingRegisterApi registerEntry(String licenseCode, String vehicleType) throws GenericException {
		Client client = getClient(licenseCode);

		Optional<ParkingRegister> byClientAndExitIsNull = parkingRegisterRepository.findByClientAndExitIsNull(client);
		if (byClientAndExitIsNull.isPresent()) {
			throw new GenericException("Carnet ya ingresado", "MULTIPLE_VEHICLES_NOT_ALLOWED");
		}

		ParkingRegister parkingRegister = ParkingRegister.newBuilder()
				.withClient(client)
				.withEntry(LocalDateTime.now(clock))
				.build();
		ParkingRegister saved = parkingRegisterRepository.save(parkingRegister);
		return modelMapper.map(saved, ParkingRegisterApi.class);
	}

	@Override
	public ParkingRegisterApi registerExit(String licenseCode) throws GenericException {
		Client client = getClient(licenseCode);

		Optional<ParkingRegister> byClientAndExitIsNull = parkingRegisterRepository.findByClientAndExitIsNull(client);
		if (byClientAndExitIsNull.isEmpty()) {
			throw new GenericException("Ingreso no registrado", "ENTRY_NOT_REGISTERED");
		}

		ParkingRegister parkingRegister = byClientAndExitIsNull.get();
		if (Boolean.FALSE.equals(parkingRegister.getPayed())) {
			throw new GenericException("Debe hacer el pago primero", "PAYMENT_NOT_REGISTERED");
		}

		parkingRegister.setExit(LocalDateTime.now(clock));
		ParkingRegister saved = parkingRegisterRepository.save(parkingRegister);
		return modelMapper.map(saved, ParkingRegisterApi.class);
	}

	private Client getClient(String licenseCode) throws GenericException {
		Optional<Client> byLicenseCode = clientRepository.findByLicenseCode(licenseCode.trim());
		if (byLicenseCode.isEmpty()) {
			throw new GenericException(String.format("Carnet no registrado %s", licenseCode), "LICENSE_NOT_FOUND");
		}
		return byLicenseCode.get();
	}

}
