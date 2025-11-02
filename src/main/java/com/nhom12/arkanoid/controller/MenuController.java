package com.nhom12.arkanoid.controller;

import com.nhom12.arkanoid.utils.ScreenManager;
import com.nhom12.arkanoid.utils.SoundManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
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

    @FXML
    private VBox mainButtonsVBox; // VBox cho các nút chính

    @FXML
    private VBox modeSelectionVBox; // VBox cho chọn chế độ

    SettingsController settingsController;

    public static boolean showModeSelectionOnReturn = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        URL mediaUrl = getClass().getResource("/test1.mp4");
        Objects.requireNonNull(mediaUrl, "Video file not found!");

        Media media = new Media(mediaUrl.toExternalForm());
        MediaPlayer player = new MediaPlayer(media);
        player.setCycleCount(MediaPlayer.INDEFINITE);
        player.setOnEndOfMedia(() -> player.seek(Duration.ZERO));
        player.setAutoPlay(true);

        settingsController = new SettingsController();
        if (settingsController.musicEnabled) {
            SoundManager.getInstance().playBackgroundMusic();
        }

        mediaView.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                mediaView.fitWidthProperty().bind(newScene.widthProperty());
                mediaView.fitHeightProperty().bind(newScene.heightProperty());
            }
        });

        mediaView.setMediaPlayer(player);

        if (showModeSelectionOnReturn) {
            mainButtonsVBox.setVisible(false);
            mainButtonsVBox.setManaged(false);
            modeSelectionVBox.setVisible(true);
            modeSelectionVBox.setManaged(true);
            showModeSelectionOnReturn = false; // Reset lại biến
        } else {
            mainButtonsVBox.setVisible(true);
            mainButtonsVBox.setManaged(true);
            modeSelectionVBox.setVisible(false);
            modeSelectionVBox.setManaged(false);
        }
    }

    // 🎮 Button actions
    @FXML
    private void onStartClicked() {
        // Ẩn VBox nút chính và hiện VBox chọn chế độ
        mainButtonsVBox.setVisible(false);
        mainButtonsVBox.setManaged(false);
        modeSelectionVBox.setVisible(true);
        modeSelectionVBox.setManaged(true);
    }

    @FXML
    private void onChallengeModeClicked() {
        System.out.println("Challenge Mode selected! Opening level map...");
        // BÂY GIỜ CHUYỂN TỚI MÀN HÌNH CHỌN LEVEL
        showModeSelectionOnReturn = true;
        ScreenManager.switchScene("/view/level_select.fxml","Select Your Case");
    }

    @FXML
    private void onBackToMainClicked() {
        // Ẩn VBox chọn chế độ và hiện lại VBox nút chính
        modeSelectionVBox.setVisible(false);
        modeSelectionVBox.setManaged(false);
        mainButtonsVBox.setVisible(true);
        mainButtonsVBox.setManaged(true);
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
        ScreenManager.switchScene("/view/highscore.fxml","Arkanoid");
    }
}
