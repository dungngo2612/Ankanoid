package com.nhom12.arkanoid.controller;

import com.nhom12.arkanoid.logic.GameEngine;
import com.nhom12.arkanoid.model.*;
import com.nhom12.arkanoid.utils.Constants;
import com.nhom12.arkanoid.utils.ImageManager;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.text.Text;

import javafx.scene.shape.Rectangle;

import java.text.BreakIterator;
import java.util.List;

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
    private GameState gameState;
    private AnimationTimer gameLoop;

    private boolean isLeftKeyPressed = false;
    private boolean isRightKeyPressed = false;

    @FXML
    public void initialize() {
        gc = gameCanvas.getGraphicsContext2D();
        gameEngine = new GameEngine();
        gameState = gameEngine.getGameState();

        // Tạo vòng lặp game
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                //Cập nhật vị trí paddle
                updatePaddlePosition();
                //Cập nhật logic game
                gameEngine.update();
                render();
                if (gameState.isGameOver()) {
                    gameLoop.stop();
                    System.out.println("GAME OVER");
                } else if (gameState.isGameWon()) {
                    gameLoop.stop();
                    System.out.println("GAME WON!");
                }
            }
        };

        gameLoop.start();
        //Handle sự kiện input
        setupInputHandling();
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
        } else if (event.getCode() == KeyCode.SPACE) {
            gameState.launchBall();
            messageText.setText("");
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

    //Cập nhật vị trí paddle
    private void updatePaddlePosition() {
        Paddle paddle = gameEngine.getGameState().getPaddle();
        if (isLeftKeyPressed) {
            if (paddle.getX() - Constants.PADDLE_SPEED > 0) {
                paddle.setX(paddle.getX() - Constants.PADDLE_SPEED);
            } else {
                paddle.setX(0);
            }
        }
        if (isRightKeyPressed) {
            if (paddle.getX() + Constants.PADDLE_WIDTH + Constants.PADDLE_SPEED < Constants.SCENE_WIDTH) {
                paddle.setX(paddle.getX() + Constants.PADDLE_SPEED);
            } else {
                paddle.setX(Constants.SCENE_WIDTH - Constants.PADDLE_WIDTH);
            }
        }
    }


    private void render() {
        GameState state = gameEngine.getGameState();

        Image background = ImageManager.getInstance().showImage("background");
        gc.drawImage(background, 0, 0, 800, 600);

        // Vẽ thanh đỡ
        Image paddleImg = ImageManager.getInstance().showImage("paddle");
        Paddle paddle = state.getPaddle();
        gc.drawImage(paddleImg, paddle.getX(), paddle.getY(), paddle.getWidth(), paddle.getHeight());

        // Vẽ bóng
        Image ballImg = ImageManager.getInstance().showImage("ball");
        Ball ball = state.getBall();
        double tmpX = ball.getX() - ball.getRadius();
        double tmpY = ball.getY() - ball.getRadius();
        double tmpWidth = ball.getRadius() * 2;
        gc.drawImage(ballImg, tmpX, tmpY, tmpWidth, tmpWidth);

        // Vẽ gạch
        Image brickImg = ImageManager.getInstance().showImage("brick");
        List<Brick> list = state.getBricks();
        for (Brick brick : list) {
            if (brick.isDestroyed()) {
                continue;
            }
            double x = brick.getX();
            double y = brick.getY();
            gc.drawImage(brickImg, x, y, Constants.BRICK_WIDTH, Constants.BRICK_HEIGHT);
        }

        scoreText.setText("Score: " + state.getScore());
        livesText.setText("Lives: " + state.getLives());

        // Vẽ các vật phẩm đang rơi
        for (Items item : gameEngine.getItems()) {
            if (item.isActive()) {
                gc.setFill(Color.YELLOW);
                gc.fillRect(item.getX(), item.getY(), item.getWidth(), item.getHeight());
            }
        }
    }
}
