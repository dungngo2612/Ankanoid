package com.nhom12.arkanoid.utils;

public class Constants {
    public static final double SCENE_WIDTH = 800;
    public static final double SCENE_HEIGHT = 600;

    public static final double PADDLE_WIDTH = 150;
    public static final double PADDLE_HEIGHT = 40;
    public static final double PADDLE_START_Y = SCENE_HEIGHT - 50;
    public static final double PADDLE_SPEED = 5.0;
    public static final double MAX_PADDLE_WIDTH = 300;
    public static final double MIN_PADDLE_WIDTH = 40;
    public static final double MAX_PADDLE_HEIGHT = 60;

    public static final double BALL_RADIUS = 8;
    public static final double BALL_SPEED = 2;
    public static final double MAX_BALL_SPEED = 12.0;
    public static final double MIN_BALL_SPEED = 1.0;

    public static final int BRICK_ROWS = 6;
    public static final int BRICK_COLS = 10;
    public static final double BRICK_WIDTH = 75;
    public static final double BRICK_HEIGHT = 20;
    public static final double BRICK_GAP = 5;

    public static final int MAX_LIVES = 3;

    public static final double LASER_BULLET_WIDTH = 50;
    public static final double LASER_BULLET_HEIGHT = 80;
    public static final double LASER_BULLET_SPEED = -8.0;
    // Thời gian hiệu lực laze (3000ms = 3 giây)
    public static final long LASER_DURATION_MS = 3000;
    // Thời gian giữa 2 lần bắn
    public static final long LASER_FIRE_RATE_MS = 500;
}