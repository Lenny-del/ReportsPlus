package com.drozal.dataterminal;

import com.drozal.dataterminal.config.ConfigReader;
import com.drozal.dataterminal.logs.Incident.IncidentLogEntry;
import com.drozal.dataterminal.logs.Incident.IncidentReportLogs;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

import java.io.IOException;
import java.util.List;

import static com.drozal.dataterminal.DataTerminalHomeApplication.*;

public class IncidentReportController {
    public Spinner incidentNumber;
    public TextField incidentDate;
    public TextField incidentTime;
    public TextArea incidentStatement;
    public TextField incidentWitnesses;
    public TextField incidentVictims;
    public TextField officerName;
    public TextField officerRank;
    public TextField officerNumber;
    public TextField officerAgency;
    public TextField officerDivision;
    public TextField incidentStreet;
    public TextField incidentArea;
    public TextField incidentCounty;
    public TextArea incidentActionsTaken;
    public TextArea incidentComments;
    public VBox vbox;
    public Label incompleteLabel;
    Boolean hasEntered = false;
    private double xOffset = 0;
    private double yOffset = 0;

    public void initialize() throws IOException {
        String name = ConfigReader.configRead("Name");
        String division = ConfigReader.configRead("Division");
        String rank = ConfigReader.configRead("Rank");
        String number = ConfigReader.configRead("Number");
        String agency = ConfigReader.configRead("Agency");

        officerName.setText(name);
        officerDivision.setText(division);
        officerRank.setText(rank);
        officerAgency.setText(agency);
        officerNumber.setText(number);

        createSpinner(incidentNumber, 0, 9999, 0);
        incidentTime.setText(getTime());
        incidentDate.setText(getDate());
    }

    public void onCalloutReportSubmitBtnClick(ActionEvent actionEvent) {
        if (incidentNumber.getValue() == null) {
            incompleteLabel.setText("Fill Out Form.");
            incompleteLabel.setStyle("-fx-text-fill: red;");
            incompleteLabel.setVisible(true);
            Timeline timeline1 = new Timeline(new KeyFrame(Duration.seconds(1), evt -> {
                incompleteLabel.setVisible(false);
            }));
            timeline1.play();
        } else {
            // Load existing logs from XML
            IncidentReportLogs calloutReportLogs = new IncidentReportLogs();
            List<IncidentLogEntry> logs = IncidentReportLogs.loadLogsFromXML();

            // Add new entry
            logs.add(new IncidentLogEntry(
                    incidentNumber.getValue().toString(),
                    incidentDate.getText(),
                    incidentTime.getText(),
                    incidentStatement.getText(),
                    incidentWitnesses.getText(),
                    incidentVictims.getText(),
                    officerName.getText(),
                    officerRank.getText(),
                    officerNumber.getText(),
                    officerAgency.getText(),
                    officerDivision.getText(),
                    incidentStreet.getText(),
                    incidentArea.getText(),
                    incidentCounty.getText(),
                    incidentActionsTaken.getText(),
                    incidentComments.getText()
            ));

            // Save logs to XML
            IncidentReportLogs.saveLogsToXML(logs);

            Stage stag = (Stage) vbox.getScene().getWindow();
            stag.close();
        }
    }

    public void onMouseDrag(MouseEvent mouseEvent) {
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.setX(mouseEvent.getScreenX() - xOffset);
        stage.setY(mouseEvent.getScreenY() - yOffset);
    }

    public void onMousePress(MouseEvent mouseEvent) {
        xOffset = mouseEvent.getSceneX();
        yOffset = mouseEvent.getSceneY();
    }

    public void onExitButtonClick(MouseEvent actionEvent) {
        // Get the window associated with the scene
        Window window = vbox.getScene().getWindow();

        // Close the window
        window.hide(); // or window.close() if you want to force close
    }

    public Spinner getIncidentNumber() {
        return incidentNumber;
    }

    public TextField getIncidentDate() {
        return incidentDate;
    }

    public TextField getIncidentTime() {
        return incidentTime;
    }

    public TextArea getIncidentStatement() {
        return incidentStatement;
    }

    public TextField getIncidentWitnesses() {
        return incidentWitnesses;
    }

    public TextField getIncidentVictims() {
        return incidentVictims;
    }

    public TextField getOfficerName() {
        return officerName;
    }

    public TextField getOfficerRank() {
        return officerRank;
    }

    public TextField getOfficerNumber() {
        return officerNumber;
    }

    public TextField getOfficerAgency() {
        return officerAgency;
    }

    public TextField getOfficerDivision() {
        return officerDivision;
    }

    public TextField getIncidentStreet() {
        return incidentStreet;
    }

    public TextField getIncidentArea() {
        return incidentArea;
    }

    public TextField getIncidentCounty() {
        return incidentCounty;
    }

    public TextArea getIncidentActionsTaken() {
        return incidentActionsTaken;
    }

    public TextArea getIncidentComments() {
        return incidentComments;
    }

    public VBox getVbox() {
        return vbox;
    }

    public Label getIncompleteLabel() {
        return incompleteLabel;
    }
}
