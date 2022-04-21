package co.edu.sergio.arboleda.smartparkingdesktop.views;

import static javafx.scene.control.Alert.AlertType;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

import com.fazecast.jSerialComm.SerialPort;

import co.edu.sergio.arboleda.smartparkingdesktop.enums.UserType;
import co.edu.sergio.arboleda.smartparkingdesktop.exceptions.GenericException;
import co.edu.sergio.arboleda.smartparkingdesktop.pojo.LoginResponse;
import co.edu.sergio.arboleda.smartparkingdesktop.services.Factory.UserServiceFactory;
import co.edu.sergio.arboleda.smartparkingdesktop.services.UsersService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class Home extends View {

	@FXML
	public TextField userInput;
	@FXML
	public PasswordField passwordInput;
	@FXML
	public Button loginButton;
	@FXML
	public ComboBox<String> puertosCombo;

	private final UsersService usersService;
	private final Admin adminView;
	private final EntryRegister entryRegisterView;
	private final ExitRegister exitRegisterView;

	public Home() throws IOException {
		usersService = UserServiceFactory.create();
		adminView = new Admin();
		exitRegisterView = new ExitRegister();
		entryRegisterView = new EntryRegister();
	}

	@Override
	public void loadView() throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(Home.class.getResource("Home.fxml"));
		fxmlLoader.setController(this);
		Scene localScene = new Scene(fxmlLoader.load());
		localScene.getStylesheets().add(Objects.requireNonNull(
				getClass().getResource("/co/edu/sergio/arboleda/smartparkingdesktop/root.css")).toExternalForm());
		super.setScene(localScene);
	}

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		loginButton.setOnAction(actionEvent -> login());
		SerialPort[] ports = SerialPort.getCommPorts();
		for (SerialPort port : ports) {
			puertosCombo.getItems().add(port.getDescriptivePortName());
		}
	}

	public void login() {
		try {
			SerialPort[] ports = SerialPort.getCommPorts();
			Optional<SerialPort> portOptional = Arrays.stream(ports)
					.filter(serialPort -> serialPort.getDescriptivePortName()
							.equals(puertosCombo.getSelectionModel().getSelectedItem()))
					.findFirst();

			if (portOptional.isEmpty()) {
				Alert alert = new Alert(AlertType.INFORMATION, "Debe seleccionar el puerto serial", ButtonType.OK);
				alert.showAndWait();
				return;
			}

			SerialPort serialPort = portOptional.get();

			LoginResponse login = usersService.login(userInput.getText(), passwordInput.getText());
			if (Objects.equals(login.getUser().getUserType(), UserType.ROOT.getCode())) {
				adminView.setLogin(login);
				adminView.setSerialPort(serialPort);
				adminView.init(getCurrent());
				adminView.show();
			}
			if (Objects.equals(login.getUser().getUserType(), UserType.ADMIN.getCode())) {
				adminView.setLogin(login);
				adminView.setSerialPort(serialPort);
				adminView.init(getCurrent());
				adminView.show();
			}
			if (Objects.equals(login.getUser().getUserType(), UserType.CASHIER.getCode())) {
				adminView.setLogin(login);
				adminView.setSerialPort(serialPort);
				adminView.init(getCurrent());
				adminView.show();
			}
			if (Objects.equals(login.getUser().getUserType(), UserType.ENTRY.getCode())) {
				entryRegisterView.setLogin(login);
				entryRegisterView.setSerialPort(serialPort);
				entryRegisterView.init(getCurrent());
				entryRegisterView.show();
			}
			if (Objects.equals(login.getUser().getUserType(), UserType.EXIT.getCode())) {
				exitRegisterView.setLogin(login);
				exitRegisterView.setSerialPort(serialPort);
				exitRegisterView.init(getCurrent());
				exitRegisterView.show();
			}
		} catch (GenericException e) {
			Alert alert = new Alert(AlertType.INFORMATION, e.getErrorCode(), ButtonType.OK);
			alert.showAndWait();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
