package edu.co.sergio.arboleda.smartparkingapi.service;

import java.util.List;

import org.springframework.stereotype.Service;

import edu.co.sergio.arboleda.smartparkingapi.GenericException;
import edu.co.sergio.arboleda.smartparkingapi.rest.api.StudentApi;

public interface StudentService {

	List<StudentApi> findAll();

	StudentApi create(StudentApi studentApi);

	StudentApi searchByDocumentNumber(String documentNumber) throws GenericException;

	StudentApi searchByLicense(String licenseCode) throws GenericException;

}
