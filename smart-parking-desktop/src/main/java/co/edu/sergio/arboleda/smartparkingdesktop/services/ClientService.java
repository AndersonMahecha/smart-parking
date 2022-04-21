package co.edu.sergio.arboleda.smartparkingdesktop.services;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;

import co.edu.sergio.arboleda.smartparkingdesktop.exceptions.GenericException;
import co.edu.sergio.arboleda.smartparkingdesktop.pojo.ClientApi;

public interface ClientService {

	List<ClientApi> getClients(String token) throws GenericException;

	ClientApi create(ClientApi clientApi, String token) throws JsonProcessingException, GenericException;

}
