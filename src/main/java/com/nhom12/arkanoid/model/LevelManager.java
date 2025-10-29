package com.nhom12.arkanoid.model;

import com.nhom12.arkanoid.utils.Constants;
import java.util.ArrayList;
import java.util.List;

public class LevelManager {

    public enum LevelDifficulty{
        EASY,
        NORMAL,
        DIFFICULLT
    }

    public static List<BrickGroup> createLevel(LevelDifficulty level) {
        List<BrickGroup> Bricks = new ArrayList<>();

        switch (level) {
            case EASY :
                Bricks = buildEasyLevel();
                break;
            case NORMAL:
                Bricks = buildNormalLevel();
                break;
            case DIFFICULLT:
                Bricks = buildDifficultyLevel();
                break;
        }

        return Bricks;
    }

    //Map của màn dễ
    private static List<BrickGroup> buildEasyLevel(){
        List<BrickGroup> Bricks = new ArrayList<>();
        for (int row = 0; row < Constants.BRICK_ROWS; row++) {
            for (int col = 0; col < Constants.BRICK_COLS; col++) {
                double x = col * (Constants.BRICK_WIDTH + Constants.BRICK_GAP) + Constants.BRICK_GAP;
                double y = row * (Constants.BRICK_HEIGHT + Constants.BRICK_GAP) + Constants.BRICK_GAP + 30;
                Bricks.add(new BrickGroup(x,y,Constants.BRICK_WIDTH,Constants.BRICK_HEIGHT,BrickGroup.Type.NORMAL));
            }
        }
        return Bricks;
    }

    // Map của màn trung bình
    private static List<BrickGroup> buildNormalLevel() {
        List<BrickGroup> Bricks = new ArrayList<>();

        for(int row = 1; row < Constants.BRICK_ROWS - 1; row++) {
            for(int col = 0; col < Constants.BRICK_COLS / 2; col++) {
                double x = col * (Constants.BRICK_WIDTH + Constants.BRICK_GAP) + Constants.BRICK_GAP;
                double y = row * (Constants.BRICK_HEIGHT + Constants.BRICK_GAP) + Constants.BRICK_GAP + 30;
                // cột trong cùng là gạch nổ
                if(col == 0) {
                    Bricks.add(new BrickGroup(x,y,Constants.BRICK_WIDTH,Constants.BRICK_HEIGHT,BrickGroup.Type.EXPLOSIVE));
                } else if(col == (Constants.BRICK_COLS / 2 - 1) ) {
                    //cột ở giữa trống còn cột cạnh cột giữa bên trái là Strong
                    Bricks.add(new BrickGroup(x,y,Constants.BRICK_WIDTH,Constants.BRICK_HEIGHT,BrickGroup.Type.STRONG));
                } else {
                    Bricks.add(new BrickGroup(x,y,Constants.BRICK_WIDTH,Constants.BRICK_HEIGHT,BrickGroup.Type.NORMAL));
                }
            }

            for(int col = 1 + Constants.BRICK_COLS / 2; col < Constants.BRICK_COLS; col++) {
                double x = col * (Constants.BRICK_WIDTH + Constants.BRICK_GAP) + Constants.BRICK_GAP;
                double y = row * (Constants.BRICK_HEIGHT + Constants.BRICK_GAP) + Constants.BRICK_GAP + 30;
                if(col == (Constants.BRICK_COLS - 1) )
                    Bricks.add(new BrickGroup(x, y, Constants.BRICK_WIDTH, Constants.BRICK_HEIGHT,  BrickGroup.Type.EXPLOSIVE));
                else if(col == (Constants.BRICK_COLS / 2 ) + 1 ) {
                    Bricks.add(new BrickGroup(x,y,Constants.BRICK_WIDTH,Constants.BRICK_HEIGHT,BrickGroup.Type.STRONG));
                } else {
                    Bricks.add(new BrickGroup(x,y,Constants.BRICK_WIDTH,Constants.BRICK_HEIGHT,BrickGroup.Type.NORMAL));
                }
            }
        }
        for(int col = 0; col < Constants.BRICK_COLS; col++) {
            double x = col * (Constants.BRICK_WIDTH + Constants.BRICK_GAP) + Constants.BRICK_GAP;
            double y = (Constants.BRICK_ROWS - 1) * (Constants.BRICK_HEIGHT + Constants.BRICK_GAP) + Constants.BRICK_GAP + 30;
            if(col == Constants.BRICK_COLS / 2) continue;
            Bricks.add(new BrickGroup(x,y,Constants.BRICK_WIDTH,Constants.BRICK_HEIGHT,BrickGroup.Type.UNBREAKABLE));
        }

        return Bricks;
    }

    // Map của màn khó
    private static List<BrickGroup> buildDifficultyLevel() {
        List<BrickGroup> Bricks = new ArrayList<>();

        for(int row = Constants.BRICK_ROWS - 2; row >=1; row--) {
            for(int col = 0; col < (Constants.BRICK_COLS / 2) - row + 1; col++) {
                double x = col * (Constants.BRICK_WIDTH + Constants.BRICK_GAP) + Constants.BRICK_GAP;
                double y = row * (Constants.BRICK_HEIGHT + Constants.BRICK_GAP) + Constants.BRICK_GAP + 30;
                // cột trong cùng là gạch nổ
                if(col == 0) {
                    Bricks.add(new BrickGroup(x,y,Constants.BRICK_WIDTH,Constants.BRICK_HEIGHT,BrickGroup.Type.EXPLOSIVE));
                } else if(col == ((Constants.BRICK_COLS / 2) - row) ) {
                    Bricks.add(new BrickGroup(x,y,Constants.BRICK_WIDTH,Constants.BRICK_HEIGHT,BrickGroup.Type.STRONG));
                } else {
                    Bricks.add(new BrickGroup(x,y,Constants.BRICK_WIDTH,Constants.BRICK_HEIGHT,BrickGroup.Type.NORMAL));
                }
            }

            for(int col =  row - 1 + (Constants.BRICK_COLS / 2) ; col < Constants.BRICK_COLS; col++) {
                double x = col * (Constants.BRICK_WIDTH + Constants.BRICK_GAP) + Constants.BRICK_GAP;
                double y = row * (Constants.BRICK_HEIGHT + Constants.BRICK_GAP) + Constants.BRICK_GAP + 30;
                if(col == (Constants.BRICK_COLS - 1) )
                    Bricks.add(new BrickGroup(x, y, Constants.BRICK_WIDTH, Constants.BRICK_HEIGHT,  BrickGroup.Type.EXPLOSIVE));
                else if(col == (Constants.BRICK_COLS / 2) + row -1 ) {
                    Bricks.add(new BrickGroup(x,y,Constants.BRICK_WIDTH,Constants.BRICK_HEIGHT,BrickGroup.Type.STRONG));
                } else {
                    Bricks.add(new BrickGroup(x,y,Constants.BRICK_WIDTH,Constants.BRICK_HEIGHT,BrickGroup.Type.NORMAL));
                }
            }
        }
        for(int col = 0; col < Constants.BRICK_COLS; col++) {
            double x = col * (Constants.BRICK_WIDTH + Constants.BRICK_GAP) + Constants.BRICK_GAP;
            double y = (Constants.BRICK_ROWS - 1) * (Constants.BRICK_HEIGHT + Constants.BRICK_GAP) + Constants.BRICK_GAP + 30;
            if(col == Constants.BRICK_COLS / 2) continue;
            Bricks.add(new BrickGroup(x,y,Constants.BRICK_WIDTH,Constants.BRICK_HEIGHT,BrickGroup.Type.UNBREAKABLE));
        }
        for(int col = 0; col < Constants.BRICK_COLS; col++) {
            double x = col * (Constants.BRICK_WIDTH + Constants.BRICK_GAP) + Constants.BRICK_GAP;
            double y = 0 * (Constants.BRICK_HEIGHT + Constants.BRICK_GAP) + Constants.BRICK_GAP + 30;
            if(col == Constants.BRICK_COLS / 2) continue;
            Bricks.add(new BrickGroup(x,y,Constants.BRICK_WIDTH,Constants.BRICK_HEIGHT,BrickGroup.Type.UNBREAKABLE));
        }

        return Bricks;
    }


}
