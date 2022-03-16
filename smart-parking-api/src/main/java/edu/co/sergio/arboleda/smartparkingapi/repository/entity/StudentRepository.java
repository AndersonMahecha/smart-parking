package edu.co.sergio.arboleda.smartparkingapi.repository.entity;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, UUID> {

	Optional<Student> findByUserDocumentNumber(String user_documentNumber);

	Optional<Student> findByLicenseCode(String licenseCode);
}
