package edu.co.sergio.arboleda.smartparkingapi.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.co.sergio.arboleda.smartparkingapi.rest.api.ClientApi;
import edu.co.sergio.arboleda.smartparkingapi.service.ClientService;
import edu.co.sergio.arboleda.smartparkingapi.util.exceptions.GenericException;

@RestController
@RequestMapping(path = "/users")
@CrossOrigin(origins = "*")
public class ClientController {

	private final ClientService clientService;

	@Autowired
	public ClientController(ClientService clientService) {
		this.clientService = clientService;
	}

	@PostMapping
	private ResponseEntity<ClientApi> create(@RequestBody ClientApi clientApi) {
		ClientApi response = clientService.create(clientApi);
		return ResponseEntity.ok(response);
	}

	@GetMapping()
	private ResponseEntity<List<ClientApi>> findAll() {
		List<ClientApi> response = clientService.findAll();
		return ResponseEntity.ok(response);
	}

	@GetMapping(path = "/find")
	private ResponseEntity<ClientApi> findStudent(@RequestParam(name = "licenseCode") String licenseCode,
												  @RequestParam("documentNumber") String documentNumber)
			throws GenericException {
		ClientApi clientApi = clientService.findBuDocumentNumberOrLicenseCode(documentNumber, licenseCode);
		return ResponseEntity.ok(clientApi);
	}

}
