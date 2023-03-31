package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class database {
	Connection connection;
	Statement statement;
	PreparedStatement preparedStatement;

	public database() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/project", "root", "Somu@2001");
			statement = connection.createStatement();
			System.out.println("Connection Established");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

