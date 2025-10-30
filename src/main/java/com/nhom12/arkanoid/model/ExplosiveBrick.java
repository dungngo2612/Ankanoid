package com.nhom12.arkanoid.model;

import com.nhom12.arkanoid.utils.Constants;

public class ExplosiveBrick extends Brick {
    public ExplosiveBrick(double x, double y) {
        super(x, y, Constants.BRICK_WIDTH, Constants.BRICK_HEIGHT, 1);
    }

}