package com.example.luckysix;

import com.example.luckysix.database.Database;
import com.example.luckysix.networking.Client;
import com.example.luckysix.scenes.TicketScene;
import javafx.scene.control.Alert;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;

public class Application extends javafx.application.Application {
    public static Stage mainStage;
    public static final int WIDTH = (int) Screen.getPrimary().getBounds().getWidth();
    public static final int HEIGHT = (int) Screen.getPrimary().getBounds().getHeight();

    /**
     * Inicijalizuje i pokreće aplikaciju sa zadatim prozorom (Stage).
     */
    @Override
    public void start(Stage stage) throws IOException {
        mainStage = stage;
        stage.setMaximized(true);
        stage.setTitle("Hello!");
        stage.setScene(new TicketScene());
        stage.setResizable(false);
        try {
            Database.connect();
            Client.connect();
        } catch (IOException | ClassNotFoundException | SQLException e) {
            Database.disconnect();
            Client.disconnect();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }
        stage.show();
    }

    /**
     * Zaustavlja izvršavanje aplikacije.
     */
    @Override
    public void stop() throws Exception {
        Client.disconnect();
        Database.disconnect();
        super.stop();
    }

    /**
     * Glavna metoda koja pokreće JavaFX aplikaciju.
     */
    public static void main(String[] args) {
        launch();
    }
}