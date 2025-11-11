package com.nhom12.arkanoid.model;

import com.nhom12.arkanoid.utils.Constants;

public class NormalBrick extends Brick {
    public NormalBrick(double x, double y) {
        super(x, y, Constants.BRICK_WIDTH, Constants.BRICK_HEIGHT, 1.0);
    }
}