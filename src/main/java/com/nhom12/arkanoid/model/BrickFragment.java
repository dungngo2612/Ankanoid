package com.nhom12.arkanoid.model;

import com.nhom12.arkanoid.utils.ImageManager;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class BrickFragment extends Rectangle {
    private double dx, dy;

    public BrickFragment(double x, double y, Image color) {
        super(x, y, 6, 6); // mảnh vuông nhỏ
        Image brickImage = ImageManager.getInstance().showImage("brick1"); // hoặc "brick2" tùy loại
        setFill(new ImagePattern(brickImage));
        dx = (Math.random() - 0.5) * 4;
        dy = (Math.random() - 0.5) * 4;
    }

    public void update() {
        setX(getX() + dx);
        setY(getY() + dy);
        dy += 0.2;
    }
}