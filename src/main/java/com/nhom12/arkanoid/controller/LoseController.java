package com.nhom12.arkanoid.controller;

import com.nhom12.arkanoid.model.GameResult;
import com.nhom12.arkanoid.utils.ScreenManager;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class LoseController {

    @FXML
    private StackPane lose;

    @FXML
    private Text scoreText;
    @FXML
    private Text timeText;
    @FXML
    private Text highScoreText;

    @FXML
    public void initialize() {
        GameResult result = (GameResult) ScreenManager.getData();
        scoreText.setText("Score: " + result.score);
        timeText.setText("Time: " + result.timeInSeconds + "s");

        int highScore = HighScoreController.getHighScore();
        highScoreText.setText("Highscore: " + highScore);
    }

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
        ScreenManager.switchScene("/view/game.fxml","Arkanoid");
    }
}
