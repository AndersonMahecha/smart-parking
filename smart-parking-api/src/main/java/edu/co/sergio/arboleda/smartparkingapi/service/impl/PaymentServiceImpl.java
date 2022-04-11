package edu.co.sergio.arboleda.smartparkingapi.service.impl;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import edu.co.sergio.arboleda.smartparkingapi.delegate.ClientDelegate;
import edu.co.sergio.arboleda.smartparkingapi.repository.ParkingRegisterRepository;
import edu.co.sergio.arboleda.smartparkingapi.repository.entity.Client;
import edu.co.sergio.arboleda.smartparkingapi.repository.entity.ParkingRegister;
import edu.co.sergio.arboleda.smartparkingapi.rest.api.UserApi;
import edu.co.sergio.arboleda.smartparkingapi.rest.response.PaymentInfoResponse;
import edu.co.sergio.arboleda.smartparkingapi.service.PaymentService;
import edu.co.sergio.arboleda.smartparkingapi.util.exceptions.GenericException;

@Service
public class PaymentServiceImpl implements PaymentService {

	private final ClientDelegate clientDelegate;
	private final ParkingRegisterRepository parkingRegisterRepository;
	private final ModelMapper modelMapper;
	private final Clock clock;

	@Value("${payment-periods-in-minutes:1}")
	private float paymentPeriodsInMinutes;

	@Value("${price-per-period}")
	private float pricePerPeriod;

	@Autowired
	public PaymentServiceImpl(ClientDelegate clientDelegate,
							  ParkingRegisterRepository parkingRegisterRepository,
							  ModelMapper modelMapper,
							  Clock clock) {
		this.clientDelegate = clientDelegate;
		this.parkingRegisterRepository = parkingRegisterRepository;
		this.modelMapper = modelMapper;
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
		long totalMinutes = entryDateTime.until(currentTime, ChronoUnit.MINUTES);
		double totalUnits = Math.ceil(totalMinutes / paymentPeriodsInMinutes);
		BigDecimal totalPrice = new BigDecimal(totalUnits * pricePerPeriod);
		return PaymentInfoResponse.newBuilder()
				.withEntryTime(parkingRegister.getEntry())
				.withTotalAmount(totalPrice)
				.withTotalMinutes(totalMinutes)
				.withUser(modelMapper.map(client.getUser(), UserApi.class))
				.build();
	}

}
