package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.util.regex.Pattern;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Registration {
	database database;

	public Registration() {
		Runnable runnable = () -> {
			database = new database();
		};
		new Thread(runnable).start();
	}

	// Registration page
	@FXML
	TextField userName2;
	@FXML
	PasswordField newPassword;
	@FXML
	TextField email;
	@FXML
	Label errorField3;
	@FXML
	AnchorPane anchorPane3;

	public static boolean isValidEmail(String email) {
		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z"
				+ "A-Z]{2,7}$";

		Pattern pat = Pattern.compile(emailRegex);
		if (email == null)
			return false;
		return pat.matcher(email).matches();
	}

	@FXML
	public void register() throws Exception {
		if (userName2.getText().equals("") || newPassword.getText().equals("") || email.getText().equals("")) {
			errorField3.setText("Please Fill All Entries");
		} else {
			database.connection.setAutoCommit(false);
			if (isValidEmail(email.getText())) {
				ResultSet resultSet = database.statement
						.executeQuery("select * from chatuser where username = '" + userName2.getText() + "';");
				if (resultSet.next() == false) {
					if (database.statement.executeUpdate("insert into chatuser values('" + userName2.getText() + "','"+ newPassword.getText() + "'," + null + "," + null + "," + null + ",'" + email.getText()+ "'," + null + "," + null +"," + null +");") != -1) {
						errorField3.setTextFill(Color.web("#00FF00"));
						FileInputStream imageFileInputStream = new FileInputStream("D:\\java programming\\Application\\application\\profileImage.png");
						database.preparedStatement = database.connection.prepareStatement("update chatuser set profilepicture = (?) where username = '"+userName2.getText()+"';");
						database.preparedStatement.setBinaryStream(1, imageFileInputStream);
						database.preparedStatement.execute();
						database.connection.commit();
						URL url = new File("D:\\java programming\\Application\\application\\personalInfo.fxml").toURI().toURL();
						Parent root = FXMLLoader.load(url);
						// Parent root =
						// FXMLLoader.load(getClass().getResource("/application/login_page.fxml")); this
						// also works
						Scene scene = new Scene(root);
						scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
						Stage primaryStage = (Stage) anchorPane3.getScene().getWindow();
						primaryStage.setScene(scene);
						primaryStage.show();
					} else {
						errorField3.setText("Registartion Unucessfull");
						email.setText("");
						newPassword.setText("");
					}
				} else {
					errorField3.setText("Username already registered");
				}
			} else {
				errorField3.setText("Please Enter a Valid Email");
				email.setText("");
				newPassword.setText("");
			}
		}

	}

	@FXML
	public void BackTologin() throws Exception {
		back(anchorPane3);
	}
	
	public void back(AnchorPane anchorPane) throws Exception {
		URL url = new File("D:\\java programming\\Application\\application\\login_page.fxml").toURI().toURL();
		Parent root = FXMLLoader.load(url);
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		Stage primaryStage = (Stage) anchorPane.getScene().getWindow();
		primaryStage.setScene(scene);
		primaryStage.show();
	}

}
