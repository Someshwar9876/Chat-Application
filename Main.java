package application;

import java.io.File;
import java.net.URL;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;

public class Main extends Application {
	public void start(Stage primaryStage) {
		try {
			URL url = new File("D:\\java programming\\Application\\application\\login_page.fxml").toURI().toURL();
			Parent root = FXMLLoader.load(url);
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			primaryStage.getIcons().add(new Image("D:\\java programming\\Application\\application\\appIcon.jpg"));
			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent arg0) {
					Platform.exit();
					System.exit(0);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void stop() throws Exception {
		super.stop();
	}

	public static void main(String[] args) throws Exception {
		launch(args);
	}
}