package edu.co.sergio.arboleda.smartparkingapi.service.impl;

import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.co.sergio.arboleda.smartparkingapi.GenericException;
import edu.co.sergio.arboleda.smartparkingapi.repository.entity.Student;
import edu.co.sergio.arboleda.smartparkingapi.repository.entity.StudentRepository;
import edu.co.sergio.arboleda.smartparkingapi.rest.api.StudentApi;
import edu.co.sergio.arboleda.smartparkingapi.service.StudentService;

@Service
public class StudentServiceImpl implements StudentService {

	private final ModelMapper modelMapper;
	private final StudentRepository studentRepository;

	@Autowired
	public StudentServiceImpl(ModelMapper modelMapper,
							  StudentRepository studentRepository) {
		this.modelMapper = modelMapper;
		this.studentRepository = studentRepository;
	}

	@Override
	public List<StudentApi> findAll() {
		return studentRepository.findAll()
				.stream()
				.map(student -> modelMapper.map(student, StudentApi.class))
				.collect(Collectors.toList());
	}

	@Override
	public StudentApi create(StudentApi studentApi) {
		Student student = modelMapper.map(studentApi, Student.class);
		return modelMapper.map(studentRepository.save(student), StudentApi.class);
	}

	@Override
	public StudentApi searchByDocumentNumber(String documentNumber) throws GenericException {
		Optional<Student> byUserDocumentNumber = studentRepository.findByUserDocumentNumber(documentNumber);
		if(byUserDocumentNumber.isPresent()){
			return modelMapper.map(byUserDocumentNumber.get(), StudentApi.class);
		}
		throw new GenericException(String.format("El numero de documento : %s no fue encontrado", documentNumber), "DOCUMENT_NUMBER_NOT_FOUND");
	}

	@Override
	public StudentApi searchByLicense(String licenseCode) throws GenericException {
		Optional<Student> byLicenseCode = studentRepository.findByLicenseCode(licenseCode);
		if(byLicenseCode.isPresent()){
			return modelMapper.map(byLicenseCode.get(), StudentApi.class);
		}
		throw new GenericException("El carnet no se encuentra registrado", "LICENSE_NOT_FOUND");
	}

}
