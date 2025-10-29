package com.nhom12.arkanoid.controller;

import com.nhom12.arkanoid.model.GameState;
import com.nhom12.arkanoid.utils.ScreenManager;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class PauseMenuController {

    @FXML
    private AnchorPane pauseMenuRoot;

    private GameController gameController;

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    @FXML
    private void handleRestart() {
        if (gameController != null) {
            System.out.println("Restart Game");
            gameController.resetGame();
            hidePauseMenu();
        }
    }

    @FXML
    private void handleMainMenu() {
        if (gameController != null) {
            System.out.println("Go to Main Menu");
            gameController.goToMainMenu();
            hidePauseMenu();
        }
    }

    @FXML
    private void handleSettings() {
            System.out.println("Open Settings");
            ScreenManager.switchScene("/view/settings.fxml","Arkanoid");
            hidePauseMenu();
    }

    @FXML
    private void handleResume() {
        System.out.println("Resume Game");
        hidePauseMenu(); // Ẩn menu và tiếp tục game
    }

    public void showPauseMenu() {
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
}