package co.edu.sergio.arboleda.smartparkingdesktop.services.Factory;

import co.edu.sergio.arboleda.smartparkingdesktop.services.UsersService;
import co.edu.sergio.arboleda.smartparkingdesktop.services.impl.UsersServiceImpl;

public class UserServiceFactory {

	public static UsersService create() {
		return new UsersServiceImpl();
	}

}
