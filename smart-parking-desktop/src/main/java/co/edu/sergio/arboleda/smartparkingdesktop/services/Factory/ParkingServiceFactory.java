package co.edu.sergio.arboleda.smartparkingdesktop.services.Factory;

import co.edu.sergio.arboleda.smartparkingdesktop.services.ParkingService;
import co.edu.sergio.arboleda.smartparkingdesktop.services.impl.ParkingServiceImpl;

public class ParkingServiceFactory {

	public static ParkingService create() {
		return new ParkingServiceImpl();
	}

}
