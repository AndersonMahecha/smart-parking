package co.edu.sergio.arboleda.smartparkingdesktop.services.Factory;

import co.edu.sergio.arboleda.smartparkingdesktop.services.PaymentService;
import co.edu.sergio.arboleda.smartparkingdesktop.services.impl.PaymentServiceImpl;

public class PaymentServiceFactory {

	public static PaymentService create() {
		return new PaymentServiceImpl();
	}

}
