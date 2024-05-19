package com.drozal.dataterminal.util.server;

import com.drozal.dataterminal.CurrentIDViewController;
import com.drozal.dataterminal.actionController;
import com.drozal.dataterminal.calloutController;
import com.drozal.dataterminal.config.ConfigWriter;
import com.drozal.dataterminal.util.LogUtils;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.drozal.dataterminal.actionController.CalloutStage;
import static com.drozal.dataterminal.actionController.IDStage;
import static com.drozal.dataterminal.util.LogUtils.log;
import static com.drozal.dataterminal.util.stringUtil.getJarPath;

public class ClientUtils {
    private static final int TIMEOUT_SECONDS = 10; // Timeout in seconds
    public static Boolean isConnected = false;
    public static String port;
    public static String inet;
    private static Socket socket = null;
    private static ServerStatusListener statusListener;

    public static void disconnectFromService() {
        try {
            isConnected = false;
            notifyStatusChanged(isConnected);
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            log("Error disconnecting from service: " + e.getMessage(), LogUtils.Severity.ERROR);
        }
    }

    /**
     * Connects to the specified service address and port.
     * If a previous socket connection exists, it is closed before establishing a new connection.
     * Upon successful connection, initiates a background thread for receiving messages from the server.
     * Updates the configuration with the last used service address and port upon successful connection.
     *
     * @param serviceAddress the IP address or hostname of the service
     * @param servicePort    the port number of the service
     * @throws IOException if an I/O error occurs while connecting to the service
     */
    public static void connectToService(String serviceAddress, int servicePort) throws IOException {
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
        new Thread(() -> {
            try {
                socket = new Socket();
                Platform.runLater(() -> {
                    actionController.clientController.getStatusLabel().setText("Testing Connection...");
                    actionController.clientController.getStatusLabel().setStyle("-fx-background-color: orange;");
                });

                socket.connect(new InetSocketAddress(serviceAddress, servicePort), 10000);
                socket.setSoTimeout(10000);

                isConnected = true;
                notifyStatusChanged(isConnected);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                Thread readerThread = new Thread(() -> receiveMessages(in));
                readerThread.start();

                log("CONNECTED: " + serviceAddress + ":" + servicePort, LogUtils.Severity.INFO);
                port = String.valueOf(servicePort);
                inet = serviceAddress;
                ConfigWriter.configwrite("lastIPV4Connection", serviceAddress);
                ConfigWriter.configwrite("lastPortConnection", String.valueOf(servicePort));
            } catch (IOException e) {
                isConnected = false;
                notifyStatusChanged(isConnected);
                log("Failed to connect: " + e.getMessage(), LogUtils.Severity.ERROR);
            }
        }).start();
        // Start a separate thread to check for server timeout
        /* TODO: temp removed */
            /*new Thread(() -> {
            ScheduledExecutorService timeoutExecutor = Executors.newScheduledThreadPool(1);
            timeoutExecutor.scheduleAtFixedRate(() -> {
                try {
                    if (socket != null && !socket.isClosed()) {
                        // Send a heartbeat message and wait for a response
                        PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
                        writer.println("HEARTBEAT");
                        log("sent heartbeat", LogUtils.Severity.DEBUG);
                        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        String response = reader.readLine();
                        if (response == null) {
                            // Server did not respond
                            log("heartbeat not received", LogUtils.Severity.ERROR);
                            isConnected = false;
                            notifyStatusChanged(isConnected);
                            // Attempt to reconnect
                            connectToService(serviceAddress, servicePort);
                            timeoutExecutor.shutdown(); // Stop the timeout checker
                        }
                    } else {
                        // Socket is closed, stop the timeout checker
                        timeoutExecutor.shutdown();
                    }
                } catch (IOException e) {
                    System.err.println("Error checking server connection: " + e.getMessage());
                }
            }, TIMEOUT_SECONDS, TIMEOUT_SECONDS, TimeUnit.SECONDS);
        }).start();*/
    }

    /**
     * Receives messages from the server through the provided BufferedReader.
     * Upon receiving an "UPDATE_ID" message, performs actions such as file retrieval and UI updates.
     *
     * @param in the BufferedReader connected to the server's input stream
     */
    public static void receiveMessages(BufferedReader in) {
        new Thread(() -> {
            try {
                String fromServer;
                label:
                while ((fromServer = in.readLine()) != null) {
                    System.out.println("reading line");
                    switch (fromServer) {
                        case "SHUTDOWN":
                            log("Received shutdown message from server. Disconnecting...", LogUtils.Severity.DEBUG);
                            disconnectFromService(); // Disconnect from the server

                            break label; // Exit the loop
                        case "UPDATE_ID":
                            log("Received ID update message from server.", LogUtils.Severity.DEBUG);
                            FileUtlis.receiveFileFromServer(inet, Integer.parseInt(port), getJarPath() + File.separator + "serverData" + File.separator + "currentID.xml", 4096);
                            Platform.runLater(() -> {
                                if (IDStage != null && IDStage.isShowing()) {
                                    IDStage.toFront();
                                    IDStage.requestFocus();
                                    return;
                                }
                                IDStage = new Stage();
                                FXMLLoader loader = new FXMLLoader(actionController.class.getResource("currentID-view.fxml"));
                                Parent root = null;
                                try {
                                    root = loader.load();
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                                Scene newScene = new Scene(root);
                                AnchorPane topbar = CurrentIDViewController.getTitleBar();
                                IDStage.setTitle("Current ID");
                                IDStage.setScene(newScene);
                                IDStage.show();
                                IDStage.centerOnScreen();

                                IDStage.setOnHidden(new EventHandler<WindowEvent>() {
                                    @Override
                                    public void handle(WindowEvent event) {
                                        IDStage = null;
                                    }
                                });
                            });
                            break;
                        case "UPDATE_CALLOUT":
                            log("Received Callout update message from server.", LogUtils.Severity.DEBUG);
                            FileUtlis.receiveFileFromServer(inet, Integer.parseInt(port), getJarPath() + File.separator + "serverData" + File.separator + "callout.xml", 4096);
                            Platform.runLater(() -> {
                                if (CalloutStage != null && CalloutStage.isShowing()) {
                                    CalloutStage.toFront();
                                    CalloutStage.requestFocus();
                                    return;
                                }
                                CalloutStage = new Stage();
                                FXMLLoader loader = new FXMLLoader(actionController.class.getResource("callout-view.fxml"));
                                Parent root = null;
                                try {
                                    root = loader.load();
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                                Scene newScene = new Scene(root);
                                CalloutStage.setTitle("Callout Display");
                                CalloutStage.setScene(newScene);
                                CalloutStage.show();
                                CalloutStage.centerOnScreen();

                                CalloutStage.setOnHidden(new EventHandler<WindowEvent>() {
                                    @Override
                                    public void handle(WindowEvent event) {
                                        CalloutStage = null;
                                    }
                                });
                            });
                            break;
                        case "UPDATE_WORLD_PED":
                            log("Received World Ped update message from server.", LogUtils.Severity.DEBUG);
                            FileUtlis.receiveFileFromServer(inet, Integer.parseInt(port), getJarPath() + File.separator + "serverData" + File.separator + "worldPeds.data", 4096);
                            break;
                        case "UPDATE_WORLD_VEH":
                            log("Received World Veh update message from server.", LogUtils.Severity.DEBUG);
                            FileUtlis.receiveFileFromServer(inet, Integer.parseInt(port), getJarPath() + File.separator + "serverData" + File.separator + "worldCars.data", 4096);
                            break;
                        case "HEARTBEAT":
                            log("heartbeat recieved, message: "+fromServer, LogUtils.Severity.DEBUG);
                            break;
                    }
                }
            } catch (SocketException e) {
                isConnected = false;
                notifyStatusChanged(isConnected);
                log("Server Disconnected", LogUtils.Severity.ERROR);
            } catch (IOException e) {
                isConnected = false;
                notifyStatusChanged(isConnected);
                log("Error reading from server: " + e.getMessage(), LogUtils.Severity.ERROR);
            }
        }).start();
    }

    /**
     * Sets the status listener for monitoring connection status changes.
     *
     * @param statusListener the listener for connection status changes
     */
    public static void setStatusListener(ServerStatusListener statusListener) {
        ClientUtils.statusListener = statusListener;
    }

    /**
     * Notifies the status listener about the change in connection status.
     * Executes the notification on the JavaFX application thread.
     *
     * @param isConnected the current connection status
     */
    public static void notifyStatusChanged(boolean isConnected) {
        if (statusListener != null) {
            Platform.runLater(() -> statusListener.onStatusChanged(isConnected));
        }
    }

    /**
     * Sends information to the server over the established socket connection.
     * If the socket is not connected, logs an error.
     *
     * @param data the data to send to the server
     * @throws IOException if an I/O error occurs while sending data to the server
     */
    public static void sendInfoToServer(String data) throws IOException {
        if (socket != null) {
            OutputStream outputStream = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(outputStream, true);
            writer.println(data);
        } else {
            LogUtils.logError("Socket is not connected.", null);
        }
    }

}