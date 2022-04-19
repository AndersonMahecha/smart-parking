package edu.co.sergio.arboleda.smartparkingapi.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.co.sergio.arboleda.smartparkingapi.repository.entity.User;

public interface UserRepository extends JpaRepository<User, UUID> {

	Optional<User> findUserByUsername(String username);

}
