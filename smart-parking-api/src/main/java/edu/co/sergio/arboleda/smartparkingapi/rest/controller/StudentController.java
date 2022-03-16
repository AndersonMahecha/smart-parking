package edu.co.sergio.arboleda.smartparkingapi.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.co.sergio.arboleda.smartparkingapi.GenericException;
import edu.co.sergio.arboleda.smartparkingapi.rest.api.ClientApi;
import edu.co.sergio.arboleda.smartparkingapi.service.StudentService;

@RestController
@RequestMapping(path = "/students")
public class StudentController {

	private final StudentService studentService;

	@Autowired
	public StudentController(StudentService studentService) {
		this.studentService = studentService;
	}

	@PostMapping
	private ResponseEntity<ClientApi> create(@RequestBody ClientApi clientApi) {
		ClientApi response = studentService.create(clientApi);
		return ResponseEntity.ok(response);
	}

	@GetMapping(path = "/")
	private ResponseEntity<List<ClientApi>> findAll() {
		List<ClientApi> response = studentService.findAll();
		return ResponseEntity.ok(response);
	}

	@GetMapping(path = "/find")
	private ResponseEntity<ClientApi> findStudent(@RequestParam(name = "licenseCode") String licenseCode,
												  @RequestParam("documentNumber") String documentNumber)
			throws GenericException {
		ClientApi clientApi = studentService.findBuDocumentNumberOrLicenseCode(documentNumber, licenseCode);
		return ResponseEntity.ok(clientApi);
	}

}
