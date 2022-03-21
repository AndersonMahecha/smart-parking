package edu.co.sergio.arboleda.smartparkingapi.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.co.sergio.arboleda.smartparkingapi.service.ParkingService;
import edu.co.sergio.arboleda.smartparkingapi.util.exceptions.GenericException;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/parking")
public class ParkingController {

	private final ParkingService parkingService;

	@Autowired
	public ParkingController(ParkingService parkingService) {
		this.parkingService = parkingService;
	}

	@PostMapping(path = "/entry")
	ResponseEntity<Void> registerEntry(@RequestParam("licenseCode") String licenseCode,
									   @RequestParam("vehicleType") String vehicleType) throws GenericException {
		parkingService.registerEntry(licenseCode, vehicleType);
		return ResponseEntity.ok().build();
	}

	@PostMapping(path = "/exit")
	ResponseEntity<Void> registerExit(@RequestParam("licenseCode") String licenseCode) throws GenericException {
		parkingService.registerExit(licenseCode);
		return ResponseEntity.ok().build();
	}

}
