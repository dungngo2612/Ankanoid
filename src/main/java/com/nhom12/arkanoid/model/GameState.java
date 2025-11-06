package com.nhom12.arkanoid.model;

import com.nhom12.arkanoid.controller.SettingsController;
import com.nhom12.arkanoid.utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.prefs.Preferences;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.text.Text;


public class GameState {
    private List<Ball> ball;
    private Paddle paddle;
    private List<Brick> bricks;
    private int score;
    private int lives;
    private boolean isGameOver;
    private boolean isGameWon;

    private boolean ballLaunched = false;

    private boolean paddleHasLaser = false;
    private List<LaserBullet> bullets = new ArrayList<>();
    // Biến lưu thời điểm hết hiệu ứng
    private long laserEndTime = 0;
    // Biến lưu thời điểm được phép bắn laze tiếp theo
    private long nextLaserFireTime = 0;

    //
    private boolean moltenBallActive = false;
    private long moltenBallEndTime = 0;

    private boolean allowWinCheck = true;

    public boolean isAllowWinCheck() { return allowWinCheck; }
    public void setAllowWinCheck(boolean allowWinCheck) { this.allowWinCheck = allowWinCheck; }
    @FXML
    private Canvas gameCanvas;
    @FXML
    private Text scoreText;
    @FXML
    private Text livesText;


    public GameState() {

        this.paddle = new Paddle(
                (Constants.SCENE_WIDTH - Constants.PADDLE_WIDTH) / 2,
                Constants.PADDLE_START_Y,
                Constants.PADDLE_WIDTH,
                Constants.PADDLE_HEIGHT
        );
        // Vị trí sẽ được reset
        this.ball = new ArrayList<>();
        this.ball.add(new Ball(0, 0, Constants.BALL_RADIUS, 0, 0));
        this.bricks = new ArrayList<>();
        this.score = 0;
        this.lives = 3;
        this.isGameOver = false;
        this.isGameWon = false;

        Preferences prefs = Preferences.userNodeForPackage(SettingsController.class);
        String difficulty = prefs.get("difficulty", "Easy");
        LevelManager.LevelDifficulty diff;

        switch(difficulty) {
            case "Easy":
                diff = LevelManager.LevelDifficulty.EASY;
                System.out.println("Easy");
                break;
            case "Medium":
                diff = LevelManager.LevelDifficulty.NORMAL;
                System.out.println("Normal");
                break;
            case "Hard":
                diff = LevelManager.LevelDifficulty.DIFFICULLT;
                System.out.println("Difficult");
                break;
            default:
                diff = LevelManager.LevelDifficulty.EASY;
        }

        // khởi tạo bricks theo difficulty
        this.bricks = LevelManager.createLevel(diff);

        resetBall();

    }


    // Đặt lại bóng về vị trí trên thanh đỡ
    public void resetBall() {
        ball.clear();
        ball.add(new Ball(paddle.getX() + paddle.getWidth() / 2,
                paddle.getY() - Constants.BALL_RADIUS, Constants.BALL_RADIUS, 0, 0));
        setBallLaunched(false);
        setMoltenBallActive(false);
        setMoltenBallEndTime(0);
        // Tắt laze khi reset
        setPaddleHasLaser(false);
        bullets.clear();
        laserEndTime = 0;
    }

    public void launchBall() {
        Random rand = new Random();
        double randomNumber = rand.nextDouble(-Constants.BALL_SPEED, Constants.BALL_SPEED);
        if (!isBallLaunched()) {
            // Bắn lên trên
            ball.get(0).setDx(randomNumber);
            ball.get(0).setDy(-Constants.BALL_SPEED);
            setBallLaunched(true);
        }
    }

    public List<Ball> getBalls() {
        return ball;
    }

    public Ball getMainBall() {
        return ball.get(0);
    }

    public void setLives(int lives) {
        this.lives = Math.min(lives, Constants.MAX_LIVES);
    }

    public Paddle getPaddle() {
        return paddle;
    }

    public List<Brick> getBricks() {
        return bricks;
    }

    public int getScore() {
        return score;
    }

    public int getLives() {
        return lives;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public boolean isGameWon() {
        return isGameWon;
    }

    public boolean isBallLaunched() {
        return ballLaunched;
    }

    public void setBallLaunched(boolean ballLaunched) {
        this.ballLaunched = ballLaunched;
    }

    public void loseLife() {
        this.lives--;
        if (this.lives <= 0) {
            this.isGameOver = true;
        } else {
            resetBall();
        }
    }

    public void incrementScore(int points) {
        this.score += points;
    }
    public void checkWinCondition() {
        if (bricks.stream().filter(Brick::isDestructible).allMatch(Brick::isDestroyed)) {
            isGameWon = true;
        }
    }


    public boolean isPaddleHasLaser() {
        return paddleHasLaser;
    }

    public void setPaddleHasLaser(boolean paddleHasLaser) {
        this.paddleHasLaser = paddleHasLaser;
    }

    public List<LaserBullet> getBullets() {
        return bullets;
    }

    public long getLaserEndTime() {
        return laserEndTime;
    }

    public void setLaserEndTime(long laserEndTime) {
        this.laserEndTime = laserEndTime;
    }

    public long getNextLaserFireTime() {
        return nextLaserFireTime;
    }

    public void setNextLaserFireTime(long nextLaserFireTime) {
        this.nextLaserFireTime = nextLaserFireTime;
    }

    public boolean isMoltenBallActive() {
        return moltenBallActive;
    }

    public void setMoltenBallActive(boolean moltenBallActive) {
        this.moltenBallActive = moltenBallActive;
    }

    public long getMoltenBallEndTime() {
        return moltenBallEndTime;
    }

    public void setMoltenBallEndTime(long moltenBallEndTime) {
        this.moltenBallEndTime = moltenBallEndTime;
    }
}