package com.drozal.dataterminal;

import com.drozal.dataterminal.config.ConfigReader;
import com.drozal.dataterminal.config.ConfigWriter;
import com.drozal.dataterminal.logs.Arrest.ArrestReportLogs;
import com.drozal.dataterminal.logs.Callout.CalloutLogEntry;
import com.drozal.dataterminal.logs.Callout.CalloutReportLogs;
import com.drozal.dataterminal.logs.Impound.ImpoundReportLogs;
import com.drozal.dataterminal.logs.Incident.IncidentLogEntry;
import com.drozal.dataterminal.logs.Incident.IncidentReportLogs;
import com.drozal.dataterminal.logs.ParkingCitation.ParkingCitationReportLogs;
import com.drozal.dataterminal.logs.Patrol.PatrolLogEntry;
import com.drozal.dataterminal.logs.Patrol.PatrolReportLogs;
import com.drozal.dataterminal.logs.Search.SearchLogEntry;
import com.drozal.dataterminal.logs.Search.SearchReportLogs;
import com.drozal.dataterminal.logs.TrafficCitation.TrafficCitationReportLogs;
import com.drozal.dataterminal.logs.TrafficStop.TrafficStopReportLogs;
import com.drozal.dataterminal.util.ResizeHelper;
import com.drozal.dataterminal.util.dropdownInfo;
import com.drozal.dataterminal.util.stringUtil;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.PopOver;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

import static com.drozal.dataterminal.DataTerminalHomeApplication.*;
import static com.drozal.dataterminal.util.windowUtils.toggleWindowedFullscreen;

public class actionController {
    @javafx.fxml.FXML
    public Button notesButton;
    @javafx.fxml.FXML
    public Button shiftInfoBtn;
    @javafx.fxml.FXML
    public AnchorPane shiftInformationPane;
    @javafx.fxml.FXML
    public TextField OfficerInfoName;
    @javafx.fxml.FXML
    public ComboBox OfficerInfoDivision;
    @javafx.fxml.FXML
    public TextArea ShiftInfoNotesTextArea;
    @javafx.fxml.FXML
    public ComboBox OfficerInfoAgency;
    @javafx.fxml.FXML
    public TextField OfficerInfoCallsign;
    @javafx.fxml.FXML
    public TextField OfficerInfoNumber;
    @javafx.fxml.FXML
    public ComboBox OfficerInfoRank;
    @javafx.fxml.FXML
    public Label generatedDateTag;
    @javafx.fxml.FXML
    public Label generatedByTag;
    @javafx.fxml.FXML
    public AnchorPane infoPane;
    @javafx.fxml.FXML
    public Label updatedNotification;
    @javafx.fxml.FXML
    public AnchorPane vbox;
    @javafx.fxml.FXML
    public AnchorPane UISettingsPane;
    @javafx.fxml.FXML
    public BarChart reportChart;
    @javafx.fxml.FXML
    public ColorPicker colorSelectMain;
    @javafx.fxml.FXML
    public AnchorPane topPane;
    @javafx.fxml.FXML
    public AnchorPane sidepane;
    @javafx.fxml.FXML
    public ColorPicker colorSelectSecondary;
    @javafx.fxml.FXML
    public Label mainColor8;
    @javafx.fxml.FXML
    public Label mainColor9Bkg;
    @javafx.fxml.FXML
    public Button updateInfoBtn;
    @javafx.fxml.FXML
    public MenuButton settingsDropdown;
    @javafx.fxml.FXML
    public Button resetDefaultsBtn;
    @javafx.fxml.FXML
    public Label primaryColor;
    @javafx.fxml.FXML
    public Label secondaryColor;
    @javafx.fxml.FXML
    public AnchorPane calloutReportPane;
    private double xOffset = 0;
    private double yOffset = 0;
    @javafx.fxml.FXML
    private Label secondaryColor2;
    @javafx.fxml.FXML
    private Label secondaryColor5;
    @javafx.fxml.FXML
    private Label secondaryColor3;
    @javafx.fxml.FXML
    private Label secondaryColor4;
    @javafx.fxml.FXML
    private Label secondaryColor3Bkg;
    @javafx.fxml.FXML
    private Label mainColor6;
    @javafx.fxml.FXML
    private Label mainColor7Bkg;
    @javafx.fxml.FXML
    private Label secondaryColor4Bkg;
    @javafx.fxml.FXML
    private Label secondaryColor5Bkg;
    @javafx.fxml.FXML
    private Button logsButton;
    @javafx.fxml.FXML
    private Button mapButton;
    @javafx.fxml.FXML
    private MenuButton createReportBtn;
    @javafx.fxml.FXML
    private TextField calloutReportResponseCounty;
    @javafx.fxml.FXML
    private MenuItem parkingCitationReportButton;
    @javafx.fxml.FXML
    private TextField calloutReportName;
    @javafx.fxml.FXML
    private Label calloutincompleteLabel;
    @javafx.fxml.FXML
    private MenuItem searchReportButton;
    @javafx.fxml.FXML
    private TextField calloutReportAgency;
    @javafx.fxml.FXML
    private MenuItem trafficReportButton;
    @javafx.fxml.FXML
    private TextField calloutReportResponseAddress;
    @javafx.fxml.FXML
    private MenuItem impoundReportButton;
    @javafx.fxml.FXML
    private TextField calloutReportResponseGrade;
    @javafx.fxml.FXML
    private TextArea calloutReportNotesTextArea;
    @javafx.fxml.FXML
    private Button calloutReportSubmitBtn;
    @javafx.fxml.FXML
    private MenuItem incidentReportButton;
    @javafx.fxml.FXML
    private MenuItem patrolReportButton;
    @javafx.fxml.FXML
    private TextField calloutReportTime;
    @javafx.fxml.FXML
    private MenuItem calloutReportButton;
    @javafx.fxml.FXML
    private TextField calloutReportDivision;
    @javafx.fxml.FXML
    private Spinner calloutReportSpinner;
    @javafx.fxml.FXML
    private TextField calloutReportResponeType;
    @javafx.fxml.FXML
    private TextField calloutReportResponseArea;
    @javafx.fxml.FXML
    private MenuItem arrestReportButton;
    @javafx.fxml.FXML
    private MenuItem trafficCitationReportButton;
    @javafx.fxml.FXML
    private TextField calloutReportNumber;
    @javafx.fxml.FXML
    private TextField calloutReportDate;
    @javafx.fxml.FXML
    private TextField calloutReportRank;
    @javafx.fxml.FXML
    private TextField patrolDate;
    @javafx.fxml.FXML
    private TextField patrolLength;
    @javafx.fxml.FXML
    private TextField patrolofficerNumber;
    @javafx.fxml.FXML
    private TextField patrolStopTime;
    @javafx.fxml.FXML
    private AnchorPane patrolReportPane;
    @javafx.fxml.FXML
    private TextField patrolofficerDivision;
    @javafx.fxml.FXML
    private Label patrolincompleteLabel;
    @javafx.fxml.FXML
    private TextField patrolofficerVehicle;
    @javafx.fxml.FXML
    private TextField patrolStartTime;
    @javafx.fxml.FXML
    private TextArea patrolComments;
    @javafx.fxml.FXML
    private Spinner patrolSpinnerNumber;
    @javafx.fxml.FXML
    private TextField patrolofficerRank;
    @javafx.fxml.FXML
    private TextField patrolofficerAgency;
    @javafx.fxml.FXML
    private TextField patrolofficerName;
    @javafx.fxml.FXML
    private Button incidentReportSubmitBtn;
    @javafx.fxml.FXML
    private TextArea incidentComments;
    @javafx.fxml.FXML
    private TextField incidentofficerName;
    @javafx.fxml.FXML
    private TextField incidentofficerAgency;
    @javafx.fxml.FXML
    private AnchorPane incidentReportPane;
    @javafx.fxml.FXML
    private TextField incidentofficerDivision;
    @javafx.fxml.FXML
    private TextField incidentVictims;
    @javafx.fxml.FXML
    private TextField incidentofficerRank;
    @javafx.fxml.FXML
    private TextField incidentCounty;
    @javafx.fxml.FXML
    private TextField incidentWitnesses;
    @javafx.fxml.FXML
    private TextArea incidentStatement;
    @javafx.fxml.FXML
    private TextField incidentofficerNumber;
    @javafx.fxml.FXML
    private TextField incidentStreet;
    @javafx.fxml.FXML
    private TextArea incidentActionsTaken;
    @javafx.fxml.FXML
    private Label incidentincompleteLabel;
    @javafx.fxml.FXML
    private TextField incidentArea;
    @javafx.fxml.FXML
    private TextField incidentReportTime;
    @javafx.fxml.FXML
    private Spinner incidentNumber;
    @javafx.fxml.FXML
    private TextField incidentReportdate;
    @javafx.fxml.FXML
    private Button searchReportSubmitBtn;
    @javafx.fxml.FXML
    private TextArea searchComments;
    @javafx.fxml.FXML
    private TextField searchTime;
    @javafx.fxml.FXML
    private TextField searchofficerName;
    @javafx.fxml.FXML
    private Spinner SearchNumber;
    @javafx.fxml.FXML
    private TextField searchofficerDivision;
    @javafx.fxml.FXML
    private TextField searchWitnesses;
    @javafx.fxml.FXML
    private Button searchpopOverBtn;
    @javafx.fxml.FXML
    private AnchorPane searchReportPane;
    @javafx.fxml.FXML
    private TextField searchCounty;
    @javafx.fxml.FXML
    private TextField searchedPersons;
    @javafx.fxml.FXML
    private ComboBox searchType;
    @javafx.fxml.FXML
    private TextField searchofficerRank;
    @javafx.fxml.FXML
    private TextField searchofficerNumber;
    @javafx.fxml.FXML
    private ComboBox searchMethod;
    @javafx.fxml.FXML
    private Label searchincompleteLabel;
    @javafx.fxml.FXML
    private TextField searchArea;
    @javafx.fxml.FXML
    private TextField searchStreet;
    @javafx.fxml.FXML
    private TextField searchDate;
    @javafx.fxml.FXML
    private TextField searchGrounds;
    @javafx.fxml.FXML
    private TextArea searchSeizedItems;
    @javafx.fxml.FXML
    private TextField searchofficerAgency;
    private DUIInformationController duiInformationController;

    public static String getDataLogsFolderPath() {
        try {
            // Get the location of the JAR file
            String jarPath = stringUtil.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();

            // Extract the directory path from the JAR path
            String jarDir = new File(jarPath).getParent();

            // Construct the path for the DataLogs folder
            return jarDir + File.separator + "DataLogs" + File.separator;
        } catch (URISyntaxException e) {
            // Handle exception if URI syntax is incorrect
            e.printStackTrace();
            return ""; // Return empty string if an error occurs
        }
    }

    public static String getJarDirectoryPath() {
        try {
            // Get the location of the JAR file
            String jarPath = actionController.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();

            // Extract the directory path from the JAR path
            return new File(jarPath).getParent();
        } catch (Exception e) {
            // Handle exception if URI syntax is incorrect
            e.printStackTrace();
            return ""; // Return empty string if an error occurs
        }
    }

    public static void clearConfig() {
        try {
            // Get the path to the config.properties file
            String configFilePath = getJarDirectoryPath() + File.separator + "config.properties";
            File configFile = new File(configFilePath);

            // Check if the config.properties file exists
            if (configFile.exists() && configFile.isFile()) {
                // Delete the config.properties file
                try {
                    Files.deleteIfExists(configFile.toPath());
                    System.out.println("Deleted config.properties file.");
                } catch (IOException e) {
                    System.err.println("Failed to delete config.properties file: " + e.getMessage());
                }
            } else {
                System.out.println("config.properties file does not exist.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close the application after deleting the files
            Platform.exit();
        }
    }

    public static void setDisable(AnchorPane... panes) {
        for (AnchorPane pane : panes) {
            pane.setVisible(false);
            pane.setDisable(true);
        }
    }

    public void updateChartIfMismatch(BarChart<String, Number> chart) {
        XYChart.Series<String, Number> series = null;
        for (XYChart.Series<String, Number> s : chart.getData()) {
            if (s.getName().equals("Series 1")) {
                series = s;
                break;
            }
        }

        if (series != null) {
            for (int i = 0; i < series.getData().size(); i++) {
                XYChart.Data<String, Number> data = series.getData().get(i);
                int reportsCount = 0;
                switch (i) {
                    case 0: // Callout
                        reportsCount = CalloutReportLogs.countReports();
                        break;
                    case 1: // Arrests
                        reportsCount = ArrestReportLogs.countReports();
                        break;
                    case 2: // Traffic Stops
                        reportsCount = TrafficStopReportLogs.countReports();
                        break;
                    case 3: // Patrols
                        reportsCount = PatrolReportLogs.countReports();
                        break;
                    case 4: // Searches
                        reportsCount = SearchReportLogs.countReports();
                        break;
                    case 5: // Incidents
                        reportsCount = IncidentReportLogs.countReports();
                        break;
                    case 6: // Impounds
                        reportsCount = ImpoundReportLogs.countReports();
                        break;
                    case 7: // PCitations
                        reportsCount = ParkingCitationReportLogs.countReports();
                        break;
                    case 8: // TCitations
                        reportsCount = TrafficCitationReportLogs.countReports();
                        break;
                }
                if (data.getYValue().intValue() != reportsCount) {
                    // Update the data point to match the report count
                    data.setYValue(reportsCount);
                }
            }
        }
    }

    public void refreshChart() throws IOException {
        // Clear existing data from the chart
        reportChart.getData().clear();
        String[] categories = {"Callout", "Arrests", "Traffic Stops", "Patrols", "Searches", "Incidents", "Impounds", "Parking Cit.", "Traffic Cit."};
        CategoryAxis xAxis = (CategoryAxis) getReportChart().getXAxis();
        NumberAxis yAxis = (NumberAxis) getReportChart().getYAxis();

        // Setting font for X and Y axis labels
        Font axisLabelFont = Font.font("Segoe UI Bold", 11.5); // Change the font family and size as needed
        xAxis.setTickLabelFont(axisLabelFont);
        yAxis.setTickLabelFont(axisLabelFont);

        xAxis.setCategories(FXCollections.observableArrayList(Arrays.asList(categories)));
        yAxis.setAutoRanging(true);
        yAxis.setMinorTickVisible(false);
        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        series1.setName("Series 1");

        String color = ConfigReader.configRead("mainColor");
        for (String category : categories) {
            XYChart.Data<String, Number> data = new XYChart.Data<>(category, 1); // Value doesn't matter, just need to add a data point
            data.nodeProperty().addListener((obs, oldNode, newNode) -> {
                if (newNode != null) {
                    newNode.setStyle("-fx-bar-fill: " + color + ";");
                }
            });
            series1.getData().add(data);
        }

        getReportChart().getData().add(series1);
        getReportChart().setLegendVisible(false);
        getReportChart().getXAxis().setTickLabelGap(20);
    }

    public BarChart getReportChart() {
        return reportChart;
    }

    public void initialize() throws IOException {
        setDisable(infoPane, UISettingsPane, patrolReportPane, calloutReportPane, incidentReportPane, searchReportPane);
        setActive(shiftInformationPane);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("popOvers/DUIInformation.fxml"));
        loader.load();
        duiInformationController = loader.getController();

        refreshChart();
        updateChartIfMismatch(reportChart);
        loadTheme();
        String name = ConfigReader.configRead("Name");
        String division = ConfigReader.configRead("Division");
        String rank = ConfigReader.configRead("Rank");
        String number = ConfigReader.configRead("Number");
        String agency = ConfigReader.configRead("Agency");

        getOfficerInfoRank().getItems().addAll(dropdownInfo.ranks);
        getOfficerInfoDivision().getItems().addAll(dropdownInfo.divisions);
        getOfficerInfoDivision().setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> p) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText(null);
                        } else {
                            setText(item);
                            setAlignment(javafx.geometry.Pos.CENTER);
                        }
                    }
                };
            }
        });
        getOfficerInfoAgency().getItems().addAll(dropdownInfo.agencies);

        OfficerInfoName.setText(name);
        OfficerInfoDivision.setValue(division);
        OfficerInfoRank.setValue(rank);
        OfficerInfoAgency.setValue(agency);
        OfficerInfoNumber.setText(number);

        //Top Generations
        generatedByTag.setText("Generated By:" + " " + name);
        String time = DataTerminalHomeApplication.getTime();
        generatedDateTag.setText("Generated at: " + time);
    }

    public void setActive(AnchorPane pane) {
        pane.setVisible(true);
        pane.setDisable(false);
    }

    @javafx.fxml.FXML
    public void onNotesButtonClicked(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("notes-view.fxml"));
        Parent root = loader.load();
        Scene newScene = new Scene(root);
        stage.setTitle("Notes");
        stage.setScene(newScene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setResizable(true);
        stage.show();
        stage.setAlwaysOnTop(true);
        stage.setAlwaysOnTop(false);
        stage.centerOnScreen();
        stage.setMinHeight(stage.getHeight() - 150);
        stage.setMinWidth(stage.getWidth() - 150);
        ResizeHelper.addResizeListener(stage);
        stage.getScene().getStylesheets().add(getClass().getResource("css/notification-styles.css").toExternalForm());
    }

    public void showNotification(String title, String message) {
        Label label = new Label(message);

        VBox vbox1 = new VBox(label);
        vbox1.setAlignment(Pos.CENTER);

        // Create and show the notification
        Notifications.create()
                .title(title)
                .text(message)
                .graphic(null) // You can add a graphic if needed
                .position(Pos.TOP_RIGHT)
                .hideAfter(Duration.seconds(1.5))
                .owner(vbox)
                .show();
    }

    @javafx.fxml.FXML
    public void onShiftInfoBtnClicked(ActionEvent actionEvent) {
        setDisable(infoPane, UISettingsPane, patrolReportPane, calloutReportPane, incidentReportPane, searchReportPane);
        setActive(shiftInformationPane);
    }

    public ComboBox getOfficerInfoAgency() {
        return OfficerInfoAgency;
    }

    public ComboBox getOfficerInfoDivision() {
        return OfficerInfoDivision;
    }

    public TextField getOfficerInfoName() {
        return OfficerInfoName;
    }

    public TextField getOfficerInfoNumber() {
        return OfficerInfoNumber;
    }

    public ComboBox getOfficerInfoRank() {
        return OfficerInfoRank;
    }

    public AnchorPane getShiftInformationPane() {
        return shiftInformationPane;
    }

    public AnchorPane getInfoPane() {
        return infoPane;
    }

    @javafx.fxml.FXML
    public void onCalloutReportButtonClick(ActionEvent actionEvent) throws IOException {
        setDisable(shiftInformationPane, infoPane, UISettingsPane, patrolReportPane, incidentReportPane, searchReportPane);
        setActive(calloutReportPane);

        createSpinner(calloutReportSpinner, 0, 9999, 0);

        String name = ConfigReader.configRead("Name");
        String division = ConfigReader.configRead("Division");
        String rank = ConfigReader.configRead("Rank");
        String number = ConfigReader.configRead("Number");
        String agency = ConfigReader.configRead("Agency");

        calloutReportName.setText(name);
        calloutReportDivision.setText(division);
        calloutReportRank.setText(rank);
        calloutReportAgency.setText(agency);
        calloutReportNumber.setText(number);

        calloutReportDate.setText(DataTerminalHomeApplication.getDate());
        calloutReportTime.setText(DataTerminalHomeApplication.getTime());
    }

    @javafx.fxml.FXML
    public void onMapButtonClick(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("map-view.fxml"));
        Parent root = loader.load();
        Scene newScene = new Scene(root);
        stage.setTitle("Los Santos Map");
        stage.setScene(newScene);
        stage.initStyle(StageStyle.UTILITY);
        stage.setResizable(false);
        stage.show();
        stage.centerOnScreen();
    }

    @javafx.fxml.FXML
    public void updateInfoButtonClick(ActionEvent actionEvent) {
        if (getOfficerInfoAgency().getValue() == null || getOfficerInfoDivision().getValue() == null ||
                getOfficerInfoRank().getValue() == null || getOfficerInfoName().getText().isEmpty() ||
                getOfficerInfoNumber().getText().isEmpty()) {
            updatedNotification.setText("Fill Out Form.");
            updatedNotification.setStyle("-fx-text-fill: red;");
            updatedNotification.setVisible(true);
            Timeline timeline1 = new Timeline(new KeyFrame(Duration.seconds(1), evt -> {
                updatedNotification.setVisible(false);
            }));
            timeline1.play();
        } else {
            ConfigWriter.configwrite("Agency", getOfficerInfoAgency().getValue().toString());
            ConfigWriter.configwrite("Division", getOfficerInfoDivision().getValue().toString());
            ConfigWriter.configwrite("Name", getOfficerInfoName().getText());
            ConfigWriter.configwrite("Rank", getOfficerInfoRank().getValue().toString());
            ConfigWriter.configwrite("Number", getOfficerInfoNumber().getText());
            generatedByTag.setText("Generated By:" + " " + getOfficerInfoName().getText());
            updatedNotification.setText("updated.");
            updatedNotification.setStyle("-fx-text-fill: green;");
            updatedNotification.setVisible(true);
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), evt -> {
                updatedNotification.setVisible(false);
            }));
            timeline.play();
        }
    }

    @javafx.fxml.FXML
    public void onExitButtonClick(MouseEvent actionEvent) {
        Platform.exit();
    }

    @javafx.fxml.FXML
    public void onLogsButtonClick(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("logBrowser.fxml"));
        Parent root = loader.load();
        Scene newScene = new Scene(root);
        stage.setTitle("Log Browser");
        stage.setScene(newScene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setResizable(true);
        stage.getIcons().add(new Image(newOfficerApplication.class.getResourceAsStream("imgs/icons/terminal.png")));
        stage.show();
        stage.centerOnScreen();
        stage.setMinHeight(stage.getHeight() - 250);
        stage.setMinWidth(stage.getWidth() - 250);
        ResizeHelper.addResizeListener(stage);
    }

    @javafx.fxml.FXML
    public void trafficStopReportButtonClick(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("trafficStopReport-view.fxml"));
        Parent root = loader.load();
        Scene newScene = new Scene(root);
        stage.setTitle("Traffic Stop Report");
        stage.setScene(newScene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setResizable(false);
        stage.getIcons().add(new Image(newOfficerApplication.class.getResourceAsStream("imgs/icons/terminal.png")));
        stage.show();
        stage.centerOnScreen();
    }

    @javafx.fxml.FXML
    public void onIncidentReportBtnClick(ActionEvent actionEvent) throws IOException {
        setDisable(shiftInformationPane, infoPane, UISettingsPane, patrolReportPane, calloutReportPane, searchReportPane);
        setActive(incidentReportPane);

        String name = ConfigReader.configRead("Name");
        String division = ConfigReader.configRead("Division");
        String rank = ConfigReader.configRead("Rank");
        String number = ConfigReader.configRead("Number");
        String agency = ConfigReader.configRead("Agency");

        incidentofficerName.setText(name);
        incidentofficerDivision.setText(division);
        incidentofficerRank.setText(rank);
        incidentofficerAgency.setText(agency);
        incidentofficerNumber.setText(number);

        createSpinner(incidentNumber, 0, 9999, 0);
        incidentReportTime.setText(getTime());
        incidentReportdate.setText(getDate());
    }

    @javafx.fxml.FXML
    public void onSearchReportBtnClick(ActionEvent actionEvent) throws IOException {
        setDisable(shiftInformationPane, infoPane, UISettingsPane, patrolReportPane, calloutReportPane, incidentReportPane);
        setActive(searchReportPane);

        String name = ConfigReader.configRead("Name");
        String division = ConfigReader.configRead("Division");
        String rank = ConfigReader.configRead("Rank");
        String number = ConfigReader.configRead("Number");
        String agency = ConfigReader.configRead("Agency");

        searchType.getItems().addAll(dropdownInfo.searchTypes);
        searchMethod.getItems().addAll(dropdownInfo.searchMethods);

        searchofficerName.setText(name);
        searchofficerDivision.setText(division);
        searchofficerRank.setText(rank);
        searchofficerAgency.setText(agency);
        searchofficerNumber.setText(number);

        createSpinner(SearchNumber, 0, 9999, 0);
        searchTime.setText(getTime());
        searchDate.setText(getDate());
    }

    @javafx.fxml.FXML
    public void onTopCLick(MouseEvent mouseEvent) {
        xOffset = mouseEvent.getSceneX();
        yOffset = mouseEvent.getSceneY();
    }

    @javafx.fxml.FXML
    public void onTopDrag(MouseEvent mouseEvent) {
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.setX(mouseEvent.getScreenX() - xOffset);
        stage.setY(mouseEvent.getScreenY() - yOffset);
    }

    @javafx.fxml.FXML
    public void onArrestReportBtnClick(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("arrestReport-view.fxml"));
        Parent root = loader.load();
        Scene newScene = new Scene(root);
        stage.setTitle("Arrest Report");
        stage.setScene(newScene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setResizable(false);
        stage.getIcons().add(new Image(newOfficerApplication.class.getResourceAsStream("imgs/icons/terminal.png")));
        stage.show();
        stage.centerOnScreen();
    }

    @javafx.fxml.FXML
    public void onPatrolButtonClick(ActionEvent actionEvent) throws IOException {
        setDisable(shiftInformationPane, infoPane, UISettingsPane, calloutReportPane, incidentReportPane, searchReportPane);
        setActive(patrolReportPane);

        String name = ConfigReader.configRead("Name");
        String division = ConfigReader.configRead("Division");
        String rank = ConfigReader.configRead("Rank");
        String number = ConfigReader.configRead("Number");
        String agency = ConfigReader.configRead("Agency");
        patrolofficerName.setText(name);
        patrolofficerDivision.setText(division);
        patrolofficerRank.setText(rank);
        patrolofficerAgency.setText(agency);
        patrolofficerNumber.setText(number);
        createSpinner(patrolSpinnerNumber, 0, 9999, 0);
        patrolStopTime.setText(getTime());
        patrolDate.setText(getDate());
    }

    @javafx.fxml.FXML
    public void onCitationReportBtnClick(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("trafficCitationReport-view.fxml"));
        Parent root = loader.load();
        Scene newScene = new Scene(root);
        stage.setTitle("Citation Report");
        stage.setScene(newScene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setResizable(false);
        stage.getIcons().add(new Image(newOfficerApplication.class.getResourceAsStream("imgs/icons/terminal.png")));
        stage.show();
        stage.centerOnScreen();
    }

    @javafx.fxml.FXML
    public void onImpoundReportBtnClick(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("impoundReport-view.fxml"));
        Parent root = loader.load();
        Scene newScene = new Scene(root);
        stage.setTitle("Impound Report");
        stage.setScene(newScene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setResizable(false);
        stage.getIcons().add(new Image(newOfficerApplication.class.getResourceAsStream("imgs/icons/terminal.png")));
        stage.show();
        stage.centerOnScreen();
    }

    @javafx.fxml.FXML
    public void onParkingCitationReportBtnClick(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("parkingCitationReport-view.fxml"));
        Parent root = loader.load();
        Scene newScene = new Scene(root);
        stage.setTitle("Parking Citation Report");
        stage.setScene(newScene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setResizable(false);
        stage.getIcons().add(new Image(newOfficerApplication.class.getResourceAsStream("imgs/icons/terminal.png")));
        stage.show();
        stage.centerOnScreen();
    }

    @javafx.fxml.FXML
    public void aboutBtnClick(ActionEvent actionEvent) {
        setDisable(shiftInformationPane, UISettingsPane, calloutReportPane, patrolReportPane, incidentReportPane, searchReportPane);
        setActive(infoPane);
    }

    public void clearDataLogs() {
        try {
            // Get the path to the DataLogs folder
            String dataLogsFolderPath = getDataLogsFolderPath();

            // Print the path for debugging
            System.out.println("DataLogs folder path: " + dataLogsFolderPath);

            // Check if the DataLogs folder exists
            File dataLogsFolder = new File(dataLogsFolderPath);
            if (dataLogsFolder.exists() && dataLogsFolder.isDirectory()) {
                System.out.println("DataLogs folder exists.");

                // Get a list of files in the DataLogs folder
                File[] files = dataLogsFolder.listFiles();

                if (files != null) {
                    // Delete each file in the DataLogs folder
                    for (File file : files) {
                        if (file.isFile()) {
                            try {
                                Files.deleteIfExists(file.toPath());
                                System.out.println("Deleted file: " + file.getName());
                            } catch (IOException e) {
                                System.err.println("Failed to delete file: " + file.getName() + " " + e.getMessage());
                            }
                        }
                    }
                    System.out.println("All files in DataLogs folder deleted successfully.");
                } else {
                    System.out.println("DataLogs folder is empty.");
                }
            } else {
                System.out.println("DataLogs folder does not exist.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @javafx.fxml.FXML
    public void clearLogsBtnClick(ActionEvent actionEvent) {
        Stage stage = (Stage) vbox.getScene().getWindow();
        confirmLogClearDialog(stage);
    }

    @javafx.fxml.FXML
    public void clearAllSaveDataBtnClick(ActionEvent actionEvent) {
        Stage stage = (Stage) vbox.getScene().getWindow();
        confirmSaveDataClearDialog(stage);
    }

    @javafx.fxml.FXML
    public void UISettingsBtnClick(ActionEvent actionEvent) {
        showSettingsWindow();
    }

    private void showSettingsWindow() {
        // Create a new stage for the settings window
        Stage settingsStage = new Stage();
        settingsStage.setTitle("UI Settings");

        // Create color pickers for selecting colors
        Label primColor = primaryColor;
        Label secColor = secondaryColor;

        ColorPicker colorPicker1 = colorSelectMain;
        ColorPicker colorPicker2 = colorSelectSecondary;

        Button saveButton = resetDefaultsBtn;

        // Create a GridPane layout for the settings window
        GridPane root = new GridPane();
        root.setPadding(new Insets(20));
        root.setHgap(15);
        root.setVgap(15);
        root.addRow(0, primColor);
        root.addRow(1, colorPicker1);
        root.addRow(2, secColor);
        root.addRow(3, colorPicker2);
        root.add(saveButton, 0, 4);

        // Create the scene for the settings window
        Scene scene = new Scene(root);

        // Set the scene on the settings stage
        settingsStage.setScene(scene);

        // Show the settings window
        settingsStage.initStyle(StageStyle.DECORATED);
        settingsStage.setResizable(false);
        settingsStage.show();
    }

    @javafx.fxml.FXML
    public void onFullscreenBtnClick(Event event) {
        Stage stage = (Stage) vbox.getScene().getWindow();
        if (stage != null) {
            toggleWindowedFullscreen(stage, 1027, 740);
        }
    }

    @javafx.fxml.FXML
    public void onColorSelectMainPress(ActionEvent actionEvent) throws IOException {
        Color selectedColor = colorSelectMain.getValue();
        updateMain(selectedColor);
        loadTheme();
    }

    private void updateMain(Color color) {
        String hexColor = toHexString(color);
        ConfigWriter.configwrite("mainColor", hexColor);
    }

    public void changeBarColors(BarChart<String, Number> barChart, String color) {
        // Get the list of series from the bar chart
        ObservableList<XYChart.Series<String, Number>> seriesList = barChart.getData();

        // Iterate over each series
        for (XYChart.Series<String, Number> series : seriesList) {
            // Iterate over each data point in the series
            for (XYChart.Data<String, Number> data : series.getData()) {
                // Access the node representing the bar for the data point
                javafx.scene.Node node = data.getNode();
                // Set the style of the node to change the color of the bar
                node.setStyle("-fx-bar-fill: " + color + ";");
            }
        }
    }

    private void loadTheme() throws IOException {
        colorSelectMain.setValue(Color.valueOf(ConfigReader.configRead("mainColor")));
        colorSelectSecondary.setValue(Color.valueOf(ConfigReader.configRead("secondaryColor")));
        changeBarColors(getReportChart(), ConfigReader.configRead("mainColor"));
        //Main
        String mainclr = ConfigReader.configRead("mainColor");
        topPane.setStyle("-fx-background-color: " + mainclr + ";");
        mainColor6.setStyle("-fx-text-fill: " + mainclr + ";");
        mainColor8.setStyle("-fx-text-fill: " + mainclr + ";");
        mainColor7Bkg.setStyle("-fx-background-color: " + mainclr + ";");
        mainColor9Bkg.setStyle("-fx-background-color: " + mainclr + ";");
        primaryColor.setStyle("-fx-text-fill: " + mainclr + ";");
        //Secondary
        String secclr = ConfigReader.configRead("secondaryColor");
        secondaryColor.setStyle("-fx-text-fill: " + secclr + ";");
        sidepane.setStyle("-fx-background-color: " + secclr + ";");
        secondaryColor2.setStyle("-fx-text-fill: " + secclr + ";");
        secondaryColor3.setStyle("-fx-text-fill: " + secclr + ";");
        secondaryColor4.setStyle("-fx-text-fill: " + secclr + ";");
        secondaryColor5.setStyle("-fx-text-fill: " + secclr + ";");
        secondaryColor3Bkg.setStyle("-fx-background-color: " + secclr + ";");
        secondaryColor4Bkg.setStyle("-fx-background-color: " + secclr + ";");
        secondaryColor5Bkg.setStyle("-fx-background-color: " + secclr + ";");
        //Buttons
        String hoverStyle = "-fx-background-color: " + ConfigReader.configRead("mainColor");
        String initialStyle = "-fx-background-color: transparent;";
        String nonTransparentBtn = "-fx-background-color: " + ConfigReader.configRead("secondaryColor") + ";";
        updateInfoBtn.setStyle("-fx-background-color: " + ConfigReader.configRead("secondaryColor") + ";");
        resetDefaultsBtn.setStyle("-fx-background-color: " + ConfigReader.configRead("secondaryColor") + ";");

        // Add hover event handling
        shiftInfoBtn.setOnMouseEntered(e -> shiftInfoBtn.setStyle(hoverStyle));
        shiftInfoBtn.setOnMouseExited(e -> shiftInfoBtn.setStyle(initialStyle));
        settingsDropdown.setOnMouseEntered(e -> settingsDropdown.setStyle(hoverStyle));
        settingsDropdown.setOnMouseExited(e -> settingsDropdown.setStyle(initialStyle));
        notesButton.setOnMouseEntered(e -> notesButton.setStyle(hoverStyle));
        notesButton.setOnMouseExited(e -> notesButton.setStyle(initialStyle));
        createReportBtn.setOnMouseEntered(e -> createReportBtn.setStyle(hoverStyle));
        createReportBtn.setOnMouseExited(e -> createReportBtn.setStyle(initialStyle));
        logsButton.setOnMouseEntered(e -> logsButton.setStyle(hoverStyle));
        logsButton.setOnMouseExited(e -> logsButton.setStyle(initialStyle));
        mapButton.setOnMouseEntered(e -> mapButton.setStyle(hoverStyle));
        mapButton.setOnMouseExited(e -> mapButton.setStyle(initialStyle));
        updateInfoBtn.setOnMouseEntered(e -> updateInfoBtn.setStyle(hoverStyle));
        updateInfoBtn.setOnMouseExited(e -> {
            updateInfoBtn.setStyle(nonTransparentBtn);
        });
        resetDefaultsBtn.setOnMouseEntered(e -> resetDefaultsBtn.setStyle(hoverStyle));
        resetDefaultsBtn.setOnMouseExited(e -> {
            resetDefaultsBtn.setStyle(nonTransparentBtn);
        });
    }

    private void updateSecondary(Color color) {
        String hexColor = toHexString(color);
        ConfigWriter.configwrite("secondaryColor", hexColor);
    }

    private String toHexString(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }

    @javafx.fxml.FXML
    public void onColorSelectSecondaryPress(ActionEvent actionEvent) throws IOException {
        Color selectedColor = colorSelectSecondary.getValue();
        updateSecondary(selectedColor);
        loadTheme();
    }

    @javafx.fxml.FXML
    public void resetDefaultsBtnPress(ActionEvent actionEvent) throws IOException {
        updateMain(Color.valueOf("#524992"));
        updateSecondary(Color.valueOf("#665cb6"));
        loadTheme();
    }

    @javafx.fxml.FXML
    public void testBtnPress(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("testWindow-view.fxml"));
        Parent root = loader.load();
        Scene newScene = new Scene(root);
        stage.setTitle("Test Window");
        stage.setScene(newScene);
        stage.initStyle(StageStyle.UTILITY);
        stage.setResizable(false);
        stage.getIcons().add(new Image(newOfficerApplication.class.getResourceAsStream("imgs/icons/terminal.png")));
        stage.show();
        stage.centerOnScreen();
    }

    @javafx.fxml.FXML
    public void onCalloutReportSubmitBtnClick(ActionEvent actionEvent) {
        if (calloutReportSpinner.getValue() == null) {
            calloutincompleteLabel.setText("Fill Out Form.");
            calloutincompleteLabel.setStyle("-fx-text-fill: red;");
            calloutincompleteLabel.setVisible(true);
            Timeline timeline1 = new Timeline(new KeyFrame(Duration.seconds(1), evt -> {
                calloutincompleteLabel.setVisible(false);
            }));
            timeline1.play();
        } else {// Load existing logs from XML
            List<CalloutLogEntry> logs = CalloutReportLogs.loadLogsFromXML();

            // Add new entry
            logs.add(new CalloutLogEntry(
                    calloutReportDate.getText(),
                    calloutReportTime.getText(),
                    calloutReportName.getText(),
                    calloutReportRank.getText(),
                    calloutReportNumber.getText(),
                    calloutReportDivision.getText(),
                    calloutReportAgency.getText(),
                    calloutReportResponeType.getText(),
                    calloutReportResponseGrade.getText(),
                    calloutReportSpinner.getValue().toString(),
                    calloutReportNotesTextArea.getText(),
                    calloutReportResponseAddress.getText(),
                    calloutReportResponseCounty.getText(),
                    calloutReportResponseArea.getText()

            ));
            // Save logs to XML
            CalloutReportLogs.saveLogsToXML(logs);
            setActive(shiftInformationPane);
            setDisable(calloutReportPane);
            updateChartIfMismatch(reportChart);
            calloutReportDate.setText("");
            calloutReportTime.setText("");
            calloutReportName.setText("");
            calloutReportRank.setText("");
            calloutReportNumber.setText("");
            calloutReportDivision.setText("");
            calloutReportAgency.setText("");
            calloutReportResponeType.setText("");
            calloutReportResponseGrade.setText("");
            calloutReportSpinner.getEditor().setText("0");
            calloutReportNotesTextArea.setText("");
            calloutReportResponseAddress.setText("");
            calloutReportResponseCounty.setText("");
            calloutReportResponseArea.setText("");
            showNotification("Reports", "A new Callout Report has been submitted.");
        }
    }

    @javafx.fxml.FXML
    public void onPatrolReportSubmitBtnClick(ActionEvent actionEvent) {
        if (patrolSpinnerNumber.getValue() == null) {
            patrolincompleteLabel.setText("Fill Out Form.");
            patrolincompleteLabel.setStyle("-fx-text-fill: red;");
            patrolincompleteLabel.setVisible(true);
            Timeline timeline1 = new Timeline(new KeyFrame(Duration.seconds(1), evt -> {
                patrolincompleteLabel.setVisible(false);
            }));
            timeline1.play();
        } else {
            // Load existing logs from XML
            List<PatrolLogEntry> logs = PatrolReportLogs.loadLogsFromXML();

            // Add new entry
            logs.add(new PatrolLogEntry(
                    patrolSpinnerNumber.getValue().toString(),
                    patrolDate.getText(),
                    patrolLength.getText(),
                    patrolStartTime.getText(),
                    patrolStopTime.getText(),
                    patrolofficerRank.getText(),
                    patrolofficerName.getText(),
                    patrolofficerNumber.getText(),
                    patrolofficerDivision.getText(),
                    patrolofficerAgency.getText(),
                    patrolofficerVehicle.getText(),
                    patrolComments.getText()
            ));
            // Save logs to XML
            PatrolReportLogs.saveLogsToXML(logs);
            setActive(shiftInformationPane);
            setDisable(patrolReportPane);
            updateChartIfMismatch(reportChart);
            showNotification("Reports", "A new Patrol Report has been submitted.");
            patrolSpinnerNumber.getEditor().setText("0");
            patrolDate.setText("");
            patrolLength.setText("");
            patrolStartTime.setText("");
            patrolStopTime.setText("");
            patrolofficerRank.setText("");
            patrolofficerName.setText("");
            patrolofficerNumber.setText("");
            patrolofficerDivision.setText("");
            patrolofficerAgency.setText("");
            patrolofficerVehicle.setText("");
            patrolComments.setText("");
        }
    }

    @javafx.fxml.FXML
    public void onIncidentReportSubmitBtnClick(ActionEvent actionEvent) {
        if (incidentNumber.getValue() == null) {
            incidentincompleteLabel.setText("Fill Out Form.");
            incidentincompleteLabel.setStyle("-fx-text-fill: red;");
            incidentincompleteLabel.setVisible(true);
            Timeline timeline1 = new Timeline(new KeyFrame(Duration.seconds(1), evt -> {
                incidentincompleteLabel.setVisible(false);
            }));
            timeline1.play();
        } else {
            // Load existing logs from XML
            List<IncidentLogEntry> logs = IncidentReportLogs.loadLogsFromXML();

            // Add new entry
            logs.add(new IncidentLogEntry(
                    incidentNumber.getValue().toString(),
                    incidentReportdate.getText(),
                    incidentReportTime.getText(),
                    incidentStatement.getText(),
                    incidentWitnesses.getText(),
                    incidentVictims.getText(),
                    incidentofficerName.getText(),
                    incidentofficerRank.getText(),
                    incidentofficerNumber.getText(),
                    incidentofficerAgency.getText(),
                    incidentofficerDivision.getText(),
                    incidentStreet.getText(),
                    incidentArea.getText(),
                    incidentCounty.getText(),
                    incidentActionsTaken.getText(),
                    incidentComments.getText()
            ));

            // Save logs to XML
            IncidentReportLogs.saveLogsToXML(logs);
            setActive(shiftInformationPane);
            setDisable(incidentReportPane);
            updateChartIfMismatch(reportChart);
            showNotification("Reports", "A new Incident Report has been submitted.");
            incidentNumber.getEditor().setText("0");
            incidentReportdate.setText("");
            incidentReportTime.setText("");
            incidentStatement.setText("");
            incidentWitnesses.setText("");
            incidentVictims.setText("");
            incidentofficerName.setText("");
            incidentofficerRank.setText("");
            incidentofficerNumber.setText("");
            incidentofficerAgency.setText("");
            incidentofficerDivision.setText("");
            incidentStreet.setText("");
            incidentArea.setText("");
            incidentCounty.setText("");
            incidentActionsTaken.setText("");
            incidentComments.setText("");
        }
    }

    private void confirmLogClearDialog(Stage ownerStage) {
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.initOwner(ownerStage);
        dialog.setTitle("Confirm Action");
        dialog.initModality(Modality.APPLICATION_MODAL);

        Label messageLabel = new Label("Are you sure you want to perform this action?\nThis will clear all your logs.");
        Button yesButton = new Button("Yes");
        yesButton.setOnAction(e -> {
            dialog.setResult(true);
            dialog.close();
        });
        Button noButton = new Button("No");
        noButton.getStyleClass().add("menuButton");
        noButton.setOnAction(e -> {
            dialog.setResult(false);
            dialog.close();
        });

        dialog.getDialogPane().setContent(new VBox(10, messageLabel, yesButton, noButton));

        dialog.showAndWait().ifPresent(result -> {
            if (result) {
                clearDataLogs();
                updateChartIfMismatch(reportChart);
            } else {
            }
        });
    }

    private void confirmSaveDataClearDialog(Stage ownerStage) {
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.initOwner(ownerStage);
        dialog.setTitle("Confirm Action");
        dialog.initModality(Modality.APPLICATION_MODAL);

        Label messageLabel = new Label("Are you sure you want to perform this action?\nThis will remove all save data including logs and config.");
        Button yesButton = new Button("Yes");
        yesButton.setOnAction(e -> {
            dialog.setResult(true);
            dialog.close();
        });
        Button noButton = new Button("No");
        noButton.getStyleClass().add("menuButton");
        noButton.setOnAction(e -> {
            dialog.setResult(false);
            dialog.close();
        });

        dialog.getDialogPane().setContent(new VBox(10, messageLabel, yesButton, noButton));

        dialog.showAndWait().ifPresent(result -> {
            if (result) {
                clearDataLogs();
                clearConfig();
            } else {
            }
        });
    }

    @javafx.fxml.FXML
    public void onSearchPopOverBtnPress(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("popOvers/DUIInformation.fxml"));
        Parent root = loader.load();
        duiInformationController = loader.getController();

        // Create a PopOver and set the content node
        PopOver popOver = new PopOver();
        popOver.setContentNode(root);

        // Optionally configure other properties of the PopOver
        popOver.setDetachable(false);
        popOver.setArrowLocation(PopOver.ArrowLocation.BOTTOM_RIGHT);
        popOver.cornerRadiusProperty().setValue(23);
        popOver.setFadeInDuration(Duration.seconds(0.5));
        popOver.setFadeOutDuration(Duration.seconds(0.4));
        popOver.setTitle("Breathalyzer Information");
        popOver.setHeaderAlwaysVisible(true);
        popOver.show(searchpopOverBtn);
    }

    @javafx.fxml.FXML
    public void onSearchReportSubmitBtnClick(ActionEvent actionEvent) {
        if (SearchNumber.getValue() == null
                || searchType.getSelectionModel().isEmpty() || searchType.getValue() == null
                || searchMethod.getSelectionModel().isEmpty() || searchMethod.getValue() == null) {
            searchincompleteLabel.setText("Fill Out Form.");
            searchincompleteLabel.setStyle("-fx-text-fill: red;");
            searchincompleteLabel.setVisible(true);
            Timeline timeline1 = new Timeline(new KeyFrame(Duration.seconds(1), evt -> {
                searchincompleteLabel.setVisible(false);
            }));
            timeline1.play();
        } else {
            // Load existing logs from XML
            List<SearchLogEntry> logs = SearchReportLogs.loadLogsFromXML();
            String BreathalyzerUsed;
            String ResultPassFail;
            String BACMeasurement = duiInformationController.getBacMeasure().getText();

            if (duiInformationController.getBreathalyzerUsedNo().isSelected()) {
                BreathalyzerUsed = "no";
                ResultPassFail = "n/a";
                BACMeasurement = "n/a";
            } else {
                BreathalyzerUsed = "yes";
                if (duiInformationController.getBreathalyzerResultFail().isSelected()) {
                    ResultPassFail = "Failed";
                } else {
                    ResultPassFail = "Passed";
                }
            }

            // Add new entry
            logs.add(new SearchLogEntry(
                    SearchNumber.getValue().toString(),
                    searchedPersons.getText(),
                    searchDate.getText(),
                    searchTime.getText(),
                    searchSeizedItems.getText(),
                    searchGrounds.getText(),
                    searchType.getValue().toString(),
                    searchMethod.getValue().toString(),
                    searchWitnesses.getText(),
                    searchofficerRank.getText(),
                    searchofficerName.getText(),
                    searchofficerNumber.getText(),
                    searchofficerAgency.getText(),
                    searchofficerDivision.getText(),
                    searchStreet.getText(),
                    searchArea.getText(),
                    searchCounty.getText(),
                    searchComments.getText(),
                    BreathalyzerUsed,
                    ResultPassFail,
                    BACMeasurement
            ));

            // Save logs to XML
            SearchReportLogs.saveLogsToXML(logs);
            setActive(shiftInformationPane);
            setDisable(searchReportPane);
            updateChartIfMismatch(reportChart);
            SearchNumber.getEditor().setText("0");
            searchedPersons.setText("");
            searchDate.setText("");
            searchTime.setText("");
            searchSeizedItems.setText("");
            searchGrounds.setText("");
            searchWitnesses.setText("");
            searchofficerRank.setText("");
            searchofficerName.setText("");
            searchofficerNumber.setText("");
            searchofficerAgency.setText("");
            searchofficerDivision.setText("");
            searchStreet.setText("");
            searchArea.setText("");
            searchCounty.setText("");
            searchComments.setText("");
            showNotification("Reports", "A new Search Report has been submitted.");
        }
    }
}