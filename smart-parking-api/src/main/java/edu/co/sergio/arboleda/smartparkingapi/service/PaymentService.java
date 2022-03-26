package edu.co.sergio.arboleda.smartparkingapi.service;

import edu.co.sergio.arboleda.smartparkingapi.rest.response.PaymentInfoResponse;
import edu.co.sergio.arboleda.smartparkingapi.util.exceptions.GenericException;

public interface PaymentService {

	void pay(String licenseCode);

	PaymentInfoResponse getPaymentInfo(String licenseCode) throws GenericException;

}
