package com.nhom12.arkanoid.controller;

import com.nhom12.arkanoid.model.GameResult;
import com.nhom12.arkanoid.controller.HighScoreController;
import com.nhom12.arkanoid.utils.ScreenManager;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class WinController {

    @FXML
    private StackPane win;

    @FXML
    private Text scoreText;
    @FXML
    private Text timeText;
    @FXML
    public void initialize() {
        GameResult result = (GameResult) ScreenManager.getData();
        scoreText.setText("Score: " + result.score);
        timeText.setText("Time: " + result.timeInSeconds + "s");

        System.out.println("Time received: " + result.timeInSeconds);
    }

    @FXML
    private void onMenuClicked() {
        ScreenManager.switchScene("/view/menu.fxml", "Arkanoid Classic");
    }

    @FXML
    private void onHighscoreClicked() {
        ScreenManager.switchScene("/view/highscore.fxml","Arkanoid");
    }

    @FXML
    private void onAgainClicked() {
        ScreenManager.switchScene("/view/game.fxml", "Arkanoid");
    }
}