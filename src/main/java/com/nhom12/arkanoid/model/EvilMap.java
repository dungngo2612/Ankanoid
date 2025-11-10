package com.nhom12.arkanoid.model;

import com.nhom12.arkanoid.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class EvilMap {
    private List<Brick> bricks= new ArrayList<>();
    private long lastPushTime = System.currentTimeMillis();
    private final double deathLineY = Constants.SCENE_HEIGHT - 100;

    private boolean isInverted = true;

    public EvilMap() {
    }

    // khởi taọ map bạn đầu
    public void initMap() {
        bricks.clear();
        for (int row = 0; row < Constants.BRICK_ROWS; row++) {
            Brick[] rowPattern = generateTriangleRow(true, row); // tam giác ngược
            double y = row * (Constants.BRICK_HEIGHT + Constants.BRICK_GAP) + Constants.BRICK_GAP + 30;

            for (int col = 0; col < Constants.BRICK_COLS; col++) {
                if (rowPattern[col] != null) {
                    Brick b = rowPattern[col];
                    b.setY(y);
                    bricks.add(b);
                }
            }
        }
    }

    // ktra xem có cần đùn gạch không
    public void update() {
        long now = System.currentTimeMillis();
        if(now - lastPushTime >= 30000) {
            pushBrickDown();
            lastPushTime = now;
        }
    }

    public void pushBrickDown() {
        for (Brick brick: bricks) {
            brick.setY(brick.getY() + Constants.BRICK_GAP + Constants.BRICK_HEIGHT);
        }

        Brick[] newRow = generateTriangleRow(isInverted, 0);
        isInverted = !isInverted;
        double y = Constants.BRICK_GAP + 30;
        for (int col = 0; col < Constants.BRICK_COLS; col++) {
            if (newRow[col] != null) {
                Brick b = newRow[col];
                b.setY(y);
                bricks.add(0, b); // Thêm vào đầu danh sách
            }
        }

    }

    private Brick[] generateTriangleRow(boolean isInverted, int rowIndex) {
        Brick[] row = new Brick[Constants.BRICK_COLS];
        int center = Constants.BRICK_COLS/2;

        int maxDistance = 0;
        if(isInverted) {
            maxDistance = rowIndex; // tam giác ngược , càng xuống càng rộng
        } else {
            maxDistance = Math.max(0, Constants.BRICK_ROWS - 1 - rowIndex);
            // tam giác xuôi càng xuống càng hẹp
        }
        for(int col = 0; col < Constants.BRICK_COLS; col++) {
            double x = col*(Constants.BRICK_WIDTH + Constants.BRICK_GAP) + Constants.BRICK_GAP;
            int distance = Math.abs(col - center);

            boolean shouldPlace;

            if(isInverted) {
                shouldPlace = distance <= maxDistance;
                // nếu là tam giác ngược sẽ dặt gạch nế  cột gần trung tâm
            } else {
                shouldPlace = distance >= maxDistance;
                // nếu là tam giác xuôi sẽ dặt gạch nế  cột cách xa trung tâm
            }

            if(shouldPlace) {
                if(distance == maxDistance) {
                    row[col] = new StrongBrick(x,0); // gạch rìa
                } else {
                    row[col] = new NormalBrick(x,0); // gạch ở bên trong
                }
            }
        }
        return row;
    }

    public boolean isGameOver() {
        for(Brick brick : bricks) {
            if(brick.getY() + brick.getHeight() > deathLineY){
                return true;
            }
        }
        return false;
    }

    public double getDeathLineY() {
        return deathLineY;
    }

    public List<Brick> getBricks() {
        return bricks;
    }
}
