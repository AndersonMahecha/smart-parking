package co.edu.sergio.arboleda.smartparkingdesktop.services;

import com.fasterxml.jackson.core.JsonProcessingException;

import co.edu.sergio.arboleda.smartparkingdesktop.exceptions.GenericException;
import co.edu.sergio.arboleda.smartparkingdesktop.pojo.LoginResponse;

public interface UsersService {

	LoginResponse login(String username, String password) throws GenericException, JsonProcessingException;

}
