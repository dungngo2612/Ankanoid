package com.nhom12.arkanoid.controller;

import com.nhom12.arkanoid.utils.ScreenManager;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;

public class WinController {

    @FXML
    private StackPane win;

    @FXML
    private void onMenuClicked() {
        System.out.println("Returning to menu...");
        ScreenManager.switchScene("/view/menu.fxml", "Arkanoid Classic");
    }

    @FXML
    private void onHighscoreClicked() {
        System.out.println("Highscore clicked!");
        ScreenManager.switchScene("/view/highscore.fxml","Arkanoid");
    }

    @FXML
    private void onAgainClicked() {
        System.out.println("Again clicked!");
        ScreenManager.switchScene("/view/game.fxml", "Arkanoid");
    }
}
