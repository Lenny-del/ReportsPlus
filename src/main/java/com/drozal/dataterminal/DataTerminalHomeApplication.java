package com.drozal.dataterminal;

import com.drozal.dataterminal.config.ConfigReader;
import com.drozal.dataterminal.util.ResizeHelper;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DataTerminalHomeApplication extends Application {

    public static String getDate() {
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return currentTime.format(formatter);
    }

    public static String getTime() {
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss a");
        return currentTime.format(formatter);
    }

    public static void main(String[] args) {
        launch();
    }

    public static Spinner<Integer> createSpinner(Spinner spinner, int min, int max, int initialValue) {
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max, initialValue);
        spinner.setValueFactory(valueFactory);
        return spinner;
    }

    @Override
    public void start(Stage stage) throws IOException {

        Stage mainRT = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("DataTerminalHome-view.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        // Set stage transparent and remove default window decorations
        mainRT.initStyle(StageStyle.UNDECORATED);
        mainRT.setScene(scene);
        mainRT.setResizable(true);
        mainRT.getIcons().add(new Image(newOfficerApplication.class.getResourceAsStream("imgs/icons/Icon.png")));
        mainRT.show();
        mainRT.centerOnScreen();
        mainRT.setHeight(740);
        mainRT.setWidth(1027);
        mainRT.setMinHeight(mainRT.getHeight() - 200);
        mainRT.setMinWidth(mainRT.getWidth() - 200);
        ResizeHelper.addResizeListener(mainRT);


        actionController controller = loader.getController();
        AnchorPane notesPane = controller.getNotesPane();
        Label generatedByTag = controller.getGeneratedByTag();
        Label generatedOnTag = controller.getGeneratedDateTag();
        AnchorPane shiftInfoPane = controller.getShiftInformationPane();
        AnchorPane informationPane = controller.getInfoPane();
        AnchorPane uiSettingsPane = controller.getUISettingsPane();

        TextField OfficerInfoName = controller.getOfficerInfoName();
        ComboBox OfficerInfoDivision = controller.getOfficerInfoDivision();
        ComboBox OfficerInfoRank = controller.getOfficerInfoRank();
        TextField OfficerInfoNumber = controller.getOfficerInfoNumber();
        ComboBox OfficerInfoAgency = controller.getOfficerInfoAgency();

        shiftInfoPane.setVisible(true);
        shiftInfoPane.setDisable(false);
        uiSettingsPane.setDisable(true);
        uiSettingsPane.setVisible(false);
        notesPane.setVisible(false);
        notesPane.setDisable(true);
        informationPane.setVisible(false);
        informationPane.setDisable(true);

        String name = ConfigReader.configRead("Name");
        String division = ConfigReader.configRead("Division");
        String rank = ConfigReader.configRead("Rank");
        String number = ConfigReader.configRead("Number");
        String agency = ConfigReader.configRead("Agency");

        OfficerInfoName.setText(name);
        OfficerInfoDivision.setValue(division);
        OfficerInfoRank.setValue(rank);
        OfficerInfoAgency.setValue(agency);
        OfficerInfoNumber.setText(number);

        //Top Generations
        generatedByTag.setText("Generated By:" + " " + name);
        String time = DataTerminalHomeApplication.getTime();
        generatedOnTag.setText("Generated At: " + time);
    }
}
