package com.nhom12.arkanoid.controller;

import com.nhom12.arkanoid.logic.GameEngine;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class GameController {

    @FXML
    private Canvas gameCanvas;
    @FXML
    private Text scoreText;
    @FXML
    private Text livesText;
    @FXML
    private Text messageText;

    private GraphicsContext gc;
    private GameEngine gameEngine;
    private AnimationTimer gameLoop;

    private boolean isLeftKeyPressed = false;
    private boolean isRightKeyPressed = false;

    @FXML
    public void initialize() {
        gc = gameCanvas.getGraphicsContext2D();
        gameEngine = new GameEngine();

        // Tạo vòng lặp game
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                //Handle sự kiện input
                setupInputHandling();
                //Cập nhật logic game
                gameEngine.update();
            }
        };

        gameLoop.start();
    }

    private void setupInputHandling() {
        gameCanvas.setFocusTraversable(true);
        gameCanvas.setOnKeyPressed(event -> {
            this.handleKeyPressed(event);
        });
        gameCanvas.setOnKeyReleased(event -> {
            this.handleKeyReleased(event);
        });
    }

    //Xử lý nhấn phím
    private void handleKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.LEFT) {
            isLeftKeyPressed = true;
        } else if (event.getCode() == KeyCode.RIGHT) {
            isRightKeyPressed = true;
        }
    }

    //Xử lý nhả phím
    private void handleKeyReleased(KeyEvent event) {
        if (event.getCode() == KeyCode.LEFT) {
            isLeftKeyPressed = false;
        } else if (event.getCode() == KeyCode.RIGHT) {
            isRightKeyPressed = false;
        }
    }
}
