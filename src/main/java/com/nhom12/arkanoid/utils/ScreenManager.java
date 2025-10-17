package com.nhom12.arkanoid.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class ScreenManager {

    private static Stage primaryStage;

    // Initialize once in Main
    public static void setStage(Stage stage) {
        primaryStage = stage;
    }

    public static void switchScene(String fxmlPath, String title) {
        try {
            URL resource = ScreenManager.class.getResource(fxmlPath);
            if (resource == null) {
                throw new IOException("FXML file not found: " + fxmlPath);
            }

            Parent root = FXMLLoader.load(resource);
            Scene scene = new Scene(root, 1280, 720);

            primaryStage.setTitle(title);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
