package co.edu.sergio.arboleda.smartparkingdesktop.views;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

import co.edu.sergio.arboleda.smartparkingdesktop.exceptions.GenericException;
import co.edu.sergio.arboleda.smartparkingdesktop.pojo.ClientApi;
import co.edu.sergio.arboleda.smartparkingdesktop.pojo.PaymentInfoResponse;
import co.edu.sergio.arboleda.smartparkingdesktop.pojo.UserApi;
import co.edu.sergio.arboleda.smartparkingdesktop.services.ClientService;
import co.edu.sergio.arboleda.smartparkingdesktop.services.Factory.ClientServiceFactory;
import co.edu.sergio.arboleda.smartparkingdesktop.services.Factory.PaymentServiceFactory;
import co.edu.sergio.arboleda.smartparkingdesktop.services.PaymentService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

public class Admin extends View {

	@FXML
	public TableView usersTable;
	@FXML
	public Button reloadButton;
	@FXML
	public Button createUserButton;
	@FXML
	public TabPane tabPane;
	@FXML
	public SplitPane splitPane;

	//Payments

	@FXML
	public TextField nameOutput;
	@FXML
	public TextField plateOutput;
	@FXML
	public TextField inTimeOutput;
	@FXML
	public TextField totalTimeOutput;
	@FXML
	public TextField totalCostOutput;
	@FXML
	public Button payButton;

	//UserCreation
	private Button cancelBtn;
	private Button createBtn;
	private TextField code;
	private TextField nameInput;
	private ComboBox<String> documentTypeCombo;
	private TextField documentNumberInput;
	private TextField phoneNumberInput;
	private TextField emailInput;

	private String licenseCode = "";
	private String paymentLicenseCode = "";

	private final ClientService clientService;
	private final PaymentService paymentService;

	private VBox vBox;

	public Admin() {
		clientService = ClientServiceFactory.create();
		paymentService = PaymentServiceFactory.create();
	}

	@Override
	public void loadView() throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(Admin.class.getResource("Admin.fxml"));
		vBox = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("UserCreation.fxml")));
		createBtn = (Button) vBox.lookup("#createBtn");
		cancelBtn = (Button) vBox.lookup("#cancelBtn");
		nameInput = (TextField) vBox.lookup("#nameInput");
		documentTypeCombo = (ComboBox) vBox.lookup("#documentTypeCombo");
		documentNumberInput = (TextField) vBox.lookup("#documentNumberInput");
		phoneNumberInput = (TextField) vBox.lookup("#phoneNumberInput");
		emailInput = (TextField) vBox.lookup("#emailInput");
		code = (TextField) vBox.lookup("#code");
		code.setEditable(false);
		fxmlLoader.setController(this);
		Scene scene = new Scene(fxmlLoader.load());
		super.setScene(scene);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		reloadButton.setOnAction(actionEvent -> loadUsers());
		createUserButton.setOnAction(actionEvent -> showCreateUser());
		payButton.setOnAction(event -> makePayment());
		tabPane.getSelectionModel().selectedItemProperty().addListener((observableValue, oldTab, newTab) -> {
			manageTabChange(newTab);
		});
		serialPort.openPort();
		loadUsers();
	}

	private void makePayment() {
		try {
			PaymentInfoResponse paymentInfoResponse = paymentService.pay(paymentLicenseCode, loginResponse.getToken());
			loadPaymentInfo(paymentInfoResponse);
		} catch (GenericException e) {
			//e.printStackTrace();
		}
	}

	private void manageTabChange(Tab newTab) {
		if (newTab.getId().equals("payments")) {
			serialPort.removeDataListener();
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
							PaymentInfoResponse payment = paymentService.getPayment(licenseCode,
									loginResponse.getToken());
							paymentLicenseCode = licenseCode;
							loadPaymentInfo(payment);
						} catch (GenericException e) {
							//e.printStackTrace();
						} finally {
							licenseCode = "";
						}
					}
				}
			});
		}
	}

	private void loadPaymentInfo(PaymentInfoResponse payment) {
		nameOutput.setText(payment.getClientApi().getName());
		plateOutput.setText(payment.getLicensePlate());
		inTimeOutput.setText(payment.getEntryTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
		totalTimeOutput.setText(payment.getTotalMinutes() + " Minutes");
		totalCostOutput.setText("$ " + payment.getTotalAmount().toPlainString());

		Thread thread = new Thread(() -> {
			try {
				Thread.sleep(10000);
				Platform.runLater(() -> {
					nameOutput.setText("");
					plateOutput.setText("");
					inTimeOutput.setText("");
					totalTimeOutput.setText("");
					totalCostOutput.setText("");
				});
			} catch (Exception exp) {
				exp.printStackTrace();
			}
		});
		thread.setDaemon(true);
		thread.start();
	}

	private void loadUsers() {
		try {
			usersTable.getItems().clear();
			usersTable.getColumns().clear();
			List<ClientApi> clients = clientService.getClients(loginResponse.getToken());

			TableColumn<String, UserApi> nameColumn = new TableColumn<>("Nombre completo");
			nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
			usersTable.getColumns().add(nameColumn);

			TableColumn<String, UserApi> documentTypeColumn = new TableColumn<>("Tipo de documento");
			documentTypeColumn.setCellValueFactory(new PropertyValueFactory<>("documentType"));
			usersTable.getColumns().add(documentTypeColumn);

			TableColumn<String, UserApi> documentNumberColumn = new TableColumn<>("Numero de documento");
			documentNumberColumn.setCellValueFactory(new PropertyValueFactory<>("documentNumber"));
			usersTable.getColumns().add(documentNumberColumn);

			TableColumn<String, UserApi> mobileNumberColumn = new TableColumn<>("Numero de telefono");
			mobileNumberColumn.setCellValueFactory(new PropertyValueFactory<>("mobileNumber"));
			usersTable.getColumns().add(mobileNumberColumn);

			TableColumn<String, UserApi> emailColumn = new TableColumn<>("Email");
			emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
			usersTable.getColumns().add(emailColumn);

			TableColumn<String, UserApi> licenseCodeColumn = new TableColumn<>("Codigo de carnet");
			licenseCodeColumn.setCellValueFactory(new PropertyValueFactory<>("licenseCode"));
			usersTable.getColumns().add(licenseCodeColumn);
			for (ClientApi client : clients) {
				usersTable.getItems().add(client);
			}
		} catch (GenericException e) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION, e.getErrorCode(), ButtonType.OK);
			alert.showAndWait();
		}
	}

	private void showCreateUser() {
		splitPane.getItems().remove(tabPane);
		splitPane.getItems().add(vBox);
		createBtn.setOnAction(actionEvent -> createUser());
		cancelBtn.setOnAction(actionEvent -> cancelCreation());
		serialPort.removeDataListener();
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
					code.setText("");
					s = s.replace("#", "");
				}
				code.setText(code.getText() + s);
			}
		});
	}

	private void createUser() {
		ClientApi clientApi = ClientApi.newBuilder()
				.withName(nameInput.getText())
				.withDocumentType(documentTypeCombo.getSelectionModel().getSelectedItem())
				.withDocumentNumber(documentNumberInput.getText())
				.withEmail(emailInput.getText())
				.withMobileNumber(phoneNumberInput.getText())
				.withLicenseCode(code.getText())
				.build();
		try {
			clientService.create(clientApi, loginResponse.getToken());
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Usuario creado", ButtonType.OK);
			alert.showAndWait();
		} catch (GenericException e) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION, e.getErrorCode(), ButtonType.OK);
			alert.showAndWait();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void cancelCreation() {
		splitPane.getItems().add(tabPane);
		splitPane.getItems().remove(vBox);
	}

}
