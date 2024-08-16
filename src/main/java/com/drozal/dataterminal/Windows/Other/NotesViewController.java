package com.drozal.dataterminal.Windows.Other;

import com.drozal.dataterminal.Launcher;
import com.drozal.dataterminal.Windows.Main.actionController;
import com.drozal.dataterminal.config.ConfigReader;
import com.drozal.dataterminal.config.ConfigWriter;
import com.drozal.dataterminal.util.Misc.NoteTab;
import com.drozal.dataterminal.util.Report.reportUtil;
import com.drozal.dataterminal.util.Window.windowUtils;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.util.List;

import static com.drozal.dataterminal.Windows.Main.actionController.notesViewController;
import static com.drozal.dataterminal.util.Misc.LogUtils.logError;

public class NotesViewController {
	
	public static AnchorPane titlebar = null;
	public static Stage codesStage = null;
	public static List<NoteTab> notesTabList;
	
	@javafx.fxml.FXML
	private TextArea notepadTextArea;
	@javafx.fxml.FXML
	private Button clearbtnnotepad;
	@javafx.fxml.FXML
	private BorderPane borderPane;
	@javafx.fxml.FXML
	private ToggleButton modeToggle;
	@javafx.fxml.FXML
	private Button codesbtnnotepad;
	@javafx.fxml.FXML
	private AnchorPane codeSelectionPane;
	@javafx.fxml.FXML
	private VBox codevbox;
	@javafx.fxml.FXML
	private Label codeSelectionlbl;
	@javafx.fxml.FXML
	private AnchorPane defaultNotesPane;
	@javafx.fxml.FXML
	private TabPane tabPane;
	
	public void initialize() throws IOException {
		titlebar = reportUtil.createTitleBar("NotePad");
		borderPane.setTop(titlebar);
		
		notepadTextArea.setText(actionController.notesText);
		
		String hoverStyle = "-fx-background-color: " + ConfigReader.configRead("uiColors", "mainColor") + ";";
		String initialStyle = "-fx-background-color: " + ConfigReader.configRead("uiColors", "accentColor") + ";";
		String padding = " -fx-padding: 2 7 2 7;";
		
		clearbtnnotepad.setStyle(initialStyle + padding);
		clearbtnnotepad.setOnMouseEntered(e -> clearbtnnotepad.setStyle(hoverStyle + padding));
		clearbtnnotepad.setOnMouseExited(e -> clearbtnnotepad.setStyle(initialStyle + padding));
		
		codesbtnnotepad.setStyle(initialStyle + padding);
		codesbtnnotepad.setOnMouseEntered(e -> codesbtnnotepad.setStyle(hoverStyle + padding));
		codesbtnnotepad.setOnMouseExited(e -> codesbtnnotepad.setStyle(initialStyle + padding));
		
		String notepadMode = ConfigReader.configRead("notepad", "notepadMode");
		if (notepadMode != null) {
			String notepadStyle;
			String modeToggleStyle;
			if (notepadMode.equals("Dark")) {
				modeToggle.setSelected(true);
				notepadStyle = "-fx-background-color: #666666; -fx-text-fill: white; -fx-border-color: transparent; -fx-background-radius: 0; -fx-border-radius: 0;";
				modeToggleStyle = "-fx-background-color: white;";
				codeSelectionPane.setStyle("-fx-background-color: gray;");
				codevbox.setStyle("-fx-background-color: rgb(200,200,200,1);");
				borderPane.setStyle("-fx-background-color: gray;");
				codeSelectionlbl.setStyle("-fx-text-fill: #f2f2f2;");
				notepadTextArea.setStyle(notepadStyle);
				modeToggle.setStyle(modeToggleStyle);
			} else if (notepadMode.equals("Light")) {
				modeToggle.setSelected(false);
				notepadStyle = "-fx-background-color: white; -fx-text-fill: black; -fx-border-color: transparent; -fx-background-radius: 0; -fx-border-radius: 0;";
				modeToggleStyle = "-fx-background-color: grey;";
				codeSelectionPane.setStyle("-fx-background-color: rgb(240,240,240,0.1);");
				codevbox.setStyle("-fx-background-color: rgb(210,210,210,0.3);");
				borderPane.setStyle("-fx-background-color: white;");
				codeSelectionlbl.setStyle("-fx-text-fill: gray;");
				notepadTextArea.setStyle(notepadStyle);
				modeToggle.setStyle(modeToggleStyle);
			}
			
		} else {
			notepadTextArea.setStyle(
					"-fx-background-color: white; -fx-text-fill: black; -fx-border-color: transparent; -fx-background-radius: 0; -fx-border-radius: 0;");
			modeToggle.setStyle("-fx-background-color: grey;");
		}
		
		codeSelectionPane.setPrefWidth(0);
		codeSelectionPane.setMaxWidth(0);
		codeSelectionPane.setMinWidth(0);
		codeSelectionPane.setVisible(false);
		
		Platform.runLater(() -> {
			try {
				createNoteTabs();
			} catch (IOException e) {
				logError("CreateNoteTabs; Exception error: ", e);
			}
		});
	}
	
	public static void createNoteTabs() throws IOException {
		String hoverStyle = "-fx-background-color: " + ConfigReader.configRead("uiColors", "mainColor") + ";";
		String initialStyle = "-fx-background-color: " + ConfigReader.configRead("uiColors", "accentColor") + ";";
		String notepadMode = ConfigReader.configRead("notepad", "notepadMode");
		
		String padding = " -fx-padding: 2 7 2 7;";
		
		notesViewController.getTabPane().getTabs().removeIf(tab -> !tab.getText().equalsIgnoreCase("default"));
		
		for (NoteTab note : notesTabList) {
			Tab newTab = new Tab(note.getTabName());
			
			FXMLLoader loader = new FXMLLoader(Launcher.class.getResource("Windows/Templates/notes-template.fxml"));
			Parent anchorPaneParent;
			try {
				anchorPaneParent = loader.load();
			} catch (IOException e) {
				logError("Could not load note template", e);
				continue;
			}
			
			AnchorPane anchorPane = (AnchorPane) anchorPaneParent;
			
			newTab.setContent(anchorPane);
			
			notesViewController.getTabPane().getTabs().add(newTab);
			
			TextArea noteArea = (TextArea) anchorPane.lookup("#notepadTextArea");
			Button clrBtn = (Button) anchorPane.lookup("#clearbtnnotepad");
			ToggleButton modeToggle = (ToggleButton) anchorPane.lookup("#modeToggle");
			Button codesbtnnotepad = (Button) anchorPane.lookup("#codesbtnnotepad");
			
			newTab.setOnClosed(event -> notesTabList.remove(note));
			
			if (noteArea != null) {
				if (note.getNoteText() != null && !note.getNoteText().isEmpty()) {
					noteArea.setText(note.getNoteText());
				}
				
				noteArea.textProperty().addListener((observable, oldValue, newValue) -> {
					note.setNoteText(newValue);
				});
				
				clrBtn.setStyle(initialStyle + padding);
				clrBtn.setOnMouseEntered(e -> clrBtn.setStyle(hoverStyle + padding));
				clrBtn.setOnMouseExited(e -> clrBtn.setStyle(initialStyle + padding));
				
				codesbtnnotepad.setStyle(initialStyle + padding);
				codesbtnnotepad.setOnMouseEntered(e -> codesbtnnotepad.setStyle(hoverStyle + padding));
				codesbtnnotepad.setOnMouseExited(e -> codesbtnnotepad.setStyle(initialStyle + padding));
				if (notepadMode != null) {
					String notepadStyle;
					String modeToggleStyle;
					if (notepadMode.equals("Dark")) {
						modeToggle.setSelected(true);
						notepadStyle = "-fx-background-color: #666666; -fx-text-fill: white; -fx-border-color: transparent; -fx-background-radius: 0; -fx-border-radius: 0;";
						modeToggleStyle = "-fx-background-color: white;";
						noteArea.setStyle(notepadStyle);
						modeToggle.setStyle(modeToggleStyle);
					} else if (notepadMode.equals("Light")) {
						modeToggle.setSelected(false);
						notepadStyle = "-fx-background-color: white; -fx-text-fill: black; -fx-border-color: transparent; -fx-background-radius: 0; -fx-border-radius: 0;";
						modeToggleStyle = "-fx-background-color: grey;";
						noteArea.setStyle(notepadStyle);
						modeToggle.setStyle(modeToggleStyle);
					}
				} else {
					noteArea.setStyle(
							"-fx-background-color: white; -fx-text-fill: black; -fx-border-color: transparent; -fx-background-radius: 0; -fx-border-radius: 0;");
					modeToggle.setStyle("-fx-background-color: grey;");
				}
			}
			if (clrBtn != null) {
				clrBtn.setOnAction(actionEvent -> {
                    if (noteArea != null) {
                        noteArea.setText("");
                    }
				});
			}
			if (modeToggle != null) {
				modeToggle.setOnAction(actionEvent -> {
					try {
						if (ConfigReader.configRead("notepad", "notepadMode").equals("Light")) {
							ConfigWriter.configwrite("notepad", "notepadMode", "Dark");
							if (noteArea != null) {
								noteArea.setStyle(
										"-fx-background-color: #666666; -fx-text-fill: white; -fx-border-color: transparent;-fx-background-radius: 0; -fx-border-radius: 0;");
							}
							modeToggle.setStyle(
									"-fx-background-color: white; -fx-background-radius: 0; -fx-border-radius: 0;");
							notesViewController.getCodeSelectionPane().setStyle("-fx-background-color: gray;");
							notesViewController.getCodevbox().setStyle("-fx-background-color: rgb(200,200,200,1);");
							notesViewController.getBorderPane().setStyle("-fx-background-color: gray;");
							notesViewController.getCodeSelectionlbl().setStyle("-fx-text-fill: #e2e2e2;");
						} else {
							if (ConfigReader.configRead("notepad", "notepadMode").equals("Dark")) {
								ConfigWriter.configwrite("notepad", "notepadMode", "Light");
								if (noteArea != null) {
									noteArea.setStyle(
											"-fx-background-color: white; -fx-text-fill: black; -fx-border-color: transparent; -fx-background-radius: 0; -fx-border-radius: 0;");
								}
								modeToggle.setStyle("-fx-background-color: grey;");
								notesViewController.getCodeSelectionPane().setStyle(
										"-fx-background-color: rgb(240,240,240,0.1);");
								notesViewController.getCodevbox().setStyle(
										"-fx-background-color: rgb(210,210,210,0.3);");
								notesViewController.getBorderPane().setStyle("-fx-background-color: white;");
								notesViewController.getCodeSelectionlbl().setStyle("-fx-text-fill: gray;");
							}
						}
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				});
			}
			if (codesbtnnotepad != null) {
				codesbtnnotepad.setOnAction(actionEvent -> {
					if (notesViewController.getCodeSelectionPane().isVisible()) {
						
						double toWidth = 0;
						
						Timeline timeline = new Timeline();
						
						KeyValue keyValuePrefHeight = new KeyValue(
								notesViewController.getCodeSelectionPane().prefWidthProperty(), toWidth);
						KeyValue keyValueMaxHeight = new KeyValue(
								notesViewController.getCodeSelectionPane().maxWidthProperty(), toWidth);
						KeyValue keyValueMinHeight = new KeyValue(
								notesViewController.getCodeSelectionPane().minWidthProperty(), toWidth);
						KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.2), keyValuePrefHeight, keyValueMaxHeight,
						                                 keyValueMinHeight);
						
						timeline.getKeyFrames().add(keyFrame);
						
						timeline.play();
						notesViewController.getCodeSelectionPane().setVisible(false);
					} else {
						
						double toWidth = 200;
						
						Timeline timeline = new Timeline();
						
						KeyValue keyValuePrefHeight = new KeyValue(
								notesViewController.getCodeSelectionPane().prefWidthProperty(), toWidth);
						KeyValue keyValueMaxHeight = new KeyValue(
								notesViewController.getCodeSelectionPane().maxWidthProperty(), toWidth);
						KeyValue keyValueMinHeight = new KeyValue(
								notesViewController.getCodeSelectionPane().minWidthProperty(), toWidth);
						KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.2), keyValuePrefHeight, keyValueMaxHeight,
						                                 keyValueMinHeight);
						
						timeline.getKeyFrames().add(keyFrame);
						
						timeline.play();
						notesViewController.getCodeSelectionPane().setVisible(true);
					}
				});
			}
		}
	}
	
	public BorderPane getBorderPane() {
		return borderPane;
	}
	
	public Label getCodeSelectionlbl() {
		return codeSelectionlbl;
	}
	
	public VBox getCodevbox() {
		return codevbox;
	}
	
	public AnchorPane getCodeSelectionPane() {
		return codeSelectionPane;
	}
	
	public TabPane getTabPane() {
		return tabPane;
	}
	
	public TextArea getNotepadTextArea() {
		return notepadTextArea;
	}
	
	@javafx.fxml.FXML
	public void onDarkModeToggle() throws IOException {
		if (ConfigReader.configRead("notepad", "notepadMode").equals("Light")) {
			ConfigWriter.configwrite("notepad", "notepadMode", "Dark");
			notepadTextArea.setStyle(
					"-fx-background-color: #666666; -fx-text-fill: white; -fx-border-color: transparent;-fx-background-radius: 0; -fx-border-radius: 0;");
			modeToggle.setStyle("-fx-background-color: white; -fx-background-radius: 0; -fx-border-radius: 0;");
			codeSelectionPane.setStyle("-fx-background-color: gray;");
			codevbox.setStyle("-fx-background-color: rgb(200,200,200,1);");
			borderPane.setStyle("-fx-background-color: gray;");
			codeSelectionlbl.setStyle("-fx-text-fill: #e2e2e2;");
		} else if (ConfigReader.configRead("notepad", "notepadMode").equals("Dark")) {
			ConfigWriter.configwrite("notepad", "notepadMode", "Light");
			notepadTextArea.setStyle(
					"-fx-background-color: white; -fx-text-fill: black; -fx-border-color: transparent; -fx-background-radius: 0; -fx-border-radius: 0;");
			modeToggle.setStyle("-fx-background-color: grey;");
			codeSelectionPane.setStyle("-fx-background-color: rgb(240,240,240,0.1);");
			codevbox.setStyle("-fx-background-color: rgb(210,210,210,0.3);");
			borderPane.setStyle("-fx-background-color: white;");
			codeSelectionlbl.setStyle("-fx-text-fill: gray;");
		}
	}
	
	@javafx.fxml.FXML
	public void onclearclick() {
		notepadTextArea.setText("");
	}
	
	@javafx.fxml.FXML
	public void oncodesclick() throws IOException {
		if (codesStage != null && codesStage.isShowing()) {
			codesStage.close();
			codesStage = null;
			return;
		}
		codesStage = new Stage();
		codesStage.initStyle(StageStyle.UNDECORATED);
		FXMLLoader loader = new FXMLLoader(Launcher.class.getResource("Windows/Misc/codes-window.fxml"));
		Parent root = loader.load();
		Scene newScene = new Scene(root);
		codesStage.setTitle("Codes");
		codesStage.setScene(newScene);
		codesStage.setAlwaysOnTop(true);
		
		codesStage.show();
		
		windowUtils.centerStageOnMainApp(codesStage);
		
		codesStage.setOnHidden(event -> codesStage = null);
	}
	
	@javafx.fxml.FXML
	public void showcodeselection(ActionEvent actionEvent) {
		if (codeSelectionPane.isVisible()) {
			
			double toWidth = 0;
			
			Timeline timeline = new Timeline();
			
			KeyValue keyValuePrefHeight = new KeyValue(codeSelectionPane.prefWidthProperty(), toWidth);
			KeyValue keyValueMaxHeight = new KeyValue(codeSelectionPane.maxWidthProperty(), toWidth);
			KeyValue keyValueMinHeight = new KeyValue(codeSelectionPane.minWidthProperty(), toWidth);
			KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.2), keyValuePrefHeight, keyValueMaxHeight,
			                                 keyValueMinHeight);
			
			timeline.getKeyFrames().add(keyFrame);
			
			timeline.play();
			codeSelectionPane.setVisible(false);
		} else {
			
			double toWidth = 200;
			
			Timeline timeline = new Timeline();
			
			KeyValue keyValuePrefHeight = new KeyValue(codeSelectionPane.prefWidthProperty(), toWidth);
			KeyValue keyValueMaxHeight = new KeyValue(codeSelectionPane.maxWidthProperty(), toWidth);
			KeyValue keyValueMinHeight = new KeyValue(codeSelectionPane.minWidthProperty(), toWidth);
			KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.2), keyValuePrefHeight, keyValueMaxHeight,
			                                 keyValueMinHeight);
			
			timeline.getKeyFrames().add(keyFrame);
			
			timeline.play();
			codeSelectionPane.setVisible(true);
		}
	}
	
	@javafx.fxml.FXML
	public void onName(ActionEvent actionEvent) {
		notepadTextArea.appendText("-name ");
		notepadTextArea.requestFocus();
	}
	
	@javafx.fxml.FXML
	public void onNumber(ActionEvent actionEvent) {
		notepadTextArea.appendText("-number ");
		notepadTextArea.requestFocus();
	}
	
	@javafx.fxml.FXML
	public void onStreet(ActionEvent actionEvent) {
		notepadTextArea.appendText("-street ");
		notepadTextArea.requestFocus();
	}
	
	@javafx.fxml.FXML
	public void onAddress(ActionEvent actionEvent) {
		notepadTextArea.appendText("-address ");
		notepadTextArea.requestFocus();
	}
	
	@javafx.fxml.FXML
	public void onNotes(ActionEvent actionEvent) {
		notepadTextArea.appendText("-notes ");
		notepadTextArea.requestFocus();
	}
	
	@javafx.fxml.FXML
	public void onSearchItems(ActionEvent actionEvent) {
		notepadTextArea.appendText("-searchitems ");
		notepadTextArea.requestFocus();
	}
	
	@javafx.fxml.FXML
	public void onAge(ActionEvent actionEvent) {
		notepadTextArea.appendText("-age ");
		notepadTextArea.requestFocus();
	}
	
	@javafx.fxml.FXML
	public void onModel(ActionEvent actionEvent) {
		notepadTextArea.appendText("-model ");
		notepadTextArea.requestFocus();
	}
	
	@javafx.fxml.FXML
	public void onPlate(ActionEvent actionEvent) {
		notepadTextArea.appendText("-platenum ");
		notepadTextArea.requestFocus();
	}
	
	@javafx.fxml.FXML
	public void onDescription(ActionEvent actionEvent) {
		notepadTextArea.appendText("-description ");
		notepadTextArea.requestFocus();
	}
	
	@javafx.fxml.FXML
	public void onGender(ActionEvent actionEvent) {
		notepadTextArea.appendText("-gender ");
		notepadTextArea.requestFocus();
	}
	
	@javafx.fxml.FXML
	public void onArea(ActionEvent actionEvent) {
		notepadTextArea.appendText("-area ");
		notepadTextArea.requestFocus();
	}
	
	@javafx.fxml.FXML
	public void onCounty(ActionEvent actionEvent) {
		notepadTextArea.appendText("-county ");
		notepadTextArea.requestFocus();
	}
	
	@javafx.fxml.FXML
	public void addTabBtn(ActionEvent actionEvent) throws IOException {
		int num = 1;
		for (NoteTab tab : notesTabList) {
			num++;
		}
		notesTabList.add(new NoteTab("Tab " + num));
		createNoteTabs();
	}
}
