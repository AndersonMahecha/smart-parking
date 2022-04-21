package co.edu.sergio.arboleda.smartparkingdesktop.services.Factory;

import co.edu.sergio.arboleda.smartparkingdesktop.services.ClientService;
import co.edu.sergio.arboleda.smartparkingdesktop.services.impl.ClientServiceImpl;

public class ClientServiceFactory {

	public static ClientService create() {
		return new ClientServiceImpl();
	}

}
