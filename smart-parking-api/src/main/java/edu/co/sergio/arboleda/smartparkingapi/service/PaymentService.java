package edu.co.sergio.arboleda.smartparkingapi.service;

import edu.co.sergio.arboleda.smartparkingapi.rest.response.PaymentInfoResponse;
import edu.co.sergio.arboleda.smartparkingapi.util.exceptions.GenericException;

public interface PaymentService {

	PaymentInfoResponse pay(String licenseCode) throws GenericException;

	PaymentInfoResponse getPaymentInfo(String licenseCode) throws GenericException;

}
