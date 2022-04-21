package co.edu.sergio.arboleda.smartparkingdesktop.services;

import co.edu.sergio.arboleda.smartparkingdesktop.exceptions.GenericException;
import co.edu.sergio.arboleda.smartparkingdesktop.pojo.PaymentInfoResponse;

public interface PaymentService {

	PaymentInfoResponse getPayment(String licenseCode, String token) throws GenericException;

	PaymentInfoResponse pay(String paymentLicenseCode, String token) throws GenericException;

}
