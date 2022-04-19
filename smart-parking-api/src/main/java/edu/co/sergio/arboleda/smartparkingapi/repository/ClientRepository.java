package edu.co.sergio.arboleda.smartparkingapi.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.co.sergio.arboleda.smartparkingapi.repository.entity.Client;

public interface ClientRepository extends JpaRepository<Client, UUID> {

	Optional<Client> findByDocumentNumber(String user_documentNumber);

	Optional<Client> findByLicenseCode(String licenseCode);

	Optional<Client> findByEmail(String email);

}
