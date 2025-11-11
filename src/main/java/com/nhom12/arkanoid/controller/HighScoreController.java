package com.nhom12.arkanoid.controller;

import com.nhom12.arkanoid.utils.ScreenManager;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.Arrays;
import java.util.List;
import java.util.prefs.Preferences;
import java.util.ArrayList;
import java.util.Collections;

public class HighScoreController {

    @FXML
    private VBox scoreListVBox;
/**
    @FXML
    private void onClearClicked() {
        clearHighScores();
    }
**/
    // Preferences key for storing scores
    private static final String PREFS_KEY_HIGH_SCORES = "highScores";

    private final Preferences prefs = Preferences.userNodeForPackage(HighScoreController.class);

    public static int getHighScore() {
        Preferences prefs = Preferences.userNodeForPackage(HighScoreController.class);
        return prefs.getInt("highscore", 0);
    }

    public void initialize() {
        // Load high scores and display them
        List<String> highScores = prefs.get(PREFS_KEY_HIGH_SCORES, "").isEmpty()
                ? new ArrayList<>()
                : Arrays.asList(prefs.get(PREFS_KEY_HIGH_SCORES, "").split(","));

        int rank = 1;
        for (String entry : highScores) {
            String[] parts = entry.split("\\|");
            if (parts.length == 2) {
                int score = Integer.parseInt(parts[0]);
                long time = Long.parseLong(parts[1]);

                Text scoreText = new Text(rank + ". " + score + " points - " + time + "s");
                scoreText.setStyle("-fx-font-size: 24px; -fx-fill: gold;" +
                        "-fx-effect: dropshadow(gaussian, black, 10, 0.5, 0, 0);");
                scoreListVBox.getChildren().add(scoreText);
                rank++;
            } else {
                System.err.println("Invalid highscore entry: " + entry);
            }
        }
    }

    // Load scores from preferences
    private List<String> loadHighScores() {
        String highScoresString = prefs.get(PREFS_KEY_HIGH_SCORES, "");
        List<String> highScores = new ArrayList<>();

        if (!highScoresString.isEmpty()) {
            String[] scoresArray = highScoresString.split(",");
            for (String entry : scoresArray) {
                if (entry.contains("|")) {
                    highScores.add(entry);
                } else {
                    System.err.println("Invalid score entry: " + entry);
                }
            }
        }

        // Sort descending by score
        highScores.sort((a, b) -> {
            int scoreA = Integer.parseInt(a.split("\\|")[0]);
            int scoreB = Integer.parseInt(b.split("\\|")[0]);
            return Integer.compare(scoreB, scoreA);
        });

        return highScores;
    }

    // Save a new score (you can pass a Text or int)
    public void saveScore(int score, long timeInSeconds) {
        String newEntry = score + "|" + timeInSeconds;

        String highScoresString = prefs.get(PREFS_KEY_HIGH_SCORES, "");
        List<String> highScores = new ArrayList<>();

        if (!highScoresString.isEmpty()) {
            String[] entries = highScoresString.split(",");
            for (String entry : entries) {
                if (entry.contains("|")) {
                    highScores.add(entry);
                }
            }
        }

        highScores.add(newEntry);

        highScores.sort((a, b) -> {
            int scoreA = Integer.parseInt(a.split("\\|")[0]);
            int scoreB = Integer.parseInt(b.split("\\|")[0]);
            return Integer.compare(scoreB, scoreA);
        });

        if (highScores.size() > 5) {
            highScores = highScores.subList(0, 5);
        }

        StringBuilder builder = new StringBuilder();
        for (String entry : highScores) {
            builder.append(entry).append(",");
        }
        if (builder.length() > 0) {
            builder.setLength(builder.length() - 1);
        }

        prefs.put(PREFS_KEY_HIGH_SCORES, builder.toString());
        prefs.putInt("highscore", Integer.parseInt(highScores.get(0).split("\\|")[0]));
    }

    /** Save a numeric score
    public void saveScore(int score) {
        List<String> highScores = loadHighScores();
        highScores.add(String.valueOf(score));

        // Sort and limit to top 10
        Collections.sort(highScores, Collections.reverseOrder());
        if (highScores.size() > 5) {
            highScores = highScores.subList(0, 5);
        }

        // Convert to comma-separated string
        StringBuilder scoresStringBuilder = new StringBuilder();
        for (String s : highScores) {
            scoresStringBuilder.append(s).append(",");
        }
        if (scoresStringBuilder.length() > 0) {
            scoresStringBuilder.setLength(scoresStringBuilder.length() - 1);
        }

        prefs.put(PREFS_KEY_HIGH_SCORES, scoresStringBuilder.toString());
    }**/

/**    @FXML
    public void clearHighScores() {
        // Remove saved preferences
        prefs.remove(PREFS_KEY_HIGH_SCORES);
        prefs.remove("highscore");

        System.out.println("High scores cleared.");
    }
**/

    @FXML
    private void onBackClicked() {
        ScreenManager.switchScene("/view/menu.fxml", "Arkanoid Menu");
    }
}
