package edu.co.sergio.arboleda.smartparkingapi.rest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.co.sergio.arboleda.smartparkingapi.rest.response.PaymentInfoResponse;

@RequestMapping(path = "/payments")
public class PaymentController {


	@PostMapping(path = "/pay")
	public ResponseEntity<Void> payParking(){
		return ResponseEntity.ok().build();
	}

	@GetMapping(path = "/amount")
	public ResponseEntity<PaymentInfoResponse> getPaymentInfo(){
		return ResponseEntity.ok().build();
	}
}
