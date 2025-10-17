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

    // kiểm tra xem ball có chạm brick
    public boolean checkBallHitsBrick(Ball ball, Brick brick) {

        double ballBottom = ball.getY() + ball.getRadius();
        double ballLeft = ball.getX() - ball.getRadius();
        double ballRight = ball.getX() + ball.getRadius();

        if ((ballBottom >= brick.getY()) && (ball.getY() <= brick.getY())) {
            if (ballLeft >= brick.getX() && ballRight <= brick.getY()) {
                return true;
            }
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
