package com.nhom12.arkanoid.model;

import com.nhom12.arkanoid.utils.Constants;

public class StrongBrick extends Brick {
    public StrongBrick(double x, double y) {
        super(x, y, Constants.BRICK_WIDTH, Constants.BRICK_HEIGHT, 3.0);
    }
}