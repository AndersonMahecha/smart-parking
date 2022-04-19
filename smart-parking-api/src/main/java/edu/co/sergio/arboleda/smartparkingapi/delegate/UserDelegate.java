package edu.co.sergio.arboleda.smartparkingapi.delegate;

import edu.co.sergio.arboleda.smartparkingapi.repository.entity.Client;
import edu.co.sergio.arboleda.smartparkingapi.rest.api.UserApi;
import edu.co.sergio.arboleda.smartparkingapi.rest.request.LoginRequest;
import edu.co.sergio.arboleda.smartparkingapi.rest.response.LoginResponse;
import edu.co.sergio.arboleda.smartparkingapi.util.exceptions.GenericException;

public interface UserDelegate {

	void create(UserApi userApi) throws GenericException;

	void create(Client client);

	LoginResponse login(LoginRequest loginRequest) throws GenericException;

}
