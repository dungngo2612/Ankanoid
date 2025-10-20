package com.nhom12.arkanoid.controller;

import com.nhom12.arkanoid.utils.ScreenManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class SettingsController implements Initializable {

    // 🧱 This connects to fx:id="root" in FXML
    @FXML
    private StackPane settings;

    @FXML
    private MediaView mediaView;

    @FXML
    private ComboBox<String> resolutionCombo;

    @FXML
    private CheckBox musicCheckBox;

    @FXML
    private CheckBox sfxCheckBox;

    @FXML
    private ComboBox<String> diff;

    Preferences prefs = Preferences.userNodeForPackage(SettingsController.class);

    boolean musicEnabled = prefs.getBoolean("musicEnabled", true);
    boolean sfxEnabled = prefs.getBoolean("sfxEnabled", true);
    String resolution = prefs.get("resolution", "800x600");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        URL mediaUrl = getClass().getResource("/test.mp4");
        Objects.requireNonNull(mediaUrl, "Video file not found!");

        Media media = new Media(mediaUrl.toExternalForm());
        MediaPlayer player = new MediaPlayer(media);
        player.setCycleCount(MediaPlayer.INDEFINITE);
        player.setOnEndOfMedia(() -> player.seek(Duration.ZERO));
        player.setAutoPlay(true);

        mediaView.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                mediaView.fitWidthProperty().bind(newScene.widthProperty());
                mediaView.fitHeightProperty().bind(newScene.heightProperty());
            }
        });

        mediaView.setMediaPlayer(player);

        resolutionCombo.getItems().addAll("800x600", "1024x768", "1280x720", "1920x1080");
        resolutionCombo.setValue("800x600");

        diff.getItems().addAll("Easy", "Medium", "Hard");
        diff.setValue("Easy");

        // Load saved settings (if you implement persistent config later)
        musicCheckBox.setSelected(true);
        sfxCheckBox.setSelected(true);
    }

    // 🎮 Button actions
    @FXML
    private void onBackClicked() {
        System.out.println("Saving settings...");
        // You could save user preferences here later
        ScreenManager.switchScene("/view/menu.fxml", "Arkanoid Menu");
    }
}
