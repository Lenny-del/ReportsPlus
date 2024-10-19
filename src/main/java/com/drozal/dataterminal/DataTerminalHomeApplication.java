package com.drozal.dataterminal;

import com.drozal.dataterminal.Desktop.mainDesktopController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DataTerminalHomeApplication extends Application {
	
	public static Stage mainRT;
	public static mainDesktopController mainDesktopControllerObj;
	public static Stage mainDesktopStage;
	
	public static String getDate() {
		LocalDateTime currentTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
		return currentTime.format(formatter);
	}
	
	public static String getTime() {
		LocalDateTime currentTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss a", Locale.ENGLISH);
		return currentTime.format(formatter);
	}
	
	public static void main(String[] args) {
		launch();
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource("Windows/Desktop/desktop-main.fxml"));
		Scene scene = new Scene(fxmlLoader.load());
		mainDesktopControllerObj = fxmlLoader.getController();
		primaryStage.setTitle("ReportsPlus Desktop");
		primaryStage.setScene(scene);
		primaryStage.show();
		mainDesktopStage = primaryStage;
		
		// todo add a config for this
		primaryStage.setFullScreen(true);
		
		DataTerminalHomeApplication.mainRT = mainDesktopStage;
		
	}
}