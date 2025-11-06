package com.nhom12.arkanoid.model;

public class GameResult {
    public int score;
    public long timeInSeconds;

    public GameResult(int score, long timeInSeconds) {
        this.score = score;
        this.timeInSeconds = timeInSeconds;
    }
}
