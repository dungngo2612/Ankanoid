package com.nhom12.arkanoid.logic;

import com.nhom12.arkanoid.model.GameState;
import com.nhom12.arkanoid.utils.Constants;

public class GameEngine {
    private final GameState gameState;
    private final CollisionManager collisionManager;

    public GameEngine() {
        this.gameState = new GameState();
        this.collisionManager = new CollisionManager();
    }

    public GameState getGameState() {
        return gameState;
    }

    public CollisionManager getCollisionManager() {
        return collisionManager;
    }

    // Update logic game mỗi frame
    public void update() {

        if (gameState.isGameOver() || gameState.isGameWon()) {
            return;
        }

        // Di chuyển bóng
        gameState.getBall().move();// Nếu bóng chưa được phóng, gắn nó theo paddle
        if (!gameState.isBallLaunched()) {
            gameState.getBall().setX(
                    gameState.getPaddle().getX() + gameState.getPaddle().getWidth() / 2
            );
            gameState.getBall().setY(
                    gameState.getPaddle().getY() - gameState.getBall().getRadius() - 2
            );
            return;
        }

        // Nếu đã phóng → bóng bay
        gameState.getBall().move();


        //Xử lý va chạm
        collisionManager.handleWallCollision(gameState.getBall());
        collisionManager.handlePaddleCollision(gameState.getBall(), gameState.getPaddle());

        gameState.getBricks().forEach(brick -> {
            if (collisionManager.handleBrickCollision(gameState.getBall(), brick)) {
                if (brick.isDestroyed()) {
                    gameState.incrementScore(10);
                }
            }
        });

        //Kiểm tra bóng rơi xuống dưới màn hình
        if (gameState.getBall().getY() > Constants.SCENE_HEIGHT) {
            gameState.loseLife();
        }
    }
}