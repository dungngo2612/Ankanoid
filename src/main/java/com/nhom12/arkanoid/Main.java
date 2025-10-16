package com.nhom12.arkanoid;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/view/menu.fxml")
        );
        Scene scene = new Scene(loader.load(), 1280, 720);
        stage.setScene(scene);
        stage.setTitle("Arkanoid Menu");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
