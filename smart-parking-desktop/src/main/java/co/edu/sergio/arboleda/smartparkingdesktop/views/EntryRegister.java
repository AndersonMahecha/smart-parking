package co.edu.sergio.arboleda.smartparkingdesktop.views;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

import co.edu.sergio.arboleda.smartparkingdesktop.exceptions.GenericException;
import co.edu.sergio.arboleda.smartparkingdesktop.pojo.ParkingRegisterApi;
import co.edu.sergio.arboleda.smartparkingdesktop.services.Factory.ParkingServiceFactory;
import co.edu.sergio.arboleda.smartparkingdesktop.services.ParkingService;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

public class EntryRegister extends View {

	@FXML
	public TextField plateNumber;
	@FXML
	public TextField name;
	@FXML
	public TextField time;
	@FXML
	public TextField currentTime;
	private String licenseCode = "";

	private final ParkingService parkingService;

	public EntryRegister() {
		parkingService = ParkingServiceFactory.create();
	}

	@Override
	public void loadView() throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(EntryRegister.class.getResource("EntryRegister.fxml"));
		fxmlLoader.setController(this);
		Scene scene = new Scene(fxmlLoader.load());
		scene.getRoot().setStyle("-fx-font-family: 'Serif Regular'");
		super.setScene(scene);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		AnimationTimer timer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				currentTime.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
			}
		};
		timer.start();

		serialPort.addDataListener(new SerialPortDataListener() {
			@Override
			public int getListeningEvents() {
				return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
			}

			@Override
			public void serialEvent(SerialPortEvent event) {
				if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE) {
					return;
				}
				byte[] newData = new byte[serialPort.bytesAvailable()];
				int numRead = serialPort.readBytes(newData, newData.length);
				System.out.println(numRead);
				String s = new String(newData);
				if (s.contains("#")) {
					s = s.replace("#", "");
				}
				licenseCode += s;
				if (s.contains("\n")) {
					licenseCode = licenseCode.replace("\n", "");
					try {
						ParkingRegisterApi parkingRegisterApi = parkingService.registerEntry(licenseCode,
								loginResponse.getToken());
						loadData(parkingRegisterApi);
					} catch (GenericException e) {
						//tempAlert(e);
					} finally {
						licenseCode = "";
					}
				}
			}
		});
		serialPort.openPort();
	}

	private void tempAlert(GenericException e) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION, e.getErrorCode(), ButtonType.OK);
		Thread thread = new Thread(() -> {
			try {
				Thread.sleep(5000);
				if (alert.isShowing()) {
					Platform.runLater(alert::close);
				}
			} catch (Exception exp) {
				exp.printStackTrace();
			}
		});
		thread.setDaemon(true);
		thread.start();
		alert.showAndWait();
	}

	private void loadData(ParkingRegisterApi parkingRegisterApi) {
		plateNumber.setText(parkingRegisterApi.getLicensePlate());
		name.setText(parkingRegisterApi.getClient().getName());
		time.setText(parkingRegisterApi.getEntry().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

		Thread thread = new Thread(() -> {
			try {
				Thread.sleep(5000);
				Platform.runLater(() -> {
					plateNumber.setText("");
					name.setText("");
					time.setText("");
				});
			} catch (Exception exp) {
				exp.printStackTrace();
			}
		});
		thread.setDaemon(true);
		thread.start();

	}

}
