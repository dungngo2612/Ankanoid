package com.nhom12.arkanoid.controller;

import com.nhom12.arkanoid.model.Brick;
import com.nhom12.arkanoid.model.NormalBrick;
import com.nhom12.arkanoid.model.StrongBrick;
import com.nhom12.arkanoid.model.UnbreakableBrick;
import com.nhom12.arkanoid.model.ExplosiveBrick;
import com.nhom12.arkanoid.logic.GameEngine;
import com.nhom12.arkanoid.model.*;
import com.nhom12.arkanoid.utils.Constants;
import com.nhom12.arkanoid.utils.ImageManager;
import com.nhom12.arkanoid.utils.ScreenManager;
import com.nhom12.arkanoid.utils.SoundManager;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.List;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import java.util.List;
import java.util.prefs.Preferences;

public class GameController {

    @FXML
    private Canvas gameCanvas;
    @FXML
    private Text scoreText;
    @FXML
    private Text livesText;
    @FXML
    private Text messageText;
    @FXML
    private AnchorPane gameRoot;

    private GraphicsContext gc;
    private GameEngine gameEngine;
    private GameState gameState;
    private AnimationTimer gameLoop;

    private boolean isLeftKeyPressed = false;
    private boolean isRightKeyPressed = false;

    private boolean isPaused = false;
    private Parent pauseMenuNode;
    private PauseMenuController pauseMenuController;

    @FXML
    private Text timerText;
    private long startTime = 0;
    private boolean isTimerStarted = false;


    @FXML
    public void initialize() {
        startTime = System.currentTimeMillis();
        gc = gameCanvas.getGraphicsContext2D();
        gameEngine = new GameEngine(gameRoot);
        gameState = gameEngine.getGameState();

        loadPauseMenu();

        // Tạo vòng lặp game
        gameLoop = new AnimationTimer() {


            @Override
            public void handle(long now) {
                if (isTimerStarted) {
                    long currentTime = System.currentTimeMillis();
                    long elapsedMillis = currentTime - startTime;
                    long seconds = elapsedMillis / 1000;
                    long minutes = seconds / 60;
                    long secs = seconds % 60;
                    timerText.setText(String.format("Time: %02d:%02d", minutes, secs));
                }

                //Cập nhật vị trí paddle
                updatePaddlePosition();
                //Cập nhật logic game
                gameEngine.update();
                render();
                if (gameState.isGameOver()) {
                    long elapsedMillis = System.currentTimeMillis() - startTime;
                    long seconds = elapsedMillis / 1000;

                    GameResult result = new GameResult(gameState.getScore(), seconds);
                    ScreenManager.setData(result);

                    HighScoreController highScoreController = new HighScoreController();
                    highScoreController.saveScore(gameState.getScore(),seconds);

                    gameLoop.stop();
                    ScreenManager.switchScene("/view/lose.fxml", "Arkanoid");
                    SoundManager.getInstance().stopPlayingMusic();
                } else if (gameState.isGameWon()) {
                    long elapsedMillis = System.currentTimeMillis() - startTime;
                    long seconds = elapsedMillis / 1000;

                    GameResult result = new GameResult(gameState.getScore(),seconds);
                    ScreenManager.setData(result);

                    HighScoreController highScoreController = new HighScoreController();
                    highScoreController.saveScore(gameState.getScore(),seconds);

                    gameLoop.stop();
                    ScreenManager.switchScene("/view/win.fxml", "Arkanoid");
                    SoundManager.getInstance().stopPlayingMusic();
                }
            }
        };

        gameLoop.start();
        //Handle sự kiện input
        setupInputHandling();
    }

    public void resetGame() {
        gameLoop.stop(); // Dừng game loop cũ
        initialize(); // Khởi tạo lại toàn bộ game
        ScreenManager.switchScene("/view/game.fxml", "Arkanoid");
    }

    public void goToMainMenu() {
        gameLoop.stop(); // Dừng game trước khi chuyển cảnh
        SoundManager.getInstance().stopPlayingMusic();
        ScreenManager.switchScene("/view/menu.fxml", "Arkanoid");
        Preferences prefs = Preferences.userNodeForPackage(SettingsController.class);
        prefs.putBoolean("evilMode", false); // Reset để lần sau không bị ảnh hưởng
    }

    /**
     * Tải FXML của pause menu và thêm nó vào gameRoot, nhưng ẩn đi
     */
    private void loadPauseMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/pause_menu.fxml"));
            pauseMenuNode = loader.load();
            pauseMenuController = loader.getController();
            pauseMenuController.setGameController(this); // Rất quan trọng!

            // Đặt menu ẩn đi và thêm vào AnchorPane
            pauseMenuNode.setVisible(false);
            gameRoot.getChildren().add(pauseMenuNode);

            // Đảm bảo pause menu che phủ toàn bộ màn hình
            AnchorPane.setTopAnchor(pauseMenuNode, 0.0);
            AnchorPane.setBottomAnchor(pauseMenuNode, 0.0);
            AnchorPane.setLeftAnchor(pauseMenuNode, 0.0);
            AnchorPane.setRightAnchor(pauseMenuNode, 0.0);

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load pause_menu.fxml");
        }
    }

    /**
     * Bật/tắt trạng thái tạm dừng
     */
    private void togglePauseMenu() {
        isPaused = !isPaused;
        if (isPaused) {
            gameLoop.stop(); // Dừng game
            pauseMenuController.showPauseMenu(); // Hiện menu
            pauseMenuNode.requestFocus(); // Chuyển focus cho menu để nhận input
        } else {
            pauseMenuController.hidePauseMenu(); // Ẩn menu
            gameLoop.start(); // Tiếp tục game
            gameCanvas.requestFocus(); // Trả focus về cho game
        }
    }

    /**
     * Hàm này được gọi từ PauseMenuController
     */
    public void resumeGame() {
        if (isPaused) {
            togglePauseMenu();
        }
    }

    private void setupInputHandling() {
        gameCanvas.setFocusTraversable(true);

        // Lắng nghe trên Scene thay vì Canvas
        // Điều này cho phép chúng ta bắt phím '1' ngay cả khi Canvas không focus
        gameCanvas.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.setOnKeyPressed(event -> {
                    this.handleKeyPressed(event);
                });
                newScene.setOnKeyReleased(event -> {
                    this.handleKeyReleased(event);
                });
            }
        });
        gameCanvas.requestFocus(); // Yêu cầu focus ban đầu
    }

    //Xử lý nhấn phím
    private void handleKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.DIGIT1 || event.getCode() == KeyCode.NUMPAD1) { // Dùng DIGIT1 cho phím '1' hàng số
            togglePauseMenu();
            return; // Dừng xử lý các phím khác
        }
        if (isPaused) return;

        if (event.getCode() == KeyCode.LEFT) {
            isLeftKeyPressed = true;
        } else if (event.getCode() == KeyCode.RIGHT) {
            isRightKeyPressed = true;
        } else if (event.getCode() == KeyCode.SPACE) {
            gameState.launchBall();
            messageText.setText("");

            // ⏱ Bắt đầu đếm thời gian nếu chưa bắt đầu
            if (!isTimerStarted) {
                startTime = System.currentTimeMillis();
                isTimerStarted = true;
            }
        }
//        else if (event.getCode() == KeyCode.SHIFT) {
//            // Chỉ bắn khi đang có item laze
//            if (gameState.isPaddleHasLaser()) {
//                Paddle p = gameState.getPaddle();
//                // Bắn 2 viên đạn từ 2 đầu paddle
//                gameState.getBullets().add(new LaserBullet(p.getX() + 10, p.getY()));
//                gameState.getBullets().add(new LaserBullet(p.getX() + p.getWidth() - 10, p.getY()));
//            }
//        }

        // Chỉ xử lý phím game nếu không bị tạm dừng
    }

    //Xử lý nhả phím
    private void handleKeyReleased(KeyEvent event) {
        if (isPaused) return;
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
//        Image ballImg = ImageManager.getInstance().showImage("ball");
//        for (Ball ball : state.getBalls()) { // Lặp qua danh sách bóng
//            double tmpX = ball.getX() - ball.getRadius();
//            double tmpY = ball.getY() - ball.getRadius();
//            double tmpWidth = ball.getRadius() * 2;
//            gc.drawImage(ballImg, tmpX, tmpY, tmpWidth, tmpWidth);
//        }
        Image ballImg;
        if (state.isMoltenBallActive()) {
            ballImg = ImageManager.getInstance().showImage("molten_ball"); // ảnh bóng hiệu ứng
        } else {
            ballImg = ImageManager.getInstance().showImage("ball"); // ảnh bóng thường
        }

        for (Ball ball : state.getBalls()) {

            int i = 0;
            double initialRadius = ball.getRadius();
            int trailSize = ball.getRecentPositions().size();

            for (Point2D pos : ball.getRecentPositions()) {
                // Kích thước của "hạt" vệt sáng (nhỏ dần)
                double trailRadius = initialRadius * (1.0 - (double) i / trailSize);
                // Độ mờ của "hạt" (mờ dần)
                double opacity = 0.5 * (1.0 - (double) i / trailSize);

                // Chọn màu cho vệt sáng
                if (state.isMoltenBallActive()) {
                    gc.setFill(Color.rgb(255, 100, 0, opacity)); // Màu cam cho molten ball
                } else {
                    gc.setFill(Color.rgb(105, 105, 105, opacity)); // Màu xanh nhạt cho bóng thường
                }

                // Vẽ "hạt" của vệt sáng
                gc.fillOval(pos.getX() - trailRadius, pos.getY() - trailRadius, trailRadius * 2, trailRadius * 2);
                i++;
            }

            double tmpX = ball.getX() - ball.getRadius();
            double tmpY = ball.getY() - ball.getRadius();
            double tmpWidth = ball.getRadius() * 2 ;
            gc.drawImage(ballImg, tmpX, tmpY, tmpWidth, tmpWidth);
        }

        Image bulletImg = ImageManager.getInstance().showImage("laser_bullet");
        for (LaserBullet bullet : state.getBullets()) {
            gc.drawImage(bulletImg, bullet.getX(), bullet.getY(), bullet.getWidth(), bullet.getHeight());
        }

        // Vẽ gạch
        List<Brick> list;
        if (state.isEvilMode()) {
            list = state.getEvilMap().getBricks();

            // Vẽ death line màu đỏ
            gc.setStroke(Color.RED);
            gc.setLineWidth(2);
            gc.strokeLine(0, state.getEvilMap().getDeathLineY(), Constants.SCENE_WIDTH, state.getEvilMap().getDeathLineY());
        } else {
            list = state.getBricks();
        }
        for (Brick brick : list) {
            Image brickImg = null;
            if (brick.isDestroyed()) {
                continue;
            }
            double x = brick.getX();
            double y = brick.getY();
            if (brick instanceof NormalBrick) {
                brickImg = ImageManager.getInstance().showImage("normal_brick");
            } else if (brick instanceof UnbreakableBrick) {
                brickImg = ImageManager.getInstance().showImage("impassable");
            } else if (brick instanceof StrongBrick) {
                if (brick.getHealth() == 3) {
                    brickImg = ImageManager.getInstance().showImage("strong_brick1");
                } else if (brick.getHealth() == 2) {
                    brickImg = ImageManager.getInstance().showImage("strong_brick2");
                } else if (brick.getHealth() == 1) {
                    brickImg = ImageManager.getInstance().showImage("strong_brick3");
                }
            } else if (brick instanceof ExplosiveBrick) {
                // Tạm thời dùng "brick1"
                brickImg = ImageManager.getInstance().showImage("explosive_brick");
            }
            if (brickImg != null) {
                gc.drawImage(brickImg, x, y, Constants.BRICK_WIDTH, Constants.BRICK_HEIGHT);
            }
        }

        scoreText.setText("Score: " + state.getScore());
        livesText.setText("Lives: " + state.getLives());

        // Vẽ các vật phẩm đang rơi
        for (Items item : gameEngine.getItems()) {
            if (item.isActive()) {
                Image item_type = null;
                if (item.getType() == Items.ItemType.EXTRA_LIFE) {
                    item_type = ImageManager.getInstance().showImage("extra_life");
                } else if (item.getType() == Items.ItemType.PADDLE_SHRINK) {
                    item_type = ImageManager.getInstance().showImage("paddle_shrink");
                } else if (item.getType() == Items.ItemType.PADDLE_EXPAND) {
                    item_type = ImageManager.getInstance().showImage("paddle_expand");
                } else if (item.getType() == Items.ItemType.LASER_PADDLE) {
                    item_type = ImageManager.getInstance().showImage("laser_paddle");
                } else if (item.getType() == Items.ItemType.SPEED_UP) {
                    item_type = ImageManager.getInstance().showImage("speed_up");
                } else if (item.getType() == Items.ItemType.SPEED_DOWN) {
                    item_type = ImageManager.getInstance().showImage("speed_down");
                } else if (item.getType() == Items.ItemType.MULTI_BALL) {
                    item_type = ImageManager.getInstance().showImage("multi_balls");
                } else if (item.getType() == Items.ItemType.MOLTEN_BALL) {
                    item_type = ImageManager.getInstance().showImage("molten_ball");
                }
                if (item_type != null) {
                    gc.drawImage(item_type, item.getX(), item.getY(), item.getWidth(), item.getHeight());
                }
            }
        }
    }
}
