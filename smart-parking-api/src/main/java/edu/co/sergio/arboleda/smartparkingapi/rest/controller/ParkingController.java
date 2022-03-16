package edu.co.sergio.arboleda.smartparkingapi.rest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/parking")
public class ParkingController {

	@PostMapping(path = "/entry")
	ResponseEntity<Void> registerEntry(){
		return ResponseEntity.ok().build();
	}
}
