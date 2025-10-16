package com.nhom12.arkanoid.model;

public class Brick {

    private double x;
    private double y;
    private double width;
    private double height;
    private int health;
    private boolean isDestroyed;

    public Brick(double x, double y, double width, double height, int health) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.health = health;
        this.isDestroyed = false;
    }

    //Giảm máu khi đánh trúng
    public void hit() {
        this.health--;
        if (this.health <= 0) {
            this.isDestroyed = true;
        }
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }
}