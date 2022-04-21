module co.edu.sergio.arboleda.smartparkingdesktop {
	requires javafx.controls;
	requires javafx.fxml;
	requires spring.webflux;
	requires spring.web;
	requires reactor.core;
	requires com.fazecast.jSerialComm;

	opens co.edu.sergio.arboleda.smartparkingdesktop to javafx.fxml;
	requires com.fasterxml.jackson.databind;
	exports co.edu.sergio.arboleda.smartparkingdesktop;
	exports co.edu.sergio.arboleda.smartparkingdesktop.views;
	exports co.edu.sergio.arboleda.smartparkingdesktop.pojo;
}