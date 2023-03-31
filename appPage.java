package application;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Observable;
import java.util.jar.Attributes.Name;

import javax.sound.midi.Soundbank;

import com.mysql.cj.x.protobuf.MysqlxNotice.SessionStateChanged.Parameter;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

class ButtonAction {
	ObservableList<HBox> messageHolder = FXCollections.observableArrayList();
	String receiverName;
	client client;
	VBox chatBox;
	TextField textField;
	ImageView backArrowImageView = new ImageView(new Image("D:\\java programming\\Application\\application\\backGoing image.png"));
	ImageView sendImageView = new ImageView(new Image("D:\\java programming\\Application\\application\\sendMessageIcon.jpg"));
	ImageView paperClipImageView = new ImageView(new Image("D:\\java programming\\Application\\application\\papercliproundcornerbackgroundicon.jpg"));;
	Label label = new Label();
	ScrollPane scrollPane;
	Image image;
	database database;
	AnchorPane anchorPane;

	public ButtonAction(String receiverName, client client, Image image ,database database,AnchorPane anchorPane) {
		this.receiverName = receiverName;
		this.client = client;
		this.image = image;
		this.database = database;
		this.anchorPane = anchorPane;
		chatBox = new VBox();
		scrollPane = new ScrollPane();
		chatBox.heightProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
				scrollPane.setVvalue((Double) arg2);
			}
		});
		
		chatBox.setStyle("-fx-background-color:white;");

		// ScrllPane for VBox
		scrollPane.setPannable(true);
		scrollPane.setLayoutX(226);
		scrollPane.setLayoutY(52);
		scrollPane.setPrefHeight(508);
		scrollPane.setPrefWidth(423);
		scrollPane.fitToHeightProperty().set(true);
		scrollPane.fitToWidthProperty().set(true);
		scrollPane.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.AS_NEEDED);
		scrollPane.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.AS_NEEDED);
		scrollPane.setContent(chatBox);
		scrollPane.setStyle("-fx-background-color:white;");
		
		// send image view
		sendImageView.setFitHeight(33.5);
		sendImageView.setFitWidth(40);
		sendImageView.setLayoutX(615);
		sendImageView.setLayoutY(560);
		sendImageView.setStyle("-fx-background-color:white;");
		sendImageView.setPreserveRatio(true);
		sendImageView.setSmooth(true);
		sendImageView.setCache(true);

		// textField for typing sending message
		textField = new TextField();
		textField.setPrefHeight(33.5);
		textField.setPrefWidth(357.5);
		textField.setLayoutX(257);
		textField.setLayoutY(560);
		textField.setPromptText("Type a message");
		textField.setFont(new Font(14));
		textField.setStyle("-fx-background-radius:0;"+"-fx-border-radius:0;"+"-fx-border-color:white;");

		Image[] imageArray = new Image[] {new Image("D:\\java programming\\Application\\application\\fileSendICon.png"),new Image("D:\\java programming\\Application\\application\\photoSendicon.png")};
		
		// clip image icon
		paperClipImageView.setFitHeight(41);
		paperClipImageView.setFitWidth(32);
		paperClipImageView.setLayoutX(226);
		paperClipImageView.setLayoutY(561);
		paperClipImageView.setPreserveRatio(true);
		paperClipImageView.setSmooth(true);
		paperClipImageView.setCache(true);
		paperClipImageView.setStyle("-fx-background-color:white;");
		paperClipImageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				ObservableList<String>  list = FXCollections.observableArrayList();
				list.addAll("File","Photos");
				ListView<String> listView = new ListView<>(list);
				listView.setPrefHeight(101);
				listView.setPrefWidth(113);
				listView.setLayoutX(226);
				listView.setLayoutY(458);
				listView.setCellFactory(Parameter -> new ListCell<String>() {
					ImageView imageView = new ImageView();
					@Override
					public void updateItem(String item, boolean empty) {
						super.updateItem(item, empty);
						if(empty) {
							setText(null);
							setGraphic(null);
						}
						else {
							if(item.equals("File")) {
								imageView.setImage(imageArray[0]);
								imageView.setFitHeight(24);
								imageView.setFitWidth(23);
							}
							else if(item.equals("Photos")) {
								imageView.setImage(imageArray[1]);
								imageView.setFitHeight(33);
								imageView.setFitWidth(28);
							}
							setText(item);
							setGraphic(imageView);
						}
					}
							
				});
				
				listView.setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						int index = listView.getSelectionModel().getSelectedIndex();
						switch (index) {
							case 0:{
								FileChooser fileChooser = new FileChooser();
								fileChooser.setTitle("Select file");
								fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Text Files", "*.txt"),new ExtensionFilter("Document", "*.doc"),new ExtensionFilter("PDF Files", "*.pdf"));
								File file = fileChooser.showOpenDialog((Stage)anchorPane.getScene().getWindow());
								if(file!=null) {
									try {
										FileSendIconToVbox(file);
										client.sendFile(receiverName,file,"***File***");
										System.out.println("File sent to method");
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
								break;
							}
							case 1:{
								System.out.println("Pictures");
								FileChooser fileChooser = new FileChooser();
								fileChooser.setTitle("Select file");
								fileChooser.getExtensionFilters().addAll(new ExtensionFilter("JPG Files", "*.jpg"),new ExtensionFilter("PNG Files", "*.png"),new ExtensionFilter("JPEG Files", "*.jpeg"));
								File file = fileChooser.showOpenDialog((Stage)anchorPane.getScene().getWindow());
								if(file != null) {
									try {
										addImageToVboxFromSender(file);
										client.sendFile(receiverName,file,"***Photo***");
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
								break;
							}
						
						}
						anchorPane.getChildren().remove(listView);
					}
				});
				
				anchorPane.getChildren().add(listView);
			}
		});

		label.setText(receiverName);
		ImageView imageView2 = new ImageView(image);
		imageView2.setFitHeight(45);
		imageView2.setFitWidth(45);
		imageView2.setPreserveRatio(true);
		imageView2.setSmooth(true);
		imageView2.setCache(true);
		label.setGraphic(imageView2);
		label.setPrefHeight(51);
		label.setPrefWidth(423.5);
		label.setLayoutX(226);
		label.setLayoutY(1);
		label.setFont(new Font(26));
		label.setStyle("-fx-background-color:white;"+"-fx-border-color:#C0C0C0;"+"-fx-border-width:1px;");
		label.setAlignment(Pos.BASELINE_LEFT);
		label.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				try {
					ResultSet resultSet = database.statement.executeQuery("select * from chatuser where userName = '"+receiverName+"';");
					resultSet.next();
					
					Label emailLabel = new Label(resultSet.getString("email"));
					emailLabel.setPrefHeight(33);
					emailLabel.setPrefWidth(320);
					emailLabel.setLayoutX(286);
					emailLabel.setLayoutY(197);
					emailLabel.setFont(new Font(20));
					emailLabel.setAlignment(Pos.CENTER);
					
					Label FNLabel = new Label("First Name");
					FNLabel.setPrefHeight(33);
					FNLabel.setPrefWidth(98);
					FNLabel.setLayoutX(241);
					FNLabel.setLayoutY(263);
					FNLabel.setFont(new Font(16));
					
					Label LNLabel = new Label("Last Name");
					LNLabel.setPrefHeight(33);
					LNLabel.setPrefWidth(98);
					LNLabel.setLayoutX(241);
					LNLabel.setLayoutY(307);
					LNLabel.setFont(new Font(16));;
					
					Label DOBLabel = new Label("Date Of Birth");
					DOBLabel.setPrefHeight(33);
					DOBLabel.setPrefWidth(98);
					DOBLabel.setLayoutX(241);
					DOBLabel.setLayoutY(353);
					DOBLabel.setFont(new Font(16));
					
					Label ProfLabel = new Label("Profession");
					ProfLabel.setPrefHeight(33);
					ProfLabel.setPrefWidth(98);
					ProfLabel.setLayoutX(241);
					ProfLabel.setLayoutY(403);
					ProfLabel.setFont(new Font(16));
					
					TextField addFNTextField = new TextField(resultSet.getString("firstname"));
					addFNTextField.setPrefHeight(33);
					addFNTextField.setPrefWidth(133);
					addFNTextField.setLayoutX(374);
					addFNTextField.setLayoutY(263);
					addFNTextField.setEditable(false);
					addFNTextField.setFont(new Font(16));
					
					TextField addLNTextField = new TextField(resultSet.getString("lastname"));
					addLNTextField.setPrefHeight(33);
					addLNTextField.setPrefWidth(133);
					addLNTextField.setLayoutX(374);
					addLNTextField.setLayoutY(307);
					addLNTextField.setEditable(false);
					addLNTextField.setFont(new Font(16));
					
					TextField addDOBTextField = new TextField(resultSet.getDate("dateofbirth").toString());
					addDOBTextField.setPrefHeight(33);
					addDOBTextField.setPrefWidth(133);
					addDOBTextField.setLayoutX(374);
					addDOBTextField.setLayoutY(353);
					addDOBTextField.setEditable(false);
					addDOBTextField.setFont(new Font(16));
					
					TextField addProfTextField = new TextField(resultSet.getString("profession"));
					addProfTextField.setPrefHeight(33);
					addProfTextField.setPrefWidth(133);
					addProfTextField.setLayoutX(374);
					addProfTextField.setLayoutY(403);
					addProfTextField.setEditable(false);
					addProfTextField.setFont(new Font(16));	
					
					ImageView profileImageView = new ImageView(image);
					profileImageView.setFitHeight(150);
					profileImageView.setFitWidth(200);
					profileImageView.setLayoutX(363);
					profileImageView.setLayoutY(21);
					profileImageView.setSmooth(true);
					profileImageView.setCache(true);
					profileImageView.setPreserveRatio(true);
					
					backArrowImageView.setFitHeight(33);
					backArrowImageView.setFitWidth(45);
					backArrowImageView.setLayoutX(226);
					backArrowImageView.setLayoutY(1);
					backArrowImageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
						@Override
						public void handle(MouseEvent event) {
							loadChattingPage();
						}
					});
					
					clearAnchorPane();
					anchorPane.getChildren().addAll(FNLabel,LNLabel,DOBLabel,ProfLabel,addDOBTextField,addFNTextField,addLNTextField,addProfTextField,emailLabel,profileImageView,backArrowImageView);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		} );
		
		this.sendImageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				String sendMessage = textField.getText();
				try {
					send(sendMessage);
				} catch (Exception e) {
					e.printStackTrace();
				}
				textField.clear();
			}
		});
	}

	public void clearAnchorPane() {
		if (anchorPane.getChildren().size() > 1) {
			anchorPane.getChildren().remove(1, anchorPane.getChildren().size());
		}
	}
	
	public void loadChattingPage() {
		clearAnchorPane();
		if (chatBox.getChildren().size() >= 1) {
			chatBox.getChildren().clear();
		}
		chatBox.getChildren().addAll(messageHolder);
		anchorPane.getChildren().addAll(scrollPane, sendImageView, paperClipImageView, label, textField);
	}
	
	public void send(String sendMessage) throws Exception {
		if(sendMessage.equals("")) {
			return;
		}
		HBox hBox = new HBox();
		hBox.setAlignment(Pos.CENTER_RIGHT);
		hBox.setPadding(new Insets(5, 5, 5, 10));
		Text text = new Text(sendMessage);
		text.setWrappingWidth(180);
		text.setFill(Color.BLACK);
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
		Text text2 = new Text("\n" + dateFormat.format(date));
		text2.setFill(Color.GRAY);
		text2.setFont(new Font(10));
		TextFlow textFlow = new TextFlow(text, text2);
		textFlow.setStyle("-fx-background-color:#ffffff;" + "-fx-background-radius:15;" + "-fx-border-radius:15;"+ "-fx-border-color:#C0C0C0;");
		textFlow.setPadding(new Insets(5, 10, 5, 10));
		hBox.getChildren().add(textFlow);
		messageHolder.add(hBox);
		chatBox.getChildren().add(hBox);
		client.writeMessageToServer(sendMessage, this.receiverName);
	}

	public void addMessageToVbox(String receivedmsg) {
		HBox hBox = new HBox();
		hBox.setAlignment(Pos.CENTER_LEFT);
		hBox.setPadding(new Insets(5, 5, 5, 10));
		Text text = new Text(receivedmsg);
		text.setWrappingWidth(180);
		text.setFill(Color.BLACK);
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
		Text text2 = new Text("\n" + dateFormat.format(date));
		text2.setFill(Color.GRAY);
		text2.setFont(new Font(10));
		TextFlow textFlow = new TextFlow(text, text2);
		textFlow.setStyle("-fx-background-color:#00bfff;" + "-fx-background-radius:15;" + "-fx-border-radius:15;"+ "-fx-border-color:#C0C0C0;");
		textFlow.setPadding(new Insets(5, 10, 5, 10));
		hBox.getChildren().add(textFlow);
		messageHolder.add(hBox);
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				chatBox.getChildren().add(hBox);
			}
		});
	}
	
	public void FileSendIconToVbox(File file) {
		HBox hBox = new HBox();
		hBox.setAlignment(Pos.CENTER_RIGHT);
		hBox.setPadding(new Insets(5,5,5,10));
		VBox vBox = new VBox();
		vBox.setPadding(new Insets(5,5,5,5));
		vBox.setSpacing(0);
		Label fileSendlabel = new Label(file.getName());
		ImageView fileImageView = new ImageView(new Image("D:\\java programming\\Application\\application\\fileimage.jpg"));
		fileImageView.setFitHeight(27);
		fileImageView.setFitWidth(26);
		fileImageView.setSmooth(true);
		fileImageView.setCache(true);
		fileImageView.setStyle("-fx-background-color:transparent;" + "-fx-background-radius:15;" + "-fx-border-radius:15;");
		fileImageView.setPreserveRatio(true);
		fileSendlabel.setGraphic(fileImageView);
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
		Text text2 = new Text("\n" + dateFormat.format(date));
		text2.setFill(Color.GRAY);
		text2.setFont(new Font(10));
		vBox.setAlignment(Pos.CENTER_LEFT);
		HBox  hBox2 = new HBox();
		hBox2.setAlignment(Pos.CENTER);
		hBox2.setPadding(new Insets(2));
		Button button = new Button("Open");
		button.setPrefWidth(hBox2.getPrefWidth());
		button.setStyle("-fx-background-color:#ffffff;" + "-fx-background-radius:8;" + "-fx-border-radius:8;"+ "-fx-border-color:#C0C0C0;");
		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				Desktop desktop = Desktop.getDesktop();
				try {
					desktop.open(file);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		hBox2.getChildren().add(button);
		vBox.setStyle("-fx-background-color:#ffffff;" + "-fx-background-radius:15;" + "-fx-border-radius:15;"+ "-fx-border-color:#C0C0C0;");
		vBox.getChildren().addAll(fileSendlabel,hBox2,text2);
		hBox.getChildren().add(vBox);
		messageHolder.add(hBox);
		chatBox.getChildren().add(hBox);
	}
	
	public void receiveFile(File file) throws IOException {
		HBox hBox = new HBox();
		hBox.setAlignment(Pos.CENTER_LEFT);
		hBox.setPadding(new Insets(5,5,5,10));
		VBox vBox = new VBox();
		vBox.setPadding(new Insets(5,5,5,5));
		vBox.setSpacing(0);
		Label filereceivelabel = new Label(file.getName());
		ImageView fileImageView = new ImageView(new Image("D:\\java programming\\Application\\application\\fileImage.jpg"));
		fileImageView.setFitHeight(27);
		fileImageView.setFitWidth(26);
		fileImageView.setSmooth(true);
		fileImageView.setCache(true);
		fileImageView.setStyle("-fx-background-color:#00bfff;" + "-fx-background-radius:15;" + "-fx-border-radius:15;");
		fileImageView.setPreserveRatio(true);
		filereceivelabel.setGraphic(fileImageView);
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
		Text text2 = new Text("\n" + dateFormat.format(date));
		text2.setFill(Color.GRAY);
		text2.setFont(new Font(10));
		vBox.setAlignment(Pos.CENTER_LEFT);
		vBox.setStyle("-fx-background-color:#00bfff;" + "-fx-background-radius:15;" + "-fx-border-radius:15;"+ "-fx-border-color:#C0C0C0;");
		HBox  hBox2 = new HBox();
		hBox2.setAlignment(Pos.CENTER);
		hBox2.setPadding(new Insets(2));
		Button button = new Button("Open");
		button.setPrefWidth(hBox2.getPrefWidth());
		button.setStyle("-fx-background-color:#ffffff;" + "-fx-background-radius:8;" + "-fx-border-radius:8;"+ "-fx-border-color:#C0C0C0;");
		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				Desktop desktop = Desktop.getDesktop();
				try {
					desktop.open(file);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		hBox2.getChildren().add(button);
		vBox.getChildren().addAll(filereceivelabel,hBox2,text2);
		hBox.getChildren().add(vBox);
		messageHolder.add(hBox);
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				chatBox.getChildren().add(hBox);
			}
		});
		
	}
	
	public void addImageToVboxFromSender(File file) throws FileNotFoundException {
		HBox hBox = new HBox();
		hBox.setAlignment(Pos.CENTER_RIGHT);
		hBox.setPadding(new Insets(5, 5, 5, 10));
		Image image = new Image(new FileInputStream(file));
		ImageView imageView = new ImageView(image);
		imageView.setFitHeight(168);
		imageView.setFitWidth(177);
		imageView.setSmooth(true);
		imageView.setCache(true);
		imageView.setPreserveRatio(true);
		imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				Desktop desktop = Desktop.getDesktop();
				try {
					desktop.open(file);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		Pane pane = new Pane();	
		pane.setStyle("-fx-border-color:#00bfff;" +"-fx-border-style: solid;"+"-fx-border-width: 3;");	
		pane.getChildren().add(imageView);
		hBox.getChildren().add(pane);
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				chatBox.getChildren().add(hBox);
			}
		});
	}
	
	public void addImageToVboxFromreceiver(File file) throws FileNotFoundException {
		HBox hBox = new HBox();
		hBox.setAlignment(Pos.CENTER_LEFT);
		hBox.setPadding(new Insets(5, 5, 5, 10));
		Image image = new Image(new FileInputStream(file));
		ImageView imageView = new ImageView(image);
		imageView.setFitHeight(168);
		imageView.setFitWidth(177);
		imageView.setSmooth(true);
		imageView.setCache(true);
		imageView.setPreserveRatio(true);
		imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				Desktop desktop = Desktop.getDesktop();
				try {
					desktop.open(file);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		Pane pane = new Pane();	
		pane.setStyle("-fx-border-color:#00bfff;" +"-fx-border-style: solid;"+"-fx-border-width: 3;");	
		pane.getChildren().add(imageView);
		hBox.getChildren().add(pane);
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				chatBox.getChildren().add(hBox);
			}
		});
	}
	
}

public class appPage {
	database database;
	static String userName;
	client client;
	ArrayList<Button> buttons ;
	HashMap<String, Button> receiverButton;
	HashMap<Button, ButtonAction> receiverButtonAction;
	public appPage(String userName) {
		appPage.userName = userName;
	}

	public appPage() {
		database = new database();
		client = new client();
		buttons = new ArrayList<Button>();
		receiverButton = new HashMap<String,Button>();
		receiverButtonAction = new HashMap<Button,ButtonAction>();
		client.readMessageFromServer(this);
	}

	@FXML
	VBox connectedUsersVBox;
	@FXML
	AnchorPane anchorPane2;
	@FXML
	VBox usersVBox;
	@FXML
	Label profileLabel;
	@FXML
	AnchorPane anchorPaneUsers;

	@FXML
	public void loadButtons() throws Exception {
		connectedUsersVBox.getChildren().clear();
		ResultSet resultSet = database.statement.executeQuery("select * from chatuser where isOnline = 'true';");
		while (resultSet.next()) {
			String receiverName = resultSet.getString("username");
			if (receiverName.equals(appPage.userName)) {
				continue;
			}
			InputStream inputStream = resultSet.getBinaryStream("profilePicture");
			Image image = new Image(inputStream);
			createButton(receiverName, image);
		}
		connectedUsersVBox.getChildren().addAll(buttons);
	}

	public void createButton(String receiverName,Image image) {
		for(Button b : buttons) {
			if(b.getText().equals(receiverName)) {
				return;
			}
		}
		Button button = new Button(receiverName);
		button.setPrefWidth(usersVBox.getPrefWidth());
		ImageView imageView = new ImageView(image);
		imageView.setFitHeight(32);
		imageView.setFitWidth(32);
		imageView.setPreserveRatio(true);
		imageView.setSmooth(true);
		imageView.setCache(true);
		button.setGraphic(imageView);
		button.setAlignment(Pos.BASELINE_LEFT);
		button.setPrefHeight(30);
		receiverButton.put(receiverName,button);
		ButtonAction buttonAction = new ButtonAction(receiverName, client, image ,database ,anchorPane2);
		receiverButtonAction.put(button, buttonAction);
		button.setStyle("-fx-border-radius:0;"+"-fx-background-radius:0;"+"-fx-background-color:#ffffff;"+"-fx-font-size:20;");
		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				for(Button b:buttons) {
					b.setStyle("-fx-border-radius:0;"+"-fx-background-radius:0;"+"-fx-background-color:#ffffff;"+"-fx-font-size:20;");
				}
				button.requestFocus();
				button.setStyle("-fx-border-radius:0;"+"-fx-background-radius:0;"+"-fx-background-color:#C0C0C0;"+"-fx-font-size:26;");
				buttonAction.loadChattingPage();
			}
		});
		button.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				for(Button b:buttons) {
					b.setStyle("-fx-border-radius:0;"+"-fx-background-radius:0;"+"-fx-background-color:#ffffff;"+"-fx-font-size:20;");
				}
				button.setStyle("-fx-border-radius:0;"+"-fx-background-radius:0;"+"-fx-background-color:#C0C0C0;"+"-fx-font-size:26;");
			}
		});
		button.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if(!button.isFocused()) {
					button.setStyle("-fx-border-radius:0;"+"-fx-background-radius:0;"+"-fx-background-color:#ffffff;"+"-fx-font-size:20;");
				}
			}
		});
		buttons.add(button);
	}
	
	public void receiveMsg(String senderName,String msg) throws Exception {
		Button button;
		System.out.println(senderName);
		System.out.println(msg);
		if((button = receiverButton.get(senderName)) == null) {
			System.out.println("no receiver");
		}
		ButtonAction buttonAction = receiverButtonAction.get(button);	
		buttonAction.addMessageToVbox(msg);
	}
	
	public void receiveMsg(String senderName,File file) throws Exception {
		Button button;
		System.out.println(senderName);
		if((button = receiverButton.get(senderName)) == null) {
			System.out.println("no receiver");
		}
		ButtonAction buttonAction = receiverButtonAction.get(button);
		buttonAction.receiveFile(file);
	}
	
	public void addImageToVbox(String senderName, File file) throws FileNotFoundException {
		Button button;
		System.out.println(senderName);
		if((button = receiverButton.get(senderName)) == null) {
			System.out.println("no receiver");
		}
		ButtonAction buttonAction = receiverButtonAction.get(button);
		buttonAction.addImageToVboxFromreceiver(file);
	}
	
	public void apPage(AnchorPane anchorPane) throws Exception {
		URL url = new File("D:\\java programming\\Application\\application\\AppPage.fxml").toURI().toURL();
		Parent root = FXMLLoader.load(url);
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		Stage primaryStage = (Stage) anchorPane.getScene().getWindow();
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	@FXML
	void initialize() {
		try {
			profileLabel.setText(appPage.userName);
			ResultSet resultSet = database.statement.executeQuery("select * from chatuser");
			ArrayList<Button> buttonsList = new ArrayList<Button>();
			while (resultSet.next()) {
				String userName = resultSet.getString("username");
				if (userName.equals(appPage.userName)) {
					InputStream inputStream = resultSet.getBinaryStream("profilePicture");
					Image image = new Image(inputStream);
					ImageView imageView2 = new ImageView(image);
					imageView2.setFitHeight(45);
					imageView2.setFitWidth(45);
					imageView2.setPreserveRatio(true);
					imageView2.setSmooth(true);
					imageView2.setCache(true);
					profileLabel.setGraphic(imageView2);
					continue;
				}
				Button button = new Button(userName);
				ImageView imageView = new ImageView(new Image(resultSet.getBinaryStream("profilePicture")));
				imageView.setFitHeight(29);
				imageView.setFitWidth(29);
				imageView.setPreserveRatio(true);
				imageView.setSmooth(true);
				imageView.setCache(true);
				button.setGraphic(imageView);
				button.setStyle("-fx-border-radius:0;"+"-fx-background-radius:0;"+"-fx-background-color:#ffffff;"+"-fx-font-size:18;");
				button.setAlignment(Pos.BASELINE_LEFT);
				button.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						for(Button b:buttons) {
							b.setStyle("-fx-border-radius:0;"+"-fx-background-radius:0;"+"-fx-background-color:#ffffff;"+"-fx-font-size:20;");
						}
						for(Button b: buttonsList) {
							b.setStyle("-fx-border-radius:0;"+"-fx-background-radius:0;"+"-fx-background-color:#ffffff;"+"-fx-font-size:20;");
						}
						button.setStyle("-fx-border-radius:0;"+"-fx-background-radius:0;"+"-fx-background-color:#C0C0C0;"+"-fx-font-size:26;");
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Warning Message");
						alert.setHeaderText(null);
						alert.setContentText("You can only chat with the Online users");
						alert.showAndWait();
					}
				});
				button.setOnMouseEntered(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						button.setStyle("-fx-border-radius:0;"+"-fx-background-radius:0;"+"-fx-background-color:#C0C0C0;"+"-fx-font-size:26;");
					}
				});
				button.setOnMouseExited(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						button.setStyle("-fx-border-radius:0;"+"-fx-background-radius:0;"+"-fx-background-color:#ffffff;"+"-fx-font-size:20;");
					}
				});
				button.setPrefWidth(usersVBox.getPrefWidth());
				button.setPrefHeight(30);
				buttonsList.add(button);
			}
			usersVBox.getChildren().addAll(buttonsList);
			loadButtons();
			Text text = new Text("WelCome To My Chat Application");
			text.setFont(new Font(22));
			text.setLayoutX(258);
			text.setLayoutY(234);
			connectedUsersVBox.setStyle("-fx-background-color:#ffffff;");
			usersVBox.setStyle("-fx-background-color:#ffffff;");
			anchorPaneUsers.setStyle("-fx-border-color:#C0C0C0;"+"-fx-border-width:1px 1px 1px 1px;"+"-fx-background-color:#ffffff;");
			anchorPane2.setStyle("-fx-border-color:#C0C0C0;"+"-fx-background-color:#ffffff;"+"-fx-border-width:1px 1px 1px 1px;");
			anchorPane2.getChildren().add(text);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void editProfile() throws Exception {
		ResultSet resultSet = database.statement.executeQuery("select * from chatuser where userName = '"+userName+"';");
		resultSet.next();
		
		ImageView backArrowImageView = new ImageView(new Image("D:\\java programming\\Application\\application\\backGoing image.png"));
		backArrowImageView.setFitHeight(33);
		backArrowImageView.setFitWidth(45);
		backArrowImageView.setLayoutX(226);
		backArrowImageView.setLayoutY(1);
		backArrowImageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (anchorPane2.getChildren().size() > 1) {
					anchorPane2.getChildren().remove(1, anchorPane2.getChildren().size());
				}
			}
		});
		
		Label emailLabel = new Label(resultSet.getString("email"));
		emailLabel.setPrefHeight(33);
		emailLabel.setPrefWidth(320);
		emailLabel.setLayoutX(286);
		emailLabel.setLayoutY(197);
		emailLabel.setFont(new Font(20));
		emailLabel.setAlignment(Pos.CENTER);
		
		Label FNLabel = new Label("First Name");
		FNLabel.setPrefHeight(33);
		FNLabel.setPrefWidth(98);
		FNLabel.setLayoutX(241);
		FNLabel.setLayoutY(263);
		FNLabel.setFont(new Font(16));
		
		Label LNLabel = new Label("Last Name");
		LNLabel.setPrefHeight(33);
		LNLabel.setPrefWidth(98);
		LNLabel.setLayoutX(241);
		LNLabel.setLayoutY(307);
		LNLabel.setFont(new Font(16));;
		
		Label DOBLabel = new Label("Date Of Birth");
		DOBLabel.setPrefHeight(33);
		DOBLabel.setPrefWidth(98);
		DOBLabel.setLayoutX(241);
		DOBLabel.setLayoutY(353);
		DOBLabel.setFont(new Font(16));
		
		Label ProfLabel = new Label("Profession");
		ProfLabel.setPrefHeight(33);
		ProfLabel.setPrefWidth(98);
		ProfLabel.setLayoutX(241);
		ProfLabel.setLayoutY(403);
		ProfLabel.setFont(new Font(16));
		
		TextField addFNTextField = new TextField(resultSet.getString("firstname"));
		addFNTextField.setPrefHeight(33);
		addFNTextField.setPrefWidth(133);
		addFNTextField.setLayoutX(374);
		addFNTextField.setLayoutY(263);
		addFNTextField.setEditable(false);
		addFNTextField.setFont(new Font(16));
		
		TextField addLNTextField = new TextField(resultSet.getString("lastname"));
		addLNTextField.setPrefHeight(33);
		addLNTextField.setPrefWidth(133);
		addLNTextField.setLayoutX(374);
		addLNTextField.setLayoutY(307);
		addLNTextField.setEditable(false);
		addLNTextField.setFont(new Font(16));
		
		TextField addDOBTextField = new TextField(resultSet.getDate("dateofbirth").toString());
		addDOBTextField.setPrefHeight(33);
		addDOBTextField.setPrefWidth(133);
		addDOBTextField.setLayoutX(374);
		addDOBTextField.setLayoutY(353);
		addDOBTextField.setEditable(false);
		addDOBTextField.setFont(new Font(16));
		
		TextField addProfTextField = new TextField(resultSet.getString("profession"));
		addProfTextField.setPrefHeight(33);
		addProfTextField.setPrefWidth(133);
		addProfTextField.setLayoutX(374);
		addProfTextField.setLayoutY(403);
		addProfTextField.setEditable(false);
		addProfTextField.setFont(new Font(16));
		
		Image editIconImage = new Image("D:\\java programming\\Application\\application\\edit icon.png");
		
		ImageView editFNImageView = new ImageView(editIconImage);
		editFNImageView.setFitHeight(33);
		editFNImageView.setFitWidth(39);
		editFNImageView.setLayoutX(557);
		editFNImageView.setLayoutY(262);
		editFNImageView.setPreserveRatio(true);
		editFNImageView.setSmooth(true);
		editFNImageView.setCache(true);
		editFNImageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				addFNTextField.clear();
				addFNTextField.setEditable(true);
				anchorPane2.getChildren().remove(editFNImageView);
				addFNTextField.setPromptText("Enter first Name");
				Button enterButton = new Button("Enter");
				enterButton.setLayoutX(540);
				enterButton.setLayoutY(267);
				enterButton.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent arg0) {
						try {
							if((database.statement.executeUpdate("update chatuser set firstname = '"+addFNTextField.getText()+"' where username = '"+userName+"';")) != -1){
								addFNTextField.setEditable(false);
								anchorPane2.getChildren().remove(enterButton);
								anchorPane2.getChildren().add(editFNImageView);
							}
						} catch (SQLException e) {
							e.printStackTrace();
						}
								
					}
				});
				anchorPane2.getChildren().add(enterButton);
			}
		});
		
		ImageView editLNImageView = new ImageView(editIconImage);
		editLNImageView.setFitHeight(33);
		editLNImageView.setFitWidth(39);
		editLNImageView.setLayoutX(557);
		editLNImageView.setLayoutY(307);
		editLNImageView.setPreserveRatio(true);
		editLNImageView.setSmooth(true);
		editLNImageView.setCache(true);
		editLNImageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				addLNTextField.clear();
				addLNTextField.setEditable(true);
				anchorPane2.getChildren().remove(editLNImageView);
				addLNTextField.setPromptText("Enter last Name");
				Button enterButton = new Button("Enter");
				enterButton.setLayoutX(540);
				enterButton.setLayoutY(311);
				enterButton.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent arg0) {
						try {
							if((database.statement.executeUpdate("update chatuser set lastname = '"+addLNTextField.getText()+"' where username = '"+userName+"';")) != -1){
								addLNTextField.setEditable(false);
								anchorPane2.getChildren().remove(enterButton);
								anchorPane2.getChildren().add(editLNImageView);
							}
						} catch (SQLException e) {
							e.printStackTrace();
						}
								
					}
				});
				anchorPane2.getChildren().add(enterButton);
			}
		});
		
		ImageView editDOBImageView = new ImageView(editIconImage);
		editDOBImageView.setFitHeight(33);
		editDOBImageView.setFitWidth(39);
		editDOBImageView.setLayoutX(557);
		editDOBImageView.setLayoutY(353);
		editDOBImageView.setPreserveRatio(true);
		editDOBImageView.setSmooth(true);
		editDOBImageView.setCache(true);
		editDOBImageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				addDOBTextField.clear();
				addDOBTextField.setEditable(true);
				anchorPane2.getChildren().remove(editDOBImageView);
				addDOBTextField.setPromptText("yyyy-mm-dd");
				Button enterButton = new Button("Enter");
				enterButton.setLayoutX(540);
				enterButton.setLayoutY(357);
				enterButton.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent arg0) {
						try {
							if((database.statement.executeUpdate("update chatuser set dateofbirth = '"+java.sql.Date.valueOf(addDOBTextField.getText())+"' where username = '"+userName+"';")) != -1){
								addDOBTextField.setEditable(false);
								anchorPane2.getChildren().remove(enterButton);
								anchorPane2.getChildren().add(editDOBImageView);
							}
						} catch (SQLException e) {
							e.printStackTrace();
						}
								
					}
				});
				anchorPane2.getChildren().add(enterButton);
			}
		});
		
		ImageView editProfImageView = new ImageView(editIconImage);
		editProfImageView.setFitHeight(33);
		editProfImageView.setFitWidth(39);
		editProfImageView.setLayoutX(557);
		editProfImageView.setLayoutY(403);
		editProfImageView.setPreserveRatio(true);
		editProfImageView.setSmooth(true);
		editProfImageView.setCache(true);
		editProfImageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				addProfTextField.clear();
				addProfTextField.setEditable(true);
				anchorPane2.getChildren().remove(editProfImageView);
				addProfTextField.setPromptText("Enter Profession");
				Button enterButton = new Button("Enter");
				enterButton.setLayoutX(540);
				enterButton.setLayoutY(407);
				enterButton.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent arg0) {
						try {
							if((database.statement.executeUpdate("update chatuser set Profession = '"+addProfTextField.getText()+"' where username = '"+userName+"';")) != -1){
								addProfTextField.setEditable(false);
								anchorPane2.getChildren().remove(enterButton);
								anchorPane2.getChildren().add(editProfImageView);
							}
						} catch (SQLException e) {
							e.printStackTrace();
						}
								
					}
				});
				anchorPane2.getChildren().add(enterButton);
			}
		});
		
		InputStream inputStream = resultSet.getBinaryStream("profilePicture");
		Image profileImage = new Image(inputStream);
		ImageView profileImageView = new ImageView(profileImage);
		profileImageView.setFitHeight(150);
		profileImageView.setFitWidth(200);
		profileImageView.setLayoutX(363);
		profileImageView.setLayoutY(21);
		profileImageView.setSmooth(true);
		profileImageView.setCache(true);
		profileImageView.setPreserveRatio(true);
		profileImageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				FileChooser profileImageChooser = new FileChooser();
				profileImageChooser.setTitle("Choose Image");
				File profileImageFile = profileImageChooser.showOpenDialog((Stage)anchorPane2.getScene().getWindow()); 
				try {
					database.connection.setAutoCommit(false);
					if(profileImageFile != null) {
						InputStream imageInputStream = new FileInputStream(profileImageFile);
						database.preparedStatement = database.connection.prepareStatement("update chatuser set profilepicture = (?) where username = '"+userName+"';");
						database.preparedStatement.setBinaryStream(1, imageInputStream);
						database.preparedStatement.execute();
						System.out.println("Sucess");
						editProfile();
					}
				}
				catch (Exception e) {
					try {
						database.connection.rollback();
						database.connection.setAutoCommit(true);
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					
					e.printStackTrace();
				}
			}
		});
		
		if (anchorPane2.getChildren().size() > 1) {
			System.out.println("Clearing Anchorpane");
			anchorPane2.getChildren().remove(1, anchorPane2.getChildren().size());
		}
		anchorPane2.getChildren().addAll(FNLabel,LNLabel,DOBLabel,ProfLabel,addDOBTextField,addFNTextField,addLNTextField,addProfTextField,editDOBImageView,editFNImageView,editLNImageView,editProfImageView,emailLabel,profileImageView,backArrowImageView);
	}
}
