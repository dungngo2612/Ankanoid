package com.nhom12.arkanoid.controller;

import com.nhom12.arkanoid.utils.ScreenManager;
import com.nhom12.arkanoid.utils.SoundManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class MenuController implements Initializable {

    // 🧱 This connects to fx:id="root" in FXML
    @FXML
    private StackPane menu;

    @FXML
    private MediaView mediaView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        URL mediaUrl = getClass().getResource("/test.mp4");
        Objects.requireNonNull(mediaUrl, "Video file not found!");

        Media media = new Media(mediaUrl.toExternalForm());
        MediaPlayer player = new MediaPlayer(media);
        player.setCycleCount(MediaPlayer.INDEFINITE);
        player.setOnEndOfMedia(() -> player.seek(Duration.ZERO));
        player.setAutoPlay(true);
        SoundManager.getInstance().playBackgroundMusic();
        mediaView.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                mediaView.fitWidthProperty().bind(newScene.widthProperty());
                mediaView.fitHeightProperty().bind(newScene.heightProperty());
            }
        });

        mediaView.setMediaPlayer(player);
    }

    // 🎮 Button actions
    @FXML
    private void onStartClicked() {
        System.out.println("Start Game clicked!");
        ScreenManager.switchScene("/view/game.fxml","Arkanoid");
    }

    @FXML
    private void onSettingsClicked() {
        System.out.println("Settings clicked!");
        ScreenManager.switchScene("/view/settings.fxml","Arkanoid");
    }

    @FXML
    private void onExitClicked() {
        System.out.println("Exit clicked!");
        System.exit(0);
    }

    @FXML
    private void onHighscoreClicked() {
        System.out.println("Highscore clicked!");
    }
}
