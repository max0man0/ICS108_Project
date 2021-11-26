import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;

public class Interface extends Application {
	private Stage primaryStage; // The primary (main) window
	private Scene scene; // The scene shown in the primaryStage
	
	// sceneWidth and sceneHeight are used to maintain the dimensions of the window
	// when changing between different scenes 
	private double sceneWidth = 600;
	private double sceneHeight = 400;
	
	private int currentIndex; // The index of the question that is currently selected
	private ArrayList<Question> qArray = new ArrayList<>(0); // ArrayList containing the questions read from the file
	
	// Change the scene to showing the main menu
	public void showMainMenu() {
		// This pane contains the ListView and the button pane
		BorderPane primaryPane = new BorderPane();
		scene = new Scene(primaryPane, sceneWidth, sceneHeight);
		
		Button btCreate = new Button("Create");
		Button btEdit = new Button("Edit");
		Button btView = new Button("View");
		Button btDelete = new Button("Delete");
		
		// This pane will contain all of the buttons of the main menu
		HBox buttonPane = new HBox(30);
		buttonPane.setAlignment(Pos.CENTER);
		buttonPane.setPadding(new Insets(5,5,5,5));
		buttonPane.getChildren().addAll(btCreate, btEdit, btView, btDelete);
		primaryPane.setBottom(buttonPane);
		
		// A ListView object that contains all of the questions in qArray
		ListView<Question> lvQuestions = new ListView<>(FXCollections.observableArrayList(qArray));
		lvQuestions.setPrefHeight(250);
		primaryPane.setCenter(lvQuestions);
		
		// Go to the edit menu of the question in the ListView that is double clicked
		lvQuestions.setOnMouseClicked(e -> {
			if (lvQuestions.getSelectionModel().getSelectedIndex() != -1 && e.getButton().equals(MouseButton.PRIMARY) && e.getClickCount() == 2) {
				sceneWidth = scene.getWidth();
				sceneHeight = scene.getHeight();
				currentIndex = lvQuestions.getSelectionModel().getSelectedIndex();
				showEditInterface();
			}
		});
		
		// Go to the create menu after clicking the create button
		btCreate.setOnAction(e -> {
			sceneWidth = scene.getWidth();
			sceneHeight = scene.getHeight();
			showCreateInterface();
		});
		
		// Go to the Edit menu of the selected question after clicking the edit button
		btEdit.setOnAction(e -> {
			if(lvQuestions.getSelectionModel().getSelectedIndex() != -1) {
				sceneWidth = scene.getWidth();
				sceneHeight = scene.getHeight();
				currentIndex = lvQuestions.getSelectionModel().getSelectedIndex();
				showEditInterface();
			}
		});
		
		// Go to the View menu of the selected question after clicking the view button
		btView.setOnAction(e -> {
			if(lvQuestions.getSelectionModel().getSelectedIndex() != -1) {
				sceneWidth = scene.getWidth();
				sceneHeight = scene.getHeight();
				currentIndex = lvQuestions.getSelectionModel().getSelectedIndex();
				showViewInterface();
			}
		});
		
		// Delete the selected question after taking confirmation from the user
		btDelete.setOnAction(e -> {
			if(lvQuestions.getSelectionModel().getSelectedIndex() != -1) {
				deleteWindow(lvQuestions.getSelectionModel().getSelectedIndex(), lvQuestions);
			}
		});
		
		primaryStage.setTitle("MainMenu");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	// Change the scene to showing the view interface
	public void showViewInterface() {
		// This pane contains the headerPane, questionPane and buttonPane
		BorderPane primaryPane = new BorderPane();
		scene = new Scene(primaryPane, sceneWidth, sceneHeight);
		
		Button btPrevious = new Button("Previous");
		Button btNext = new Button("Next");
		
		// This pane will contains the next and previous buttons
		HBox buttonPane = new HBox(20);
		buttonPane.setAlignment(Pos.CENTER);
		buttonPane.getChildren().addAll(btPrevious, btNext);
		
		
		Label lblQuestionText = new Label(qArray.get(currentIndex).getQuestionText());
		lblQuestionText.setWrapText(true);
		lblQuestionText.setFont(Font.font(17));
		
		Text txtChoice1 = new Text("A. " + qArray.get(currentIndex).getChoices()[0]); txtChoice1.setFont(Font.font(15));
		Text txtChoice2 = new Text("B. " + qArray.get(currentIndex).getChoices()[1]); txtChoice2.setFont(Font.font(15));
		Text txtChoice3 = new Text("C. " + qArray.get(currentIndex).getChoices()[2]); txtChoice3.setFont(Font.font(15));
		Text txtChoice4 = new Text("D. " + qArray.get(currentIndex).getChoices()[3]); txtChoice4.setFont(Font.font(15));
		
		VBox choicesPane = new VBox(10);
		choicesPane.getChildren().addAll(txtChoice1, txtChoice2, txtChoice3, txtChoice4);
		
		// This pane contains the text of the question and its choices
		VBox questionPane = new VBox(30);
		questionPane.getChildren().addAll(lblQuestionText, choicesPane);
		questionPane.setPadding(new Insets(0, 50, 0, 50));
		questionPane.setAlignment(Pos.CENTER_LEFT);
		
		// Go to the main menu after clicking the back button
		Button btBack = new Button("Back");
		btBack.setOnAction(e -> {
			sceneWidth = scene.getWidth();
			sceneHeight = scene.getHeight();
			showMainMenu();
		});
		
		// Text that shows the position of the current question
		Label lblQuestionNumber = new Label((currentIndex+1) + " out of " + qArray.size());
		
		// This pane contains the back button and the position of the current question
		BorderPane headerPane = new BorderPane();
		headerPane.setPadding(new Insets(5,5,5,5));
		headerPane.setLeft(btBack);
		headerPane.setRight(lblQuestionNumber);
		
		// Color the correct choice green the first time the interface is shown
		switch(qArray.get(currentIndex).getCorrectChoiceIndex()) {
		case 0:
			txtChoice1.setFill(Color.GREEN);
			break;
		case 1:
			txtChoice2.setFill(Color.GREEN);
			break;
		case 2:
			txtChoice3.setFill(Color.GREEN);
			break;
		case 3:
			txtChoice4.setFill(Color.GREEN);
			break;
		}
		
		// Handler for next and previous buttons to navigate between the questions
		EventHandler<ActionEvent> navigate = e -> {
			
			// Change the current index based on the clicked button
			// Increment for next and decrement for previous  
			int delta = (( (Button)e.getSource() ).getText() == "Previous")? -1 : 1;
			currentIndex += delta;
			
			// Undo the previous change if the current index is at the boundaries of the qArray
			if(currentIndex < 0 || currentIndex > qArray.size() - 1) {
				currentIndex -= delta;
				return;
			}
			
			// Update the label of the current position
			lblQuestionNumber.setText((currentIndex+1) + " out of " + qArray.size());
			
			// Update the question and choices' text
			lblQuestionText.setText(qArray.get(currentIndex).getQuestionText());
			txtChoice1.setText("A. " + qArray.get(currentIndex).getChoices()[0]);
			txtChoice2.setText("B. " + qArray.get(currentIndex).getChoices()[1]);
			txtChoice3.setText("C. " + qArray.get(currentIndex).getChoices()[2]);
			txtChoice4.setText("D. " + qArray.get(currentIndex).getChoices()[3]);
			
			
			// Color the correct choice in green and the rest in black
			if(qArray.get(currentIndex).getCorrectChoiceIndex() == 0)
				txtChoice1.setFill(Color.GREEN);
			else
				txtChoice1.setFill(Color.BLACK);
			
			if(qArray.get(currentIndex).getCorrectChoiceIndex() == 1)
				txtChoice2.setFill(Color.GREEN);
			else
				txtChoice2.setFill(Color.BLACK);
			
			if(qArray.get(currentIndex).getCorrectChoiceIndex() == 2)
				txtChoice3.setFill(Color.GREEN);
			else
				txtChoice3.setFill(Color.BLACK);
			
			if(qArray.get(currentIndex).getCorrectChoiceIndex() == 3)
				txtChoice4.setFill(Color.GREEN);
			else
				txtChoice4.setFill(Color.BLACK);
		};
		
		btPrevious.setOnAction(navigate);
		btNext.setOnAction(navigate);
		
		primaryPane.setBottom(buttonPane);
		primaryPane.setCenter(questionPane);
		primaryPane.setTop(headerPane);
		BorderPane.setMargin(buttonPane, new Insets(5, 5, 5, 5));
		BorderPane.setAlignment(lblQuestionNumber, Pos.CENTER_RIGHT);
		
		primaryStage.setTitle("View");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	// Change the scene to showing the create interface
	public void showCreateInterface() {
		// This pane contains the back button, questionPane and the create button
		BorderPane primaryPane = new BorderPane();
		
		scene = new Scene(primaryPane, sceneWidth, sceneHeight);
		
		TextField tfQuestionText = new TextField();
		tfQuestionText.setPromptText("Question");
		tfQuestionText.setFont(Font.font(17));
		
		TextField tfChoice1 = new TextField(); tfChoice1.setPromptText("Choice 1"); tfChoice1.setFont(Font.font(15));
		TextField tfChoice2 = new TextField(); tfChoice2.setPromptText("Choice 2"); tfChoice2.setFont(Font.font(15));
		TextField tfChoice3 = new TextField(); tfChoice3.setPromptText("Choice 3"); tfChoice3.setFont(Font.font(15));
		TextField tfChoice4 = new TextField(); tfChoice4.setPromptText("Choice 4"); tfChoice4.setFont(Font.font(15));
		
		// This pane contains the choices' text fields
		VBox tfChoicesPane = new VBox(14.5);
		tfChoicesPane.getChildren().addAll(tfChoice1, tfChoice2, tfChoice3, tfChoice4);
		
		ToggleGroup group = new ToggleGroup();
		RadioButton rbChoice1 = new RadioButton("A. "); rbChoice1.setToggleGroup(group); rbChoice1.setFont(tfChoice1.getFont());
		RadioButton rbChoice2 = new RadioButton("B. "); rbChoice2.setToggleGroup(group); rbChoice2.setFont(tfChoice2.getFont());
		RadioButton rbChoice3 = new RadioButton("C. "); rbChoice3.setToggleGroup(group); rbChoice3.setFont(tfChoice3.getFont());
		RadioButton rbChoice4 = new RadioButton("D. "); rbChoice4.setToggleGroup(group); rbChoice4.setFont(tfChoice4.getFont());
		
		// Setting IDs for the radio buttons to get the correct choice index in the create button handler
		rbChoice1.setId("0");
		rbChoice2.setId("1");
		rbChoice3.setId("2");
		rbChoice4.setId("3");
		
		// Handler that colors the choice selected by the radio button in green and the rest in black
		EventHandler<ActionEvent> changeLabelColor = e -> {
			if(rbChoice1.isSelected())
				tfChoice1.setStyle("-fx-text-fill: green");
			else
				tfChoice1.setStyle("-fx-text-fill: black");
			
			if(rbChoice2.isSelected())
				tfChoice2.setStyle("-fx-text-fill: green");
			else
				tfChoice2.setStyle("-fx-text-fill: black");
			
			if(rbChoice3.isSelected())
				tfChoice3.setStyle("-fx-text-fill: green");
			else
				tfChoice3.setStyle("-fx-text-fill: black");
			
			if(rbChoice4.isSelected())
				tfChoice4.setStyle("-fx-text-fill: green");
			else
				tfChoice4.setStyle("-fx-text-fill: black");
		};
		
		rbChoice1.setOnAction(changeLabelColor);
		rbChoice2.setOnAction(changeLabelColor);
		rbChoice3.setOnAction(changeLabelColor);
		rbChoice4.setOnAction(changeLabelColor);
		
		// This pane contains the radio buttons used to select the correct choice
		VBox radioButtonPane = new VBox(24);
		radioButtonPane.getChildren().addAll(rbChoice1, rbChoice2, rbChoice3, rbChoice4);
		radioButtonPane.setPadding(new Insets(6, 6, 0, 0));
		
		// this pane contains the pane containing the choices' text fields and the radio button pane
		BorderPane choicesPane = new BorderPane();
		choicesPane.setLeft(radioButtonPane);
		choicesPane.setCenter(tfChoicesPane);
		
		// This pane contains choicesPane and the question text field
		VBox questionPane = new VBox(30);
		questionPane.getChildren().addAll(tfQuestionText, choicesPane);
		questionPane.setPadding(new Insets(0, 50, 0, 50));
		questionPane.setAlignment(Pos.CENTER_LEFT);
		
		// Go to the main menu when the back button is clicked
		Button btBack = new Button("Back");
		btBack.setOnAction(e -> {
			sceneWidth = scene.getWidth();
			sceneHeight = scene.getHeight();
			showMainMenu();
		});
		
		// Create button with the string "Select the correct choice" above it
		Button btCreate = new Button("Create");
		Label lblInstruction = new Label("Select the correct choice", btCreate);
		lblInstruction.setContentDisplay(ContentDisplay.BOTTOM);
		lblInstruction.setFont(Font.font(13));
		
		// Handler that adds the created question to qArray if all requirements are met
		btCreate.setOnAction(e -> {
			// If any text field is empty show a warning pop-up window
			if (tfChoice1.getText().equals("") || tfChoice2.getText().equals("") || tfChoice3.getText().equals("") ||
					tfChoice4.getText().equals("") || tfQuestionText.getText().equals("")){
				warningWindow("Please fill all the text fields", "Creation Failed");
			}
			// If there is no radio button selected show a warning pop-up window
			else if(group.getSelectedToggle() == null) {
				warningWindow("Please select the correct choice using the corresponding button", "Creation Failed");
			}
			// Create the question and add it to qArray then go to the main menu
			else {
				String[] choices = {tfChoice1.getText(), tfChoice2.getText(), tfChoice3.getText(), tfChoice4.getText()};
				// Get the selected radio button ID and convert it to integer to get the correct choice index 
				int correctChoiceIndex = Integer.parseInt( ((RadioButton)group.getSelectedToggle()).getId() );
				Question newQuestion = new Question(tfQuestionText.getText(), choices, correctChoiceIndex);
				qArray.add(newQuestion);
				showMainMenu();
			}
		});
		
		primaryPane.setTop(btBack);
		primaryPane.setCenter(questionPane);
		primaryPane.setBottom(lblInstruction);
		BorderPane.setMargin(btBack, new Insets(5, 0, 0, 5));
		BorderPane.setMargin(lblInstruction, new Insets(5, 5, 5, 5));
		BorderPane.setAlignment(lblInstruction, Pos.CENTER);
		
		primaryStage.setTitle("Create");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	// Change the scene to showing the edit interface
	public void showEditInterface() {
		// This pane contains the headerPane, questionPane and buttonPane
		BorderPane primaryPane = new BorderPane();
		
		scene = new Scene(primaryPane, sceneWidth, sceneHeight);
		
		// Text fields that contain the selected question's text and choices
		TextField tfQuestionText = new TextField();
		tfQuestionText.setPromptText(qArray.get(currentIndex).getQuestionText());
		tfQuestionText.setText(qArray.get(currentIndex).getQuestionText());
		tfQuestionText.setFont(Font.font(17));
		
		TextField tfChoice1 = new TextField(); tfChoice1.setPromptText(qArray.get(currentIndex).getChoices()[0]); tfChoice1.setFont(Font.font(15));
		TextField tfChoice2 = new TextField(); tfChoice2.setPromptText(qArray.get(currentIndex).getChoices()[1]); tfChoice2.setFont(Font.font(15));
		TextField tfChoice3 = new TextField(); tfChoice3.setPromptText(qArray.get(currentIndex).getChoices()[2]); tfChoice3.setFont(Font.font(15));
		TextField tfChoice4 = new TextField(); tfChoice4.setPromptText(qArray.get(currentIndex).getChoices()[3]); tfChoice4.setFont(Font.font(15));
		
		tfChoice1.setText(qArray.get(currentIndex).getChoices()[0]);
		tfChoice2.setText(qArray.get(currentIndex).getChoices()[1]);
		tfChoice3.setText(qArray.get(currentIndex).getChoices()[2]);
		tfChoice4.setText(qArray.get(currentIndex).getChoices()[3]);
		
		// This pane contains the choices' text fields
		VBox tfChoicesPane = new VBox(14.5);
		tfChoicesPane.getChildren().addAll(tfChoice1, tfChoice2, tfChoice3, tfChoice4);
		
		ToggleGroup group = new ToggleGroup();
		RadioButton rbChoice1 = new RadioButton("A. "); rbChoice1.setToggleGroup(group); rbChoice1.setFont(tfChoice1.getFont());
		RadioButton rbChoice2 = new RadioButton("B. "); rbChoice2.setToggleGroup(group); rbChoice2.setFont(tfChoice2.getFont());
		RadioButton rbChoice3 = new RadioButton("C. "); rbChoice3.setToggleGroup(group); rbChoice3.setFont(tfChoice3.getFont());
		RadioButton rbChoice4 = new RadioButton("D. "); rbChoice4.setToggleGroup(group); rbChoice4.setFont(tfChoice4.getFont());
		
		// Setting IDs for the radio buttons to get the correct choice index in the save button handler
		rbChoice1.setId("0");
		rbChoice2.setId("1");
		rbChoice3.setId("2");
		rbChoice4.setId("3");
		
		// Handler that colors the choice selected by the radio button in green and the rest in black
		EventHandler<ActionEvent> changeLabelColor = e -> {
			if(rbChoice1.isSelected())
				tfChoice1.setStyle("-fx-text-fill: green");
			else
				tfChoice1.setStyle("-fx-text-fill: black");
			
			if(rbChoice2.isSelected())
				tfChoice2.setStyle("-fx-text-fill: green");
			else
				tfChoice2.setStyle("-fx-text-fill: black");
			
			if(rbChoice3.isSelected())
				tfChoice3.setStyle("-fx-text-fill: green");
			else
				tfChoice3.setStyle("-fx-text-fill: black");
			
			if(rbChoice4.isSelected())
				tfChoice4.setStyle("-fx-text-fill: green");
			else
				tfChoice4.setStyle("-fx-text-fill: black");
		};
		
		// Color the correct choice green the first time the interface is shown and select the corresponding radio button
		switch(qArray.get(currentIndex).getCorrectChoiceIndex()) {
		case 0:
			rbChoice1.setSelected(true);
			tfChoice1.setStyle("-fx-text-fill: green");
			break;
		case 1:
			rbChoice2.setSelected(true);
			tfChoice2.setStyle("-fx-text-fill: green");
			break;
		case 2:
			rbChoice3.setSelected(true);
			tfChoice3.setStyle("-fx-text-fill: green");
			break;
		case 3:
			rbChoice4.setSelected(true);
			tfChoice4.setStyle("-fx-text-fill: green");
			break;
		}
				
		rbChoice1.setOnAction(changeLabelColor);
		rbChoice2.setOnAction(changeLabelColor);
		rbChoice3.setOnAction(changeLabelColor);
		rbChoice4.setOnAction(changeLabelColor);
		
		// This pane contains the radio buttons used to select the correct choice
		VBox radioButtonPane = new VBox(24);
		radioButtonPane.getChildren().addAll(rbChoice1, rbChoice2, rbChoice3, rbChoice4);
		radioButtonPane.setPadding(new Insets(6, 6, 0, 0));
		
		// This pane contains the radio button pane and the choices' text fields pane
		BorderPane choicesPane = new BorderPane();
		choicesPane.setLeft(radioButtonPane);
		choicesPane.setCenter(tfChoicesPane);
		
		// This pane contains the choicesPane and the question text
		VBox questionPane = new VBox(30);
		questionPane.getChildren().addAll(tfQuestionText, choicesPane);
		questionPane.setPadding(new Insets(0, 50, 0, 50));
		questionPane.setAlignment(Pos.CENTER_LEFT);
		
		// Go to the main menu when the back button is clicked
		Button btBack = new Button("Back");
		btBack.setOnAction(e -> {
			sceneWidth = scene.getWidth();
			sceneHeight = scene.getHeight();
			showMainMenu();
		});		
		
		Button btSave = new Button("Save");
		// Handler that saves the changes to the selected question in qArray if all requirements are met
		btSave.setOnAction(e -> {
			// If all text fields are filled save the changes to the selected question	
			if( !(tfChoice1.getText().equals("") || tfChoice2.getText().equals("") || tfChoice3.getText().equals("") ||
					tfChoice4.getText().equals("") || tfQuestionText.getText().equals("")) ) {
				String[] choices = {tfChoice1.getText(), tfChoice2.getText(), tfChoice3.getText(), tfChoice4.getText()};
				int correctChoiceIndex = Integer.parseInt( ((RadioButton)group.getSelectedToggle()).getId() );
				
				qArray.get(currentIndex).setQuestionText(tfQuestionText.getText());
				qArray.get(currentIndex).setChoices(choices);
				qArray.get(currentIndex).setCorrectChoiceIndex(correctChoiceIndex);
			}
			// Otherwise show a warning pop-up window
			else {
				warningWindow("Please fill all the text fields", "Edit Failed");
			}
		});
		
		// Text that shows the position of the current question
		Label lblQuestionNumber = new Label((currentIndex+1) + " out of " + qArray.size());

		Button btPrevious = new Button("Previous");
		Button btNext = new Button("Next");
		
		// Handler for next and previous buttons to navigate between the questions
		EventHandler<ActionEvent> navigate = e -> {
			// Change the current index based on the clicked button
			// Increment for next and decrement for previous
			int delta = (( (Button)e.getSource() ).getText() == "Previous")? -1 : 1;
			currentIndex += delta;
			
			// Undo the previous change if the current index is at the boundaries of the qArray
			if(currentIndex < 0 || currentIndex > qArray.size() - 1) {
				currentIndex -= delta;
				return;
			}
			
			// Update the label of the current position
			lblQuestionNumber.setText((currentIndex+1) + " out of " + qArray.size());
			
			// Update the question and choices' text fields
			tfQuestionText.setText(qArray.get(currentIndex).getQuestionText());
			tfChoice1.setText(qArray.get(currentIndex).getChoices()[0]);
			tfChoice2.setText(qArray.get(currentIndex).getChoices()[1]);
			tfChoice3.setText(qArray.get(currentIndex).getChoices()[2]);
			tfChoice4.setText(qArray.get(currentIndex).getChoices()[3]);
			
			tfQuestionText.setPromptText(qArray.get(currentIndex).getQuestionText());
			tfChoice1.setPromptText(qArray.get(currentIndex).getChoices()[0]);
			tfChoice2.setPromptText(qArray.get(currentIndex).getChoices()[1]);
			tfChoice3.setPromptText(qArray.get(currentIndex).getChoices()[2]);
			tfChoice4.setPromptText(qArray.get(currentIndex).getChoices()[3]);
			
			// Select the radio button corresponding to the correct choice
			switch(qArray.get(currentIndex).getCorrectChoiceIndex()) {
			case 0:
				rbChoice1.setSelected(true);
				break;
			case 1:
				rbChoice2.setSelected(true);
				break;
			case 2:
				rbChoice3.setSelected(true);
				break;
			case 3:
				rbChoice4.setSelected(true);
				break;
			}
			
			// Invoke the changeLabelColor handler
			changeLabelColor.handle(e);
		};
		
		btPrevious.setOnAction(navigate);
		btNext.setOnAction(navigate);
		
		// This pane contains the previous, save and next buttons
		HBox buttonPane = new HBox(30);
		buttonPane.setPadding(new Insets(5, 5, 5, 5));
		buttonPane.setAlignment(Pos.CENTER);
		buttonPane.getChildren().addAll(btPrevious, btSave,  btNext);
		
		// This pane contains the back button and the position of the current question
		BorderPane headerPane = new BorderPane();
		headerPane.setPadding(new Insets(5,5,5,5));
		headerPane.setLeft(btBack);
		headerPane.setRight(lblQuestionNumber);
		
		primaryPane.setTop(headerPane);
		primaryPane.setCenter(questionPane);
		primaryPane.setBottom(buttonPane);
		
		primaryStage.setTitle("Edit");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	// When the program starts, read the questions from the file and save it in qArray then go to the main menu
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		
		try {
			read();
		}
		catch(IOException e3){
			e3.printStackTrace();
		}
		
		showMainMenu();
	}
	
	// Save qArray in the file before terminating the program
	@Override
	public void stop() {
		try {
			write();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// Read the questions from the file "QuestionBank.dat" if all requirements are met
	@SuppressWarnings("unchecked")
	public void read() throws IOException {
		
		try(ObjectInputStream input = new ObjectInputStream(new FileInputStream("QuestionBank.dat"))) {
			Object read = input.readObject();
			// If read is an empty ArrayList or a non-empty ArrayList that contains elements of type Question, save it to qArray
			if (read instanceof ArrayList<?> && (((ArrayList<?>) read).isEmpty() || ((ArrayList<?>)read).get(0) instanceof Question)) {
				qArray = (ArrayList<Question>)read;
			}
			else {
				throw new ClassNotFoundException();
			}
		}
		catch(ClassNotFoundException ex1) {
			warningWindow("\"QuestionBank.dat\" is corrupted.\nThe file has been overwritten", "File Corrupted");
			write();
		}
		catch(FileNotFoundException ex2) {
			warningWindow("\"QuestionBank.dat\" not found.\nA new file has been created", "File Not Found");
			write();
		}
	}
	
	// Write qArray to the file "QuestionBank.dat"
	public void write() throws FileNotFoundException, IOException {
		ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream("QuestionBank.dat"));
		output.writeObject(qArray);
		output.close();
	}
	
	// Show a warning pop-up window with a warning message
	public void warningWindow(String warningMessage, String title) {
		ImageView warningImage = new ImageView("Warning.png");
		warningImage.setFitHeight(30);
		warningImage.setFitWidth(30);
		
		Stage warningWindow = new Stage();
		
		// This pane contains the warning message and the OK button
		BorderPane primaryPane = new BorderPane();
		Scene scene = new Scene(primaryPane, 300, 170);
		
		Label lblWarningMessage = new Label(warningMessage, warningImage);
		lblWarningMessage.setWrapText(true);
		lblWarningMessage.setPadding(new Insets(10,10,10,10));
		
		Button btOk = new Button("OK");
		btOk.setOnAction(e -> warningWindow.close()); // Close the window when the OK button is clicked
		
		BorderPane.setAlignment(lblWarningMessage, Pos.CENTER_LEFT);
		BorderPane.setAlignment(btOk, Pos.CENTER);
		BorderPane.setMargin(btOk, new Insets(5,5,5,5));
		primaryPane.setCenter(lblWarningMessage);
		primaryPane.setBottom(btOk);
		
		// Wait 200 ms before showing the window
		Timeline tlWait = new Timeline(new KeyFrame(Duration.millis(200), e -> warningWindow.show()));
		tlWait.play();
		
		warningWindow.setTitle(title);
		warningWindow.setScene(scene);
	}
	
	// Show a deletion confirmation window
	public void deleteWindow(int index, ListView<Question> lvQuestions) {
		ImageView warningImage = new ImageView("Warning.png");
		warningImage.setFitHeight(30);
		warningImage.setFitWidth(30);
		
		Stage deleteWindow = new Stage();
		
		// This pane contains the warning message and the OK button
		BorderPane primaryPane = new BorderPane();
		Scene scene = new Scene(primaryPane, 300, 150);
		
		Label lblWarningMessage = new Label("Are you sure you want to delete the question\n\"" + qArray.get(index).getQuestionText()
				+ "\"?\n This can not be undone.", warningImage);
		lblWarningMessage.setWrapText(true);
		lblWarningMessage.setPadding(new Insets(10,10,10,10));
		
		Button btYes = new Button("Yes");
		Button btNo = new Button("No");
		
		// This pane contains the yes and no buttons
		HBox buttonPane = new HBox(20);
		buttonPane.setAlignment(Pos.CENTER);
		buttonPane.getChildren().addAll(btYes, btNo);
		buttonPane.setPadding(new Insets(5,5,5,5));
		
		// Delete the question from qArray, close the window then update the ListView in the main menu when the yes button is clicked
		btYes.setOnAction(e -> {
			qArray.remove(index);
			deleteWindow.close();
			lvQuestions.setItems(FXCollections.observableArrayList(qArray));
		});
		
		// Close the window when the no button is clicked
		btNo.setOnAction(e -> {
			deleteWindow.close();
		});
		
		primaryPane.setCenter(lblWarningMessage);
		primaryPane.setBottom(buttonPane);
		
		deleteWindow.setTitle("Delete Question");
		deleteWindow.setScene(scene);
		deleteWindow.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}