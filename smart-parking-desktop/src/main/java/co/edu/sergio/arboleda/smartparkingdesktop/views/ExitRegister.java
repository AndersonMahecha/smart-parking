package co.edu.sergio.arboleda.smartparkingdesktop.views;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

public class ExitRegister extends View {

	@Override
	public void loadView() throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(ExitRegister.class.getResource("ExitRegister.fxml"));
		fxmlLoader.setController(this);
		Scene scene = new Scene(fxmlLoader.load());
		scene.getRoot().setStyle("-fx-font-family: 'Serif Regular'");
		super.setScene(scene);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

}
