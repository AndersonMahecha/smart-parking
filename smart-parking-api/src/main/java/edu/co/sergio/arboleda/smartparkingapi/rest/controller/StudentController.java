package edu.co.sergio.arboleda.smartparkingapi.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.co.sergio.arboleda.smartparkingapi.rest.api.StudentApi;
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
	private ResponseEntity<StudentApi> create(@RequestBody StudentApi studentApi){
		StudentApi response = studentService.create(studentApi);
		return ResponseEntity.ok(response);
	}

	@GetMapping(path = "/")
	private ResponseEntity<List<StudentApi>> findAll() {
		List<StudentApi> response = studentService.findAll();
		return ResponseEntity.ok(response);
	}

}
