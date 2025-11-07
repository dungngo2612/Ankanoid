package com.nhom12.arkanoid.model;
import javafx.geometry.Point2D;
import java.util.Deque;
import java.util.LinkedList;
public class Ball {

    private double x;
    private double y;
    private double radius;
    //Vận tốc theo trục x và y
    private double dx;
    private double dy;
    // Vệt sáng sẽ có 10 "hạt"
    private static final int MAX_TRAIL_LENGTH = 50;
    private Deque<Point2D> recentPositions = new LinkedList<>();

    public Ball(){}

    public Ball(double x, double y, double radius, double dx, double dy) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.dx = dx;
        this.dy = dy;
    }

    //Cập nhật vị trí quả bóng
    public void move() {
        x += dx;
        y += dy;

        // Thêm vị trí mới vào đầu danh sách
        recentPositions.addFirst(new Point2D(this.x, this.y));

        // Xóa vị trí cũ nhất nếu vệt quá dài
        if (recentPositions.size() > MAX_TRAIL_LENGTH) {
            recentPositions.removeLast();
        }
    }

    // Đổi hướng đi của bóng
    public void reverseX() {
        dx = -dx;
    }

    public void reverseY() {
        dy = -dy;
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

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public double getDx() {
        return dx;
    }

    public void setDx(double dx) {
        this.dx = dx;
    }

    public double getDy() {
        return dy;
    }

    public void setDy(double dy) {
        this.dy = dy;
    }

    public Deque<Point2D> getRecentPositions() {
        return recentPositions;
    }
}