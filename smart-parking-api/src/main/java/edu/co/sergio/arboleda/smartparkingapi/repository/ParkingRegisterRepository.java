package edu.co.sergio.arboleda.smartparkingapi.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.co.sergio.arboleda.smartparkingapi.repository.entity.Client;
import edu.co.sergio.arboleda.smartparkingapi.repository.entity.ParkingRegister;

public interface ParkingRegisterRepository extends JpaRepository<ParkingRegister, UUID> {

	Optional<ParkingRegister> findByClientAndExitIsNull(Client client);

}
