package com.nhom12.arkanoid.controller;

import com.nhom12.arkanoid.utils.ScreenManager;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.List;
import java.util.prefs.Preferences;
import java.util.ArrayList;
import java.util.Collections;

public class HighScoreController {

    @FXML
    private VBox scoreListVBox;

    // Preferences key for storing scores
    private static final String PREFS_KEY_HIGH_SCORES = "highScores";

    private final Preferences prefs = Preferences.userNodeForPackage(HighScoreController.class);

    public void initialize() {
        // Load high scores and display them
        List<Integer> highScores = loadHighScores();

        int rank = 1;
        for (Integer score : highScores) {
            Text scoreText = new Text(rank + ". " + score + " points");
            scoreText.setStyle("-fx-font-size: 18px; -fx-fill: white;");
            scoreListVBox.getChildren().add(scoreText);
            rank++;
        }
    }

    // Load scores from preferences
    private List<Integer> loadHighScores() {
        String highScoresString = prefs.get(PREFS_KEY_HIGH_SCORES, "");
        List<Integer> highScores = new ArrayList<>();

        if (!highScoresString.isEmpty()) {
            String[] scoresArray = highScoresString.split(",");
            for (String score : scoresArray) {
                try {
                    highScores.add(Integer.parseInt(score.trim()));
                } catch (NumberFormatException e) {
                    System.err.println("Invalid score: " + score);
                }
            }
        }

        Collections.sort(highScores, Collections.reverseOrder());
        return highScores;
    }

    // Save a new score (you can pass a Text or int)
    public void saveScore(Text scoreText) {
        try {
            int score = Integer.parseInt(scoreText.getText().trim());
            saveScore(score);
        } catch (NumberFormatException e) {
            System.err.println("Invalid Text content: must be a number");
        }
    }

    // Save a numeric score
    public void saveScore(int score) {
        List<Integer> highScores = loadHighScores();
        highScores.add(score);

        // Sort and limit to top 10
        Collections.sort(highScores, Collections.reverseOrder());
        if (highScores.size() > 5) {
            highScores = highScores.subList(0, 5);
        }

        // Convert to comma-separated string
        StringBuilder scoresStringBuilder = new StringBuilder();
        for (int s : highScores) {
            scoresStringBuilder.append(s).append(",");
        }
        if (scoresStringBuilder.length() > 0) {
            scoresStringBuilder.setLength(scoresStringBuilder.length() - 1);
        }

        prefs.put(PREFS_KEY_HIGH_SCORES, scoresStringBuilder.toString());
    }

    @FXML
    private void onBackClicked() {
        ScreenManager.switchScene("/view/menu.fxml", "Arkanoid Menu");
    }
}
