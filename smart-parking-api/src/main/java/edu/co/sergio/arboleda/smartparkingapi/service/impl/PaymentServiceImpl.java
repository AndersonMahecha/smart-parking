package edu.co.sergio.arboleda.smartparkingapi.service.impl;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.co.sergio.arboleda.smartparkingapi.delegate.ClientDelegate;
import edu.co.sergio.arboleda.smartparkingapi.repository.ParkingRegisterRepository;
import edu.co.sergio.arboleda.smartparkingapi.repository.entity.Client;
import edu.co.sergio.arboleda.smartparkingapi.repository.entity.ParkingRegister;
import edu.co.sergio.arboleda.smartparkingapi.rest.response.PaymentInfoResponse;
import edu.co.sergio.arboleda.smartparkingapi.service.PaymentService;
import edu.co.sergio.arboleda.smartparkingapi.util.exceptions.GenericException;

@Service
public class PaymentServiceImpl implements PaymentService {

	private final ClientDelegate clientDelegate;
	private final ParkingRegisterRepository parkingRegisterRepository;
	private final Clock clock;

	@Autowired
	public PaymentServiceImpl(ClientDelegate clientDelegate,
							  ParkingRegisterRepository parkingRegisterRepository, Clock clock) {
		this.clientDelegate = clientDelegate;
		this.parkingRegisterRepository = parkingRegisterRepository;
		this.clock = clock;
	}

	@Override
	public void pay(String licenseCode) {

	}

	@Override
	public PaymentInfoResponse getPaymentInfo(String licenseCode) throws GenericException {
		Client client = clientDelegate.findClientByLicenseCode(licenseCode);
		ParkingRegister parkingRegister = parkingRegisterRepository.findByClientAndExitIsNull(client).orElseThrow(
				() -> new GenericException("Registros sin pagar no encontrados", "NOT_PAYMENT_REQUIRED_FOUND"));
		LocalDateTime entryDateTime = parkingRegister.getEntry();
		LocalDateTime currentTime = LocalDateTime.now(clock);
		long totalHours = entryDateTime.until(currentTime, ChronoUnit.HOURS);
		return null;
	}

}
