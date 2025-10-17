package com.nhom12.arkanoid.model;

import com.nhom12.arkanoid.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class GameState {
    private Ball ball;
    private Paddle paddle;
    private List<Brick> bricks;
    private int score;
    private int lives;
    private boolean isGameOver;
    private boolean isGameWon;


    public GameState() {
        this.paddle = new Paddle(
                (Constants.SCENE_WIDTH - Constants.PADDLE_WIDTH) / 2,
                Constants.PADDLE_START_Y,
                Constants.PADDLE_WIDTH,
                Constants.PADDLE_HEIGHT
        );
        // Vị trí sẽ được reset
        this.ball = new Ball(0, 0, Constants.BALL_RADIUS, 0, 0);
        this.bricks = new ArrayList<>();
        this.score = 0;
        this.lives = 3;
        this.isGameOver = false;
        this.isGameWon = false;

        initializeBricks();
        resetBall();
    }

    // Khởi tạo danh sách các viên gạch
    private void initializeBricks() {
        bricks.clear();
        for (int row = 0; row < Constants.BRICK_ROWS; row++) {
            for (int col = 0; col < Constants.BRICK_COLS; col++) {
                double x = col * (Constants.BRICK_WIDTH + Constants.BRICK_GAP) + Constants.BRICK_GAP;
                double y = row * (Constants.BRICK_HEIGHT + Constants.BRICK_GAP) + Constants.BRICK_GAP + 30;
                bricks.add(new Brick(x, y, Constants.BRICK_WIDTH, Constants.BRICK_HEIGHT, 1));
            }
        }
    }

    // Đặt lại bóng về vị trí trên thanh đỡ
    public void resetBall() {
        ball.setX(paddle.getX() + paddle.getWidth() / 2);
        ball.setY(paddle.getY() - ball.getRadius());
        ball.setDx(0);
        ball.setDy(0);
    }

    public void launchBall() {
        // Chỉ phóng bóng nếu bóng đang đứng yên
        if (ball.getDx() == 0 && ball.getDy() == 0) {
            // Bắn lên trên
            ball.setDy(-Constants.BALL_SPEED);
        }
    }

    public Ball getBall() {
        return ball;
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
        // Kiểm tra điều kiện thắng game
        if (bricks.stream().allMatch(Brick::isDestroyed)) {
            isGameWon = true;
        }
    }
}