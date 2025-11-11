package com.nhom12.arkanoid.controller;

import com.nhom12.arkanoid.utils.ScreenManager;
import com.nhom12.arkanoid.utils.SoundManager;
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
    private CheckBox musicCheckBox;

    @FXML
    private CheckBox sfxCheckBox;

    @FXML
    private ComboBox<String> diff;

    Preferences prefs = Preferences.userNodeForPackage(SettingsController.class);
    private MediaPlayer player;

    String difficulty = prefs.get("difficulty","Easy");
    boolean musicEnabled = prefs.getBoolean("musicEnabled", true);
    boolean sfxEnabled = prefs.getBoolean("sfxEnabled", true);


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        URL mediaUrl = getClass().getResource("/test1.mp4");
        Objects.requireNonNull(mediaUrl, "Video file not found!");

        Media media = new Media(mediaUrl.toExternalForm());
        this.player = new MediaPlayer(media);
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

        diff.getItems().addAll("Easy", "Medium", "Hard");
        diff.setValue(difficulty);

        // Load saved settings (if you implement persistent config later)
        musicCheckBox.setSelected(musicEnabled);
        sfxCheckBox.setSelected(sfxEnabled);

        //Setting background music
        musicCheckBox.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            if (isSelected) {
                SoundManager.getInstance().playBackgroundMusic();
            } else {
                SoundManager.getInstance().stopBackgroundMusic();
            }
        });

    }

    private void stopVideo() {
        if (player != null) {
            player.stop();  // Dừng video
            player.dispose(); // Hủy tài nguyên
        }
        if (mediaView != null) {
            mediaView.setMediaPlayer(null); // Gỡ player khỏi view
        }
    }

    // 🎮 Button actions
    @FXML
    private void onBackClicked() {
        stopVideo();
        System.out.println("Saving settings...");
        prefs.putBoolean("musicEnabled", musicCheckBox.isSelected());
        prefs.putBoolean("sfxEnabled", sfxCheckBox.isSelected());
        prefs.put("difficulty", diff.getValue());


        ScreenManager.switchScene("/view/menu.fxml", "Arkanoid Menu");
    }
}
