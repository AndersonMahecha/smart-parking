package co.edu.sergio.arboleda.smartparkingdesktop;

import java.io.IOException;

import co.edu.sergio.arboleda.smartparkingdesktop.views.Home;
import co.edu.sergio.arboleda.smartparkingdesktop.views.View;
import javafx.stage.Stage;

public class Application extends javafx.application.Application {

	@Override
	public void start(Stage stage) throws IOException {
		View home = new Home();
		home.init(stage);
		home.show();
		stage.setOnCloseRequest(event -> System.exit(0));
	}

	public static void main(String[] args) {
		launch(args);
	}

}