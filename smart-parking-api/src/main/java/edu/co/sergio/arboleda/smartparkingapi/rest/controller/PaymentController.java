package edu.co.sergio.arboleda.smartparkingapi.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.co.sergio.arboleda.smartparkingapi.rest.response.PaymentInfoResponse;
import edu.co.sergio.arboleda.smartparkingapi.service.PaymentService;
import edu.co.sergio.arboleda.smartparkingapi.util.exceptions.GenericException;

@RestController
@RequestMapping(path = "/payments")
@CrossOrigin(origins = "*")
public class PaymentController {

	private final PaymentService paymentService;

	@Autowired
	public PaymentController(PaymentService paymentService) {
		this.paymentService = paymentService;
	}

	@PostMapping(path = "/pay/{licenseCode}")
	public ResponseEntity<Void> payParking(@PathVariable String licenseCode){
		paymentService.pay(licenseCode);
		return ResponseEntity.ok().build();
	}

	@GetMapping(path = "/amount/{licenseCode}")
	public ResponseEntity<PaymentInfoResponse> getPaymentInfo(@PathVariable(name = "licenseCode") String licenseCode)
			throws GenericException {
		PaymentInfoResponse paymentInfo = paymentService.getPaymentInfo(licenseCode);
		return ResponseEntity.ok(paymentInfo);
	}
}
