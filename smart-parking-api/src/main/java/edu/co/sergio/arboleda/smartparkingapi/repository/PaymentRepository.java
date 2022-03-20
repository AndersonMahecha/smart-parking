package edu.co.sergio.arboleda.smartparkingapi.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.co.sergio.arboleda.smartparkingapi.repository.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {

}
