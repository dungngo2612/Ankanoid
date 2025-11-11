package com.nhom12.arkanoid.logic;

import com.nhom12.arkanoid.model.*;
import com.nhom12.arkanoid.utils.Constants;
import com.nhom12.arkanoid.utils.SoundManager;
import com.nhom12.arkanoid.model.Enemy;   // THÊM DÒNG NÀY
import com.nhom12.arkanoid.model.Minion; // THÊM DÒNG NÀY
import javafx.scene.shape.Rectangle;  // THÊM DÒNG NÀY

public class CollisionManager {
    // Handle bóng va chạm với paddle;
    public void handlePaddleCollision(Ball ball, Paddle paddle) {

        //Bóng
        double ballX = ball.getX();
        double ballY = ball.getY();
        double ballRadius = ball.getRadius();

        //Thanh đỡ
        double paddleX = paddle.getX();
        double paddleY = paddle.getY();
        double paddleWidth = paddle.getWidth();
        double paddleHeight = paddle.getHeight();

        //Tìm điểm trên thanh paddle mà nó gần với tâm nhất
        double closestX;
        if (ballX < paddleX) {
            // Nếu tâm bóng ở bên trái của thanh đỡ thì điểm gần nhất là cạnh trái
            closestX = paddleX;
        } else if (ballX > paddleX + paddleWidth) {
            // Nếu tâm bóng ở bên phải của thanh đỡ thì điểm gần nhất là cạnh phải
            closestX = paddleX + paddleWidth;
        } else {
            // Nếu tâm bóng nằm giữa cạnh trái và phải thì điểm gần nhất có cùng tọa độ X với bóng
            closestX = ballX;
        }

        //Tương tự với trục Y
        double closestY;
        if (ballY < paddleY) {
            closestY = paddleY;
        } else if (ballY > paddleY + paddleHeight) {
            closestY = paddleY + paddleHeight;
        } else {
            closestY = ballY;
        }

        double distanceX = ballX - closestX;
        double distanceY = ballY - closestY;

        double distance = Math.sqrt((distanceX * distanceX) + (distanceY * distanceY));

        boolean isColliding = distance < ballRadius;

        if (isColliding) {
            SoundManager.getInstance().playEffect("hit_paddle");

            // tính vị trí va chạm tương đối trên paddle (từ -1 đến 1)
            double locationX = (ballX - (paddleX + paddleWidth / 2)) / (paddleWidth / 2);

            // tính góc phản xạ: càng lệch thì góc càng lớn
            double bounceAngle = locationX * Math.toRadians(75); // độ lệch 75 độ

            // tính vận tốc mới
            double speed = Math.sqrt(ball.getDx() * ball.getDx() + ball.getDy() * ball.getDy());
            ball.setDx(speed * Math.sin(bounceAngle));
            ball.setDy(-Math.abs(speed * Math.cos(bounceAngle))); // luôn đi lên

            // Đặt lại vị trí bóng để tránh kẹt
            ball.setY(paddleY - ballRadius - 1);
        }


    }

    // Handle bóng va chạm với brick
    public boolean handleBrickCollision(Ball ball, Brick brick, boolean moltenBallActive) {
        if (brick.isDestroyed()) {
            return false;
        }

        //Bóng
        double ballX = ball.getX();
        double ballY = ball.getY();
        double ballRadius = ball.getRadius();

        //Brick
        double brickX = brick.getX();
        double brickY = brick.getY();
        double brickWidth = brick.getWidth();
        double brickHeight = brick.getHeight();

        double closestX;
        if (ballX < brickX) {
            closestX = brickX;
        } else if (ballX > brickX + brickWidth) {
            closestX = brickX + brickWidth;
        } else {
            closestX = ballX;
        }

        double closestY;
        if (ballY < brickY) {
            closestY = brickY;
        } else if (ballY > brickY + brickHeight) {
            closestY = brickY + brickHeight;
        } else {
            closestY = ballY;
        }

        double distanceX = ballX - closestX;
        double distanceY = ballY - closestY;

        double distance = Math.sqrt((distanceX * distanceX) + (distanceY * distanceY));

        boolean isColliding = distance < ballRadius;
        if (isColliding) {
            SoundManager.getInstance().playEffect("hit_brick");
            //Tính phần giao nhau giữa ball và brick ở các phía
            double overlapLeft = (ballX + ballRadius) - brickX;
            double overlapRight = (brickX + brickWidth) - (ballX - ballRadius);
            double overlapTop = (ballY + ballRadius) - brickY;
            double overlapBottom = (brickY + brickHeight) - (ballY - ballRadius);

            // Tìm ra giá trị giao nhau nhỏ nhất.
            double minOverlapX = Math.min(overlapLeft, overlapRight);
            double minOverlapY = Math.min(overlapTop, overlapBottom);

//            if (minOverlapX < minOverlapY) {
//                //-->Bóng va vào cạnh ngang
//                ball.reverseX();
//            } else {
//                //-->Bóng va vào cạnh dọc
//                ball.reverseY();
//            }
            // nếu bóng đang ở chế đọ molten thì sẽ không bị nảy
            if (!moltenBallActive) {
                if (minOverlapX < minOverlapY) {
                    ball.reverseX();
                } else {
                    ball.reverseY();
                }
            }
            return true;
        }
        return false;
    }

    // Nếu ball chạm tường trái, phải hay là bên trên sẽ bị bật lại
    public void handleWallCollision(Ball ball) {
        // tường trái
        if (ball.getX() - ball.getRadius() <= 0) {
            SoundManager.getInstance().playEffect("ball_bounce");
            ball.reverseX();
            ball.setX(ball.getRadius()); // đẩy bóng ra khỏi tường trái
        }
        // tường phải
        else if (ball.getX() + ball.getRadius() >= Constants.SCENE_WIDTH) {
            SoundManager.getInstance().playEffect("ball_bounce");
            ball.reverseX();
            ball.setX(Constants.SCENE_WIDTH - ball.getRadius()); // đẩy bóng ra khỏi tường phải
        }

        // tường trên
        if (ball.getY() - ball.getRadius() <= 0) {
            SoundManager.getInstance().playEffect("ball_bounce");
            ball.reverseY();
            ball.setY(ball.getRadius()); // dẩy bóng ra khỏi tường trên
        }
    }

    /**
     * Phương thức MỚI: Kiểm tra va chạm giữa Minion và Paddle
     * @param minion
     * @param paddle
     * @return true nếu va chạm
     */
    public boolean handlePaddleCollision(Minion minion, Paddle paddle) {
        Rectangle minionBounds = minion.getBounds();
        Rectangle paddleBounds = new Rectangle(paddle.getX(), paddle.getY(), paddle.getWidth(), paddle.getHeight());
        return minionBounds.intersects(paddleBounds.getBoundsInLocal());
    }

    /**
     * Phương thức MỚI: Overload handleEnemyCollision để chấp nhận Enemy (Boss/Minion)
     * (Code này gần giống hệt handleEnemyCollision của bạn, chỉ khác kiểu tham số)
     */
    public boolean handleEnemyCollision(Ball ball, Enemy enemy, boolean moltenBallActive) {
        if (enemy.isDestroyed()) {
            return false;
        }

        double ballX = ball.getX();
        double ballY = ball.getY();
        double ballRadius = ball.getRadius();

        double enemyX = enemy.getX();
        double enemyY = enemy.getY();
        double enemyWidth = enemy.getWidth();
        double enemyHeight = enemy.getHeight();

        double closestX;
        if (ballX < enemyX) closestX = enemyX;
        else if (ballX > enemyX + enemyWidth) closestX = enemyX + enemyWidth;
        else closestX = ballX;

        double closestY;
        if (ballY < enemyY) closestY = enemyY;
        else if (ballY > enemyY + enemyHeight) closestY = enemyY + enemyHeight;
        else closestY = ballY;

        double distanceX = ballX - closestX;
        double distanceY = ballY - closestY;
        double distance = Math.sqrt((distanceX * distanceX) + (distanceY * distanceY));

        boolean isColliding = distance < ballRadius;
        if (isColliding) {
            // SoundManager.getInstance().playEffect("hit_brick"); // Có thể thay bằng âm thanh khác cho boss

            if (!moltenBallActive) { // Bóng nảy lại
                double overlapLeft = (ballX + ballRadius) - enemyX;
                double overlapRight = (enemyX + enemyWidth) - (ballX - ballRadius);
                double overlapTop = (ballY + ballRadius) - enemyY;
                double overlapBottom = (enemyY + enemyHeight) - (ballY - ballRadius);

                double minOverlapX = Math.min(overlapLeft, overlapRight);
                double minOverlapY = Math.min(overlapTop, overlapBottom);

                if (minOverlapX < minOverlapY) {
                    ball.reverseX();
                } else {
                    ball.reverseY();
                }
            }
            // Nếu là moltenBallActive, nó vẫn trả về true (va chạm) nhưng bóng không nảy
            return true;
        }
        return false;
    }
}
