package application;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class logintochat {
	database database;

	public logintochat() {
		Thread thread = new Thread() {
			public void run() {
				database = new database();
			}
		};
		thread.start();
	}

	@FXML
	PasswordField password;
	@FXML
	TextField userName;
	@FXML
	Button Submit;
	@FXML
	Label errorField;
	@FXML
	AnchorPane anchorPane;

	@FXML
	void login(ActionEvent e) {
		try {
			if (userName.getText() == "" || password.getText() == "") {
				errorField.setText("Please Enter username and password");
			}
			ResultSet resultSet = database.statement
					.executeQuery("select * from chatUser where  userName = '" + userName.getText() + "';");
			if (resultSet.next() == false) {
				errorField.setText("Invalid Username or Password");
			} else {
				if (resultSet.getString("password").equals(password.getText())) {
					errorField.setTextFill(Color.web("#00FF00"));
					errorField.setText("Login Sucessfull");
					new appPage(userName.getText()).apPage(anchorPane);
				} else {
					errorField.setText("Invalid Username or Password");
				}
			}
		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e2) {
			e2.printStackTrace();
		}
	}

	@FXML
	public void signup() throws Exception {
		URL url = new File("D:\\java programming\\Application\\application\\Registration.fxml").toURI().toURL();
		Parent root = FXMLLoader.load(url);
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		Stage primaryStage = (Stage) anchorPane.getScene().getWindow();
		primaryStage.setScene(scene);
		primaryStage.show();
	}

}
