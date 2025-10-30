package com.nhom12.arkanoid.model;

import com.nhom12.arkanoid.utils.Constants;

public class UnbreakableBrick extends Brick {
    public UnbreakableBrick(double x, double y) {
        super(x, y, Constants.BRICK_WIDTH, Constants.BRICK_HEIGHT, Integer.MAX_VALUE);
    }

    @Override
    public void hit(double damageAmount) {
    }
}