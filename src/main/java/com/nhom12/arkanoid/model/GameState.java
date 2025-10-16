package com.nhom12.arkanoid.model;

public class GameState {
    private Ball ball;
    private  Paddle paddle;
    private final int screenWidth;
    private final int screenHeight;

    // paddle nằm giữa trên thanh dưới màn hình
    // quả bóng ở giữa paddle
    public GameState(int screenHeight, int screenWidth) {
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;

        paddle = new Paddle(screenWidth/2 -40 , screenHeight -40, 100,20);
        resetBall();
    }


    // method reset vị trí của bóng tiện cho sử dụng lại khi mà resetgame
    public void resetBall() {
        double ballX = paddle.getX() + paddle.getWidth()/2;
        double ballY = paddle.getY() + paddle.getHeight()/2;

        ball = new Ball(ballX,ballY, 8, 0,-4,4);
    }


}
