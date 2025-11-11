package com.nhom12.arkanoid.model;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class BrickFragment extends Rectangle {
    private double dx, dy;

    public BrickFragment(double x, double y, Image image) {
        super(x, y, 6, 6); // mảnh vuông nhỏ
        setFill(new ImagePattern(image));
        dx = (Math.random() - 0.5) * 4;
        dy = (Math.random() - 0.5) * 4;
    }

    public void update() {
        setX(getX() + dx);
        setY(getY() + dy);
        dy += 0.2;
    }
}