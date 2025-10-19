package com.nhom12.arkanoid.logic;

import com.nhom12.arkanoid.model.*;
import com.nhom12.arkanoid.utils.Constants;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;


public class GameEngine {
    private final GameState gameState;
    private final CollisionManager collisionManager;

    private final List<Items> items = new ArrayList<>();

    private static final int MAX_ITEMS = 3; // tối đa 2 vật phẩm trong 1 màn
    private int itemsSpawned = 0; // đếm số vật phẩm đã sinh ra

    public GameEngine() {
        this.gameState = new GameState();
        this.collisionManager = new CollisionManager();
    }

    public List<Items> getItems() {
        return items;
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
                    spawnItemIfPossible(brick);
                }
            }
        });
        //câp nhật vật phẩm
        updateItems();

        Paddle paddle = gameState.getPaddle();
        //Kiểm tra bóng rơi xuống dưới màn hình
        for(Items item: items) {

        }
        if (gameState.getBall().getY() > Constants.SCENE_HEIGHT) {
            gameState.loseLife();
        }
    }
    private void spawnItemIfPossible(Brick brick) {
        // Nếu hiện có ít hơn 2 vật phẩm thì mới cho spawn thêm
        if (items.size() < MAX_ITEMS) {
            Random rand = new Random();
            // 30% xác suất sinh vật phẩm
            if (rand.nextDouble() < 0.3) {
                Items newItem = new Items(
                        brick.getX() + brick.getWidth() / 2 - 15,
                        brick.getY() + brick.getHeight() / 2
                );
                items.add(newItem);
            }
        }
    }


    // Cập nhật trạng thái vật phẩm rơi xuống và xử lí cham với paddle
    private void updateItems() {
        Paddle paddle = gameState.getPaddle();

        Iterator<Items> it =items.iterator();

        while(it.hasNext()) {
            Items item = it.next();
            // cho vật phẩm rơi xuống
            item.removeItem();

            // khi vật phẩm chạm paddle
            if (item.intersects(paddle)) {
                applyItemEffect(item.getType());
                item.deActivated();
            }

            //Nếu vật phẩm ngoài màn hình => xoá liền

            if(item.getY() > Constants.SCENE_HEIGHT || !item.isActive()){
                it.remove();
            }

        }
    }

    // xử lý khi ăn được vật phẩm
    private void applyItemEffect(Items.ItemType type) {
        Paddle paddle = gameState.getPaddle();
         Ball ball = gameState.getBall();

        switch (type) {
            case EXTRA_LIFE:
                if (gameState.getLives() < Constants.MAX_LIVES) {
                    gameState.setLives(gameState.getLives() + 1);
                }
                break;
            case SPEED_UP:

                Ball balls = gameState.getBall();
                double dx = balls.getDx();
                double dy = balls.getDy();

                // Tăng 20% vận tốc theo từng trục
                dx *= 1.2;
                dy *= 1.2;
                double newSpeed = Math.sqrt(dx*dx + dy*dy);
                if (newSpeed > Constants.MAX_BALL_SPEED) {
                    dx = (Constants.MAX_BALL_SPEED/newSpeed) * dx;
                    dy = (Constants.MAX_BALL_SPEED/newSpeed) * dy;
                }
                ball.setDx(dx);
                ball.setDy(dy);
                break;
            case PADDLE_EXPAND:
                double newWidth = paddle.getWidth() + 20;
                if (newWidth > Constants.MAX_PADDLE_WIDTH) {
                    newWidth = Constants.MAX_PADDLE_WIDTH;
                }
                paddle.setWidth(newWidth);
                break;
        }

    }

}