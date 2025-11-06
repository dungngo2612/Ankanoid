package com.nhom12.arkanoid.logic;

import com.nhom12.arkanoid.model.*;
import com.nhom12.arkanoid.utils.Constants;
import com.nhom12.arkanoid.utils.ImageManager;
import com.nhom12.arkanoid.utils.ParticleManager;
import com.nhom12.arkanoid.model.Brick;
import com.nhom12.arkanoid.model.ExplosiveBrick;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;


public class GameEngine {
    private final GameState gameState;
    private final CollisionManager collisionManager;
    private Pane gameRoot;

    private final List<Items> items = new ArrayList<>();

    private static final int MAX_ITEMS = 100; // tối đa 2 vật phẩm trong 1 màn
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
            gameState.getMainBall().setX(
                    gameState.getPaddle().getX() + gameState.getPaddle().getWidth() / 2
            );
            gameState.getMainBall().setY(
                    gameState.getPaddle().getY() - gameState.getMainBall().getRadius() - 2
            );
            return;
        }


        Iterator<Ball> ballIterator = gameState.getBalls().iterator();
        while (ballIterator.hasNext()) {
            Ball ball = ballIterator.next();

            // 1. Di chuyển bóng
            ball.move();

            // 2. Xử lý va chạm tường và paddle
            collisionManager.handleWallCollision(ball);
            collisionManager.handlePaddleCollision(ball, gameState.getPaddle());

            // 3. Xử lý bóng rơi (và xóa bóng an toàn)
            if (ball.getY() > Constants.SCENE_HEIGHT) {
                ballIterator.remove(); // Xóa bóng
                continue; // Chuyển sang bóng tiếp theo
            }

            // Lặp qua tất cả gạch cho mỗi quả bóng
            for (Brick brick : gameState.getBricks()) {
                // nếu ăn item  molten thì kiểm tra va chạm với brick
                if (gameState.isMoltenBallActive()) {
                    // nếu là loại normal, Strong, hay explosive thì bóng sẽ đi qua và brick đó bị vỡ
                    if (brick.isDestructible() && !brick.isDestroyed()) {
                        boolean collided = collisionManager.handleBrickCollision(ball, brick, gameState.isMoltenBallActive()); // ✅ kiểm tra va chạm
                        if (collided) {
                            brick.hit(1.0);
                            if (brick.isDestroyed()) {
                                gameState.incrementScore(10);
                                spawnItemIfPossible(brick);
                                if (brick instanceof ExplosiveBrick) {
                                    handleExplosiveBrick((ExplosiveBrick) brick);
                                }

                                String imageKey = "brick1";
                                if (brick instanceof StrongBrick) imageKey = "brick2";
                                else if (brick instanceof ExplosiveBrick) imageKey = "explosive_brick";

                                Image brickImage = ImageManager.getInstance().showImage(imageKey);
                                ParticleManager.spawnBrickFragments(
                                        brick.getX() + brick.getWidth() / 2,
                                        brick.getY() + brick.getHeight() / 2,
                                        brickImage,
                                        gameRoot
                                );
                                gameState.checkWinCondition();
                            }
                        }
                    } else if (brick instanceof UnbreakableBrick) { // nếu là unbreakable thì bóng sẽ bị nảy như bình thường
                        collisionManager.handleBrickCollision(ball, brick, false);
                    }
                    gameState.checkWinCondition();
                } else {
                    if (collisionManager.handleBrickCollision(ball, brick,false)) { // Kiểm tra va chạm cho bóng hiện tại
                        brick.hit(1.0);
                        if (brick.isDestroyed()) {
                            gameState.incrementScore(10);
                            spawnItemIfPossible(brick);
                            // Chỉ xử lý nổ sau khi gạch đã bị phá hủy
                            if (brick instanceof ExplosiveBrick) {
                                handleExplosiveBrick((ExplosiveBrick) brick);
                            }

                            // lấy màu của mảnh vỡ theo màu chủ đạo của từng loại brick
                            String imageKey = "brick1";
                            if (brick instanceof StrongBrick) imageKey = "brick2";
                            else if (brick instanceof UnbreakableBrick) imageKey = "impassable";
                            else if (brick instanceof ExplosiveBrick) imageKey = "explosive_brick";

                            Image brickImage = ImageManager.getInstance().showImage(imageKey);

                            ParticleManager.spawnBrickFragments(
                                    brick.getX() + brick.getWidth() / 2,
                                    brick.getY() + brick.getHeight() / 2,
                                    brickImage,
                                    gameRoot
                            );
                        }
                        gameState.checkWinCondition();// check win
                    }
                }
            }
        }

        // Kiểm tra mất mạng (chỉ khi không còn bóng nào)
        if (gameState.getBalls().isEmpty()) {
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
        Ball ball = gameState.getMainBall();

        switch (type) {
            case EXTRA_LIFE:
                if (gameState.getLives() < Constants.MAX_LIVES) {
                    gameState.setLives(gameState.getLives() + 1);
                }
                break;
            case SPEED_UP:

                Ball balls = gameState.getMainBall();
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
            case MULTI_BALL:
                System.out.println("Multi_ball activated");
                double x = gameState.getBalls().get(0).getX();
                double y = gameState.getBalls().get(0).getY();
                double vx = gameState.getBalls().get(0).getDx();
                double vy = gameState.getBalls().get(0).getDy();
                Random rand = new Random();
                // Vận tốc x ngẫu nhiên từ -2 đến 2 (nhưng không quá gần 0)
                double newDx1 = (rand.nextDouble() * 2.0 + 1.0) * (rand.nextBoolean() ? 1 : -1); // từ 1-3 hoặc -1 đến -3
                double newDx2 = (rand.nextDouble() * 2.0 + 1.0) * (rand.nextBoolean() ? 1 : -1); // từ 1-3 hoặc -1 đến -3

                // Thêm 2 bóng mới vào danh sách
                gameState.getBalls().add(new Ball(x, y, Constants.BALL_RADIUS, newDx1, vy));
                gameState.getBalls().add(new Ball(x, y, Constants.BALL_RADIUS, newDx2, vy));
                break;
            case MOLTEN_BALL:
                gameState.setMoltenBallActive(true);
                gameState.setMoltenBallEndTime(System.currentTimeMillis() + 5000);

                ball = gameState.getMainBall();
                dx = ball.getDx();
                dy = ball.getDy();

                dx *= 1.5;
                dy *= 1.5;

                // Giới hạn không vượt quá MAX_BALL_SPEED
                double speed = Math.sqrt(dx * dx + dy * dy);
                if (speed > Constants.MAX_BALL_SPEED) {
                    dx = (Constants.MAX_BALL_SPEED / speed) * dx;
                    dy = (Constants.MAX_BALL_SPEED / speed) * dy;
                }

                ball.setDx(dx);
                ball.setDy(dy);
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