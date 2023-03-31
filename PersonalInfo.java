package application;

import java.sql.Date;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class PersonalInfo {
	database database;

	public PersonalInfo() {
		Runnable runnable = () -> {
			database = new database();
		};
		new Thread(runnable).start();
	}

	// personal information
	@FXML
	TextField userName3;
	@FXML
	TextField firstName;
	@FXML
	TextField lastName;
	@FXML
	DatePicker dateOfBirth;
	@FXML
	TextField profession;
	@FXML
	Label errorField2;
	@FXML
	AnchorPane anchorPane2;

	@FXML
	public void Login() throws Exception {
		if (userName3.getText().equals("")||firstName.getText().equals("") || lastName.getText().equals("") || dateOfBirth.getValue().equals("")|| profession.getText().equals("")) {
			errorField2.setText("Please Fill all the places");
		} 
		else {
			Date date = Date.valueOf(dateOfBirth.getValue());
			if ((database.statement.executeUpdate("update chatuser set firstname = '" + firstName.getText()+ "',lastname = '" + lastName.getText() + "',dateofbirth = '" + date + "',profession = '"+ profession.getText() + "' where username = '" + userName3.getText() + "';")) != -1) {
				errorField2.setText("updated Sucessfully");
				new Registration().back(anchorPane2);
			} else {
				errorField2.setText("Unable to Update");
			}
		}
	}

	@FXML
	public void skip() throws Exception {
		new Registration().back(anchorPane2);
	}
}
