package co.edu.sergio.arboleda.smartparkingdesktop.views;

import java.io.IOException;

import com.fazecast.jSerialComm.SerialPort;

import co.edu.sergio.arboleda.smartparkingdesktop.pojo.LoginResponse;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.stage.Stage;

public abstract class View implements Initializable {

	private Scene scene;
	private Stage current;
	SerialPort serialPort;
	LoginResponse loginResponse;

	public void init(Stage stage) throws IOException {
		loadView();
		current = stage;
	}

	public void show() {
		current.setScene(scene);
		current.show();
	}

	public abstract void loadView() throws IOException;

	public void setScene(Scene scene) {
		this.scene = scene;
	}

	public Scene getScene() {
		return scene;
	}

	public Stage getCurrent() {
		return current;
	}

	public void setCurrent(Stage current) {
		this.current = current;
	}

	protected void setLogin(LoginResponse login) {
		loginResponse = login;
	}

	protected void setSerialPort(SerialPort serialPort) {
		this.serialPort = serialPort;
	}

}
