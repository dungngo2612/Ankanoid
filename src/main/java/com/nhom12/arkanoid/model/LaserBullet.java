package com.nhom12.arkanoid.model;

import com.nhom12.arkanoid.utils.Constants;

public class LaserBullet {
    private double x, y;

    private double width = Constants.LASER_BULLET_WIDTH;
    private double height = Constants.LASER_BULLET_HEIGHT;
    private double speedY = Constants.LASER_BULLET_SPEED;

    private boolean active = true;

    public LaserBullet(double x, double y) {
        // Căn giữa viên đạn
        this.x = x - width / 2;
        this.y = y - height;
    }

    public void move() {
        y += speedY;
        if (y < 0) {
            active = false;
        }
    }

    public boolean isActive() { return active; }
    public double getX() { return x; }
    public double getY() { return y; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }

    // Hàm kiểm tra va chạm của đạn với gạch
    public boolean collision(Brick b) {
        return !b.isDestroyed() &&
                x < b.getX() + b.getWidth() &&
                x + width > b.getX() &&
                y < b.getY() + b.getHeight() &&
                y + height > b.getY();
    }
}