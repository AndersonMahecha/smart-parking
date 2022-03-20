package edu.co.sergio.arboleda.smartparkingapi.service;

import edu.co.sergio.arboleda.smartparkingapi.rest.response.PaymentInfoResponse;

public interface PaymentService {

	void pay(String licenseCode);

	PaymentInfoResponse getPaymentInfo(String licenseCode);

}
