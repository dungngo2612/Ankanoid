package com.nhom12.arkanoid;

import com.nhom12.arkanoid.utils.ScreenManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        ScreenManager.setStage(stage);
        ScreenManager.switchScene("/view/menu.fxml", "Arkanoid Classic");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
