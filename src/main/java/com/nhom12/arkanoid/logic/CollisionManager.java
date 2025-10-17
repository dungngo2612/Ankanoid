package com.nhom12.arkanoid.logic;

import com.nhom12.arkanoid.model.*;
import com.nhom12.arkanoid.utils.Constants;


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
            ball.reverseY();
        }
    }

    // Handle bóng va chạm với brick
    public boolean handleBrickCollision(Ball ball, Brick brick) {
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
            brick.hit();
            //Tính phần giao nhau giữa ball và brick ở các phía
            double overlapLeft = (ballX + ballRadius) - brickX;
            double overlapRight = (brickX + brickWidth) - (ballX - ballRadius);
            double overlapTop = (ballY + ballRadius) - brickY;
            double overlapBottom = (brickY + brickHeight) - (ballY - ballRadius);

            // Tìm ra giá trị giao nhau nhỏ nhất.
            double minOverlapX = Math.min(overlapLeft, overlapRight);
            double minOverlapY = Math.min(overlapTop, overlapBottom);

            if (minOverlapX < minOverlapY) {
                //-->Bóng va vào cạnh ngang
                ball.reverseX();
            } else {
                //-->Bóng va vào cạnh dọc
                ball.reverseY();
            }
            return true;
        }
        return false;
    }

    // Nếu ball chạm tường trái, phải hay là bên trên sẽ bị bật lại
    public void handleWallCollision(Ball ball) {
        // Tường trái và phải
        if (ball.getX() - ball.getRadius() <= 0 || ball.getX() + ball.getRadius() >= Constants.SCENE_WIDTH) {
            ball.reverseX();
        }
        // Tường trên
        if (ball.getY() - ball.getRadius() <= 0) {
            ball.reverseY();
        }
    }
}
