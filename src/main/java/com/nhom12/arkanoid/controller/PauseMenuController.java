package com.nhom12.arkanoid.controller;

import com.nhom12.arkanoid.model.GameState;
import com.nhom12.arkanoid.utils.ScreenManager;
import com.nhom12.arkanoid.utils.SoundManager;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import jdk.jfr.BooleanFlag;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class PauseMenuController implements Initializable {

    @FXML
    private AnchorPane pauseMenuRoot;
    @FXML
    private VBox mainPauseButtonsVBox;
    @FXML
    private VBox goToMainMenuConfirmationVBox;
    @FXML
    private VBox restartConfirmationVBox;
    @FXML
    private VBox settingsVBox;
    @FXML
    private CheckBox musicEnable;
    @FXML
    private CheckBox effectsEnable;
    @FXML
    private VBox selectLevelVBox;
    Preferences prefs = Preferences.userNodeForPackage(SettingsController.class);
    boolean musicEnabled = prefs.getBoolean("musicEnabled", true);
    boolean sfxEnabled = prefs.getBoolean("sfxEnabled", true);

    private GameController gameController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Tải trạng thái đã lưu
        musicEnable.setSelected(musicEnabled);
        effectsEnable.setSelected(sfxEnabled);

        // Đặt listener ở đây
        musicEnable.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            if (isSelected) {
                SoundManager.getInstance().playPlayingMusic();
            } else {
                SoundManager.getInstance().stopPlayingMusic();
            }
        });
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    @FXML
    private void handleRestart() {
        restartConfirmationVBox.setVisible(true);
        restartConfirmationVBox.setVisible(true);
    }

    @FXML
    private void handleConfirmRestart() {
        if (gameController != null) {
            System.out.println("Restarting game...");
            gameController.resetGame();
            ScreenManager.switchScene("/view/game.fxml","Arkanoid");
        }
    }

    @FXML
    private void handleMainMenu() {
        goToMainMenuConfirmationVBox.setVisible(true);
        goToMainMenuConfirmationVBox.setManaged(true);
    }

    @FXML
    private void handleConfirmExit() {
        if (gameController != null) {
            System.out.println("Go to Main Menu");
            gameController.goToMainMenu();
            hidePauseMenu();
        }
    }

    @FXML
    private void handleSettings() {
            System.out.println("Open Settings");
            settingsVBox.setVisible(true);
            settingsVBox.setManaged(true);

        musicEnable.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            if (isSelected) {
                SoundManager.getInstance().playPlayingMusic();
            } else {
                SoundManager.getInstance().stopPlayingMusic();
            }
        });
    }

    @FXML
    private void handleResume() {
        if (gameController != null) {
            System.out.println("Resume Game");
            gameController.resumeGame();
            hidePauseMenu(); // Ẩn menu và tiếp tục game
        }
    }

    public void showPauseMenu() {
        if (mainPauseButtonsVBox != null) {
            mainPauseButtonsVBox.setVisible(true);
            mainPauseButtonsVBox.setManaged(true);
        }
        if (goToMainMenuConfirmationVBox != null) {
            goToMainMenuConfirmationVBox.setVisible(false);
            goToMainMenuConfirmationVBox.setManaged(false);
        }
        if (restartConfirmationVBox != null) {
            restartConfirmationVBox.setVisible(false);
            restartConfirmationVBox.setManaged(false);
        }
        if (settingsVBox != null) {
            settingsVBox.setVisible(false);
            settingsVBox.setManaged(false);
        }
        if (selectLevelVBox != null) {
            selectLevelVBox.setManaged(false);
            selectLevelVBox.setManaged(false);
        }

        pauseMenuRoot.setVisible(true);
        FadeTransition ft = new FadeTransition(Duration.millis(300), pauseMenuRoot);
        ft.setFromValue(0); // Bắt đầu từ trong suốt
        ft.setToValue(1);   // Đến hoàn toàn hiện rõ
        ft.play();
    }

    public void hidePauseMenu() {
        FadeTransition ft = new FadeTransition(Duration.millis(300), pauseMenuRoot);
        ft.setFromValue(1); // Bắt đầu từ hiện rõ
        ft.setToValue(0);   // Đến trong suốt
        ft.setOnFinished(event -> pauseMenuRoot.setVisible(false)); // Ẩn hoàn toàn sau khi animation kết thúc
        ft.play();
    }

    @FXML
    private void handleCancelExit() {
        // Quay lại menu pause chính
        goToMainMenuConfirmationVBox.setVisible(false);
        goToMainMenuConfirmationVBox.setManaged(false);

        restartConfirmationVBox.setVisible(false);
        restartConfirmationVBox.setManaged(false);

        selectLevelVBox.setManaged(false);
        selectLevelVBox.setVisible(false);

        if (settingsVBox.isVisible()) {
            prefs.putBoolean("musicEnabled", musicEnable.isSelected());
            prefs.putBoolean("sfxEnabled", effectsEnable.isSelected());
        }
        settingsVBox.setVisible(false);
        settingsVBox.setManaged(false);

        mainPauseButtonsVBox.setVisible(true);
        mainPauseButtonsVBox.setManaged(true);
    }

    @FXML
    private void handleSelectLevel() {
        System.out.println("Open Select Level");
        selectLevelVBox.setVisible(true);
        selectLevelVBox.setManaged(true);
    }

    @FXML
    private void handleConfirmSelectLevel() {
        System.out.println("Selecting Level");
        ScreenManager.switchScene("/view/level_select.fxml", "Arkanoid");
    }
}