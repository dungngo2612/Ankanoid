package com.nhom12.arkanoid.logic;

import com.nhom12.arkanoid.model.*;
import com.nhom12.arkanoid.utils.Constants;
import com.nhom12.arkanoid.model.Brick;
import com.nhom12.arkanoid.model.ExplosiveBrick;
import com.nhom12.arkanoid.utils.ImageManager;
import com.nhom12.arkanoid.utils.ParticleManager;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;


public class GameEngine {
    private final GameState gameState;
    private final CollisionManager collisionManager;
    private Pane gameRoot;

    private final List<Items> items = new ArrayList<>();

    private static final int MAX_ITEMS = 3; // tối đa 2 vật phẩm trong 1 màn
    private int itemsSpawned = 0; // đếm số vật phẩm đã sinh ra

    public GameEngine(AnchorPane gameRoot) {
        this.gameRoot = gameRoot;
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

        //Cập nhật vật phẩm
        updateItems();

        //Cập nhật laser
        updateLasers();

        // Thêm logic kiểm tra thời gian laser
        if (gameState.isPaddleHasLaser()) {
            long currentTime = System.currentTimeMillis();
            if (currentTime > gameState.getLaserEndTime()) {
                // Hết thời gian, tắt laze
                gameState.setPaddleHasLaser(false);
                gameState.setLaserEndTime(0);
                gameState.setNextLaserFireTime(0);
            }
            // Nếu còn buff, kiểm tra xem đã đến lúc tự động bắn chưa
            else if (currentTime >= gameState.getNextLaserFireTime()) {
                // Đã đến lúc, tạo 2 viên đạn
                Paddle p = gameState.getPaddle();
                gameState.getBullets().add(new LaserBullet(p.getX() + 10, p.getY()));
                gameState.getBullets().add(new LaserBullet(p.getX() + p.getWidth() - 10, p.getY()));

                // Đặt lại thời gian cho lần bắn tiếp theo
                gameState.setNextLaserFireTime(currentTime + Constants.LASER_FIRE_RATE_MS);
            }
        }

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
                brick.hit(1.0);
                if (brick.isDestroyed()) {
                    gameState.incrementScore(10);
                    spawnItemIfPossible(brick);
                    // Chỉ xử lý nổ sau khi gạch đã bị phá hủy
                    if (brick instanceof ExplosiveBrick) {
                        handleExplosiveBrick((ExplosiveBrick) brick);
                    }
                    String imageKey = "brick1";
                    if (brick instanceof StrongBrick) imageKey = "brick2";
                    else if (brick instanceof UnbreakableBrick) imageKey = "impassable";
                    else if (brick instanceof ExplosiveBrick) imageKey = "brick1"; // hoặc ảnh riêng nếu có

                    Image brickImage = ImageManager.getInstance().showImage(imageKey);
                    ParticleManager.spawnBrickFragments(
                            brick.getX() + brick.getWidth() / 2,
                            brick.getY() + brick.getHeight() / 2,
                            brickImage,
                            gameRoot
                    );
                }
            }
        });

        Paddle paddle = gameState.getPaddle();
        //Kiểm tra bóng rơi xuống dưới màn hình
        for (Items item : items) {

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

        Iterator<Items> it = items.iterator();

        while (it.hasNext()) {
            Items item = it.next();
            // cho vật phẩm rơi xuống
            item.removeItem();

            // khi vật phẩm chạm paddle
            if (item.intersects(paddle)) {
                applyItemEffect(item.getType());
                item.deActivated();
            }

            //Nếu vật phẩm ngoài màn hình => xoá liền

            if (item.getY() > Constants.SCENE_HEIGHT || !item.isActive()) {
                it.remove();
            }

        }
    }

    // xử lí nếu phá vỡ viên gạch nổ=> các viên gạch xung quanh cũng sẽ bị mất 1 máu
    private void handleExplosiveBrick(ExplosiveBrick centerBrick) {
        List<Brick> bricks = gameState.getBricks();

        for (Brick brick : bricks) {
            if (!brick.isDestroyed()) {

                if (brick.isDestroyed()) continue;
                if (centerBrick.isDestroyed()) continue;
                // tìm ô gạch nào ở xung quanh
                double dx = Math.abs(brick.getX() - centerBrick.getX());
                double dy = Math.abs(brick.getY() - centerBrick.getY());

                if (dx <= Math.max(brick.getWidth(), brick.getHeight()) && dy <= Math.max(brick.getWidth(), brick.getHeight())) {
                    brick.hit(1.0);
                }
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
                double newSpeed = Math.sqrt(dx * dx + dy * dy);
                if (newSpeed > Constants.MAX_BALL_SPEED) {
                    dx = (Constants.MAX_BALL_SPEED / newSpeed) * dx;
                    dy = (Constants.MAX_BALL_SPEED / newSpeed) * dy;
                }
                ball.setDx(dx);
                ball.setDy(dy);
                break;
            case SPEED_DOWN:
                double currentDx = ball.getDx();
                double currentDy = ball.getDy();

                // Giảm 20% tốc độ (nhân với 0.8)
                currentDx *= 0.8;
                currentDy *= 0.8;
                double currentSpeed = Math.sqrt(currentDx * currentDx + currentDy * currentDy);
                if (currentSpeed < Constants.MIN_BALL_SPEED) {
                    currentDx = (Constants.MIN_BALL_SPEED / currentSpeed) * currentDx;
                    currentDy = (Constants.MIN_BALL_SPEED / currentSpeed) * currentDy;
                }
                ball.setDx(currentDx);
                ball.setDy(currentDy);
                break;
            case PADDLE_EXPAND:
                double newWidth = paddle.getWidth() + 20;
                if (newWidth > Constants.MAX_PADDLE_WIDTH) {
                    newWidth = Constants.MAX_PADDLE_WIDTH;
                }
                paddle.setWidth(newWidth);
                break;
            case PADDLE_SHRINK:
                double shrinkWidth = paddle.getWidth() - 20;
                if (shrinkWidth < Constants.MIN_PADDLE_WIDTH) {
                    shrinkWidth = Constants.MIN_PADDLE_WIDTH;
                }
                paddle.setWidth(shrinkWidth);
                break;
            case LASER_PADDLE:
                gameState.setPaddleHasLaser(true);
                // Tính thời điểm hết hạn = thời gian hiện tại + thời gian hiệu lực
                long currentTime = System.currentTimeMillis();
                gameState.setLaserEndTime(currentTime + Constants.LASER_DURATION_MS);
                gameState.setNextLaserFireTime(currentTime);
                break;
        }
    }

    private void updateLasers() {
        Iterator<LaserBullet> it = gameState.getBullets().iterator();
        while (it.hasNext()) {
            LaserBullet bullet = it.next();
            bullet.move();

            // Kiểm tra va chạm đạn với tất cả gạch
            for (Brick brick : gameState.getBricks()) {
                if (bullet.collision(brick)) {
                    brick.hit(0.5);

                    if (brick.isDestroyed()) {
                        gameState.incrementScore(10);
                        spawnItemIfPossible(brick);

                        // Xử lý nổ nếu là gạch nổ
                        if (brick instanceof ExplosiveBrick) {
                            handleExplosiveBrick((ExplosiveBrick) brick);
                        }
                    }
                    // Xóa đạn
                    it.remove();
                    break;
                }
            }
            // Xóa đạn nếu bay ra khỏi màn hình
            if (!bullet.isActive()) {
                it.remove();
            }
        }
    }
}