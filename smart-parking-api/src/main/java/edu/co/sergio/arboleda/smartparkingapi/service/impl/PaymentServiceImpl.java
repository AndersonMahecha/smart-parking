package edu.co.sergio.arboleda.smartparkingapi.service.impl;

import org.springframework.stereotype.Service;

import edu.co.sergio.arboleda.smartparkingapi.rest.response.PaymentInfoResponse;
import edu.co.sergio.arboleda.smartparkingapi.service.PaymentService;

@Service
public class PaymentServiceImpl implements PaymentService {

	@Override
	public void pay(String licenseCode) {

	}

	@Override
	public PaymentInfoResponse getPaymentInfo(String licenseCode) {
		return null;
	}

}
