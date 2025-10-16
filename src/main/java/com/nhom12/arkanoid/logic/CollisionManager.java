package com.nhom12.arkanoid.logic;

import com.nhom12.arkanoid.model.*;



public class CollisionManager {
    // Kiểm tra bóng va chạm với paddle;
    public  boolean checkBallHitsPaddle(Ball ball, Paddle paddle) {
        double ballBottom = ball.getY() + ball.getRadius();
        double ballLeft = ball.getX() - ball.getRadius();
        double ballRight = ball.getX() + ball.getRadius();

        if ( (ballBottom >= paddle.getY()) && (ball.getY() <= paddle.getY() + 5) ) {
            if(ballLeft >= paddle.getX() && ballRight <= paddle.getY()) {
                return true;
            }
        }
        return false;
    }

    // kiểm tra xem ball có chạm brick
    public boolean checkBallHitsBrick(Ball ball, Brick brick){

        double ballBottom = ball.getY() + ball.getRadius();
        double ballLeft = ball.getX() - ball.getRadius();
        double ballRight = ball.getX() + ball.getRadius();

        if ( (ballBottom >= brick.getY()) && (ball.getY() <= brick.getY()) ) {
            if(ballLeft >= brick.getX() && ballRight <= brick.getY()){
                return true;
            }
        }
        return false;
    }

    // Nếu ball chạm tường trái, phải hay là bên trên sẽ bị bật lại
    public void handleWallCollision(Ball ball, double screenWidth, double screenHeight) {
        double ballLeft = ball.getX() - ball.getRadius();
        double ballRight = ball.getY() + ball.getRadius();

        if(ballLeft <= 0 || ballRight >= screenWidth) {
            ball.reverseX();
        }

        if(ball.getY() - ball.getRadius() <= 0) {
            ball.reverseY();
        }
    }
}
