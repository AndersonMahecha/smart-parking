package edu.co.sergio.arboleda.smartparkingapi.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.co.sergio.arboleda.smartparkingapi.repository.entity.Client;

public interface ClientRepository extends JpaRepository<Client, UUID> {

	Optional<Client> findByUserDocumentNumber(String user_documentNumber);

	Optional<Client> findByLicenseCode(String licenseCode);
}
