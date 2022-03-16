package edu.co.sergio.arboleda.smartparkingapi.service.impl;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.co.sergio.arboleda.smartparkingapi.repository.ClientRepository;
import edu.co.sergio.arboleda.smartparkingapi.repository.ParkingRegisterRepository;
import edu.co.sergio.arboleda.smartparkingapi.repository.entity.Client;
import edu.co.sergio.arboleda.smartparkingapi.repository.entity.ParkingRegister;
import edu.co.sergio.arboleda.smartparkingapi.service.ParkingService;
import edu.co.sergio.arboleda.smartparkingapi.util.exceptions.GenericException;

@Service
public class ParkingServiceImpl implements ParkingService {

	private final ClientRepository clientRepository;
	private final ParkingRegisterRepository parkingRegisterRepository;
	private final Clock clock;

	@Autowired
	public ParkingServiceImpl(ClientRepository clientRepository,
							  ParkingRegisterRepository parkingRegisterRepository, Clock clock) {
		this.clientRepository = clientRepository;
		this.parkingRegisterRepository = parkingRegisterRepository;
		this.clock = clock;
	}

	@Override
	public void registerEntry(String licenseCode) throws GenericException {
		Client client = getClient(licenseCode);

		Optional<ParkingRegister> byClientAndExitIsNull = parkingRegisterRepository.findByClientAndExitIsNull(client);
		if (byClientAndExitIsNull.isPresent()) {
			throw new GenericException("Carnet ya ingresado", "MULTIPLE_VEHICLES_NOT_ALLOWED");
		}

		ParkingRegister parkingRegister = ParkingRegister.newBuilder()
				.withClient(client)
				.withEntry(LocalDateTime.now(clock))
				.build();
		parkingRegisterRepository.save(parkingRegister);
	}

	@Override
	public void registerExit(String licenseCode) throws GenericException {
		Client client = getClient(licenseCode);

		Optional<ParkingRegister> byClientAndExitIsNull = parkingRegisterRepository.findByClientAndExitIsNull(client);
		if (byClientAndExitIsNull.isEmpty()) {
			throw new GenericException("Ingreso no registrado", "ENTRY_NOT_REGISTERED");
		}

		ParkingRegister parkingRegister = byClientAndExitIsNull.get();
		if (parkingRegister.getPayed().equals(Boolean.FALSE)) {
			throw new GenericException("Debe hacer el pago primero", "PAYMENT_NOT_REGISTERED");
		}

		parkingRegister.setExit(LocalDateTime.now(clock));
		parkingRegisterRepository.save(parkingRegister);
	}

	private Client getClient(String licenseCode) throws GenericException {
		Optional<Client> byLicenseCode = clientRepository.findByLicenseCode(licenseCode);
		if (byLicenseCode.isEmpty()) {
			throw new GenericException("Carnet no registrado", "LICENSE_NOT_FOUND");
		}
		return byLicenseCode.get();
	}

}
