package com.nhom12.arkanoid.model;

import com.nhom12.arkanoid.controller.SettingsController;
import com.nhom12.arkanoid.utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.prefs.Preferences;
import com.nhom12.arkanoid.model.Boss;
import com.nhom12.arkanoid.model.Minion;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.text.Text;


public class GameState {
    private List<Ball> ball;
    private Paddle paddle;
    private List<Brick> bricks;
    private int score;
    private int lives;
    private boolean isGameOver;
    private boolean isGameWon;

    private boolean ballLaunched = false;

    private boolean paddleHasLaser = false;
    private List<LaserBullet> bullets = new ArrayList<>();
    // Biến lưu thời điểm hết hiệu ứng
    private long laserEndTime = 0;
    // Biến lưu thời điểm được phép bắn laze tiếp theo
    private long nextLaserFireTime = 0;

    //
    private boolean moltenBallActive = false;
    private long moltenBallEndTime = 0;

    private boolean allowWinCheck = true;

    private EvilMap map = new EvilMap();
    private boolean isEvilMode = false;
    private List<Brick> bricksEvil; // dùng cho ChallengeMode

    private Boss boss;
    private List<Minion> minions;

    private int totalBossProtectionBricks;
    private int remainingBossProtectionBricks;


    public boolean isAllowWinCheck() {
        return allowWinCheck;
    }

    public void setAllowWinCheck(boolean allowWinCheck) {
        this.allowWinCheck = allowWinCheck;
    }

    @FXML
    private Canvas gameCanvas;
    @FXML
    private Text scoreText;
    @FXML
    private Text livesText;


    public GameState() {

        this.paddle = new Paddle(
                (Constants.SCENE_WIDTH - Constants.PADDLE_WIDTH) / 2,
                Constants.PADDLE_START_Y,
                Constants.PADDLE_WIDTH,
                Constants.PADDLE_HEIGHT
        );
        // Vị trí sẽ được reset
        this.ball = new ArrayList<>();
        this.ball.add(new Ball(0, 0, Constants.BALL_RADIUS, 0, 0));
        this.bricks = new ArrayList<>();
        this.score = 0;
        this.lives = 3;
        this.isGameOver = false;
        this.isGameWon = false;

        this.moltenBallActive = false; // Đảm bảo được khởi tạo
        this.paddleHasLaser = false;     // Đảm bảo được khởi tạo
        this.moltenBallEndTime = 0;      // Đảm bảo được khởi tạo
        this.laserEndTime = 0;           // Đảm bảo được khởi tạo
        this.nextLaserFireTime = 0;      // Đảm bảo được khởi tạo
        this.ballLaunched = false;     // Đảm bảo được khởi tạo
        this.allowWinCheck = true;

        Preferences prefs = Preferences.userNodeForPackage(SettingsController.class);
        String difficulty = prefs.get("difficulty", "Easy");
        boolean evilMode = prefs.getBoolean("evilMode", false);

        if (evilMode) {
            setEvilMode(true);
            System.out.println("✅ Evil Mode activated!");
            return; // không cần gọi createLevel nữa
        }
        // THÊM LOGIC BOSS MỚI VÀO ĐÂY
        else if (difficulty.equals("Boss")) {
            System.out.println("Boss Level selected!");
            this.bricks = new ArrayList<>(); // Không có gạch
            this.boss = new Boss(Constants.SCENE_WIDTH / 2 - 150 / 2, 50, 100); // Tạo Boss (giả sử rộng 150, HP 100)
            this.minions = this.boss.getMinions(); // Lấy reference đến danh sách minions của boss

            createBossProtectionBricks();
        } else {
            LevelManager.LevelDifficulty diffEnum;
            switch(difficulty) {
                case "Easy":
                    diffEnum = LevelManager.LevelDifficulty.EASY;
                    break;
                case "Medium":
                    diffEnum = LevelManager.LevelDifficulty.NORMAL;
                    break;
                case "Hard":
                    diffEnum = LevelManager.LevelDifficulty.DIFFICULLT;
                    break;
                default:
                    diffEnum = LevelManager.LevelDifficulty.EASY;
            }
            this.bricks = LevelManager.createLevel(diffEnum); // Sử dụng LevelManager của bạn
        }
        resetBall();

    }


    // Đặt lại bóng về vị trí trên thanh đỡ
    public void resetBall() {
        ball.clear();
        ball.add(new Ball(paddle.getX() + paddle.getWidth() / 2,
                paddle.getY() - Constants.BALL_RADIUS, Constants.BALL_RADIUS, 0, 0));
        setBallLaunched(false);
        setMoltenBallActive(false);
        setMoltenBallEndTime(0);
        // Tắt laze khi reset
        setPaddleHasLaser(false);
        bullets.clear();
        laserEndTime = 0;
    }

    public void launchBall() {
        Random rand = new Random();
        double randomNumber = rand.nextDouble(-Constants.BALL_SPEED, Constants.BALL_SPEED);
        if (!isBallLaunched()) {
            // Bắn lên trên
            ball.get(0).setDx(randomNumber);
            ball.get(0).setDy(-Constants.BALL_SPEED);
            setBallLaunched(true);
        }
    }

    public void setEvilMode(boolean value) {
        isEvilMode = value;
        if (value) {
            map = new EvilMap();
            map.initMap();
        } else {
            bricks = LevelManager.createLevel(LevelManager.LevelDifficulty.NORMAL); // hoặc EASY/HARD
        }
    }

    public List<Ball> getBalls() {
        return ball;
    }

    public Ball getMainBall() {
        return ball.get(0);
    }

    public void setLives(int lives) {
        this.lives = Math.min(lives, Constants.MAX_LIVES);
    }

    public Paddle getPaddle() {
        return paddle;
    }

    public List<Brick> getBricks() {
        return isEvilMode ? map.getBricks() : bricks;
    }

    public int getScore() {
        return score;
    }

    public int getLives() {
        return lives;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public boolean isGameWon() {
        return isGameWon;
    }

    public void setGameWon(boolean isGameWon) {
        this.isGameWon = isGameWon;
    }

    public boolean isBallLaunched() {
        return ballLaunched;
    }

    public void setBallLaunched(boolean ballLaunched) {
        this.ballLaunched = ballLaunched;
    }

    public void loseLife() {
        this.lives--;
        if (this.lives <= 0) {
            this.isGameOver = true;
        } else {
            resetBall();
        }
    }

    public void incrementScore(int points) {
        this.score += points;
    }
    public void checkWinCondition() {
        List<Brick> currentBricks = isEvilMode ? map.getBricks() : bricks;
        if (currentBricks.stream().filter(Brick::isDestructible).allMatch(Brick::isDestroyed)) {
            isGameWon = true;
        }
    }
    public void setGameOver(boolean value) {
        this.isGameOver = value;
    }

    public EvilMap getEvilMap() {
        return map;
    }

    public boolean isEvilMode() {
        return isEvilMode;
    }
    public boolean isPaddleHasLaser() {
        return paddleHasLaser;
    }

    public void setPaddleHasLaser(boolean paddleHasLaser) {
        this.paddleHasLaser = paddleHasLaser;
    }

    public List<LaserBullet> getBullets() {
        return bullets;
    }

    public long getLaserEndTime() {
        return laserEndTime;
    }

    public void setLaserEndTime(long laserEndTime) {
        this.laserEndTime = laserEndTime;
    }

    public long getNextLaserFireTime() {
        return nextLaserFireTime;
    }

    public void setNextLaserFireTime(long nextLaserFireTime) {
        this.nextLaserFireTime = nextLaserFireTime;
    }

    public boolean isMoltenBallActive() {
        return moltenBallActive;
    }

    public void setMoltenBallActive(boolean moltenBallActive) {
        this.moltenBallActive = moltenBallActive;
    }

    public long getMoltenBallEndTime() {
        return moltenBallEndTime;
    }

    public void setMoltenBallEndTime(long moltenBallEndTime) {
        this.moltenBallEndTime = moltenBallEndTime;
    }

    public Boss getBoss() {
        return boss;
    }

    public List<Minion> getMinions() {
        return minions;
    }

    private void createBossProtectionBricks() {
        // Lấy vị trí và kích thước của boss
        double bossX = boss.getX();
        double bossY = boss.getY();
        double bossWidth = boss.getWidth();
        double bossHeight = boss.getHeight();

        // Kích thước mỗi viên gạch
        double bw = Constants.BRICK_WIDTH;
        double bh = Constants.BRICK_HEIGHT;
        double gap = 5; // khoảng cách giữa gạch và boss

        int bricksLeft = 0;

        this.bricks = new ArrayList<>();

        // --- TẠO HÀNG GẠCH TRÊN BOSS ---
        int colsTop = (int) Math.ceil(bossWidth / bw) + 1; // để phủ kín
        double startXTop = bossX - (bw / 2.0); // canh giữa
        double brickYTop = bossY - bh - gap;
        for (int i = 0; i < colsTop; i++) {
            double brickX = startXTop + i * (bw + 2);
            this.bricks.add(new StrongBrick(brickX, brickYTop));
            bricksLeft++;
        }

        // --- TẠO HÀNG GẠCH DƯỚI BOSS ---
        int colsBottom = colsTop;
        double startXBottom = startXTop;
        double brickYBottom = bossY + bossHeight + gap;
        for (int i = 0; i < colsBottom; i++) {
            double brickX = startXBottom + i * (bw + 2);
            this.bricks.add(new StrongBrick(brickX, brickYBottom));
            bricksLeft++;
        }

        // --- TẠO CỘT GẠCH BÊN TRÁI ---
        int rowsLeft = (int) Math.ceil(bossHeight / bh);
        double brickXLeft = bossX - bw - gap;
        double startYLeft = bossY;
        for (int i = 0; i < rowsLeft - 1; i++) {
            double brickY = startYLeft + i * (bh + 2);
            this.bricks.add(new StrongBrick(brickXLeft, brickY));
            bricksLeft++;
        }

        // --- TẠO CỘT GẠCH BÊN PHẢI ---
        int rowsRight = rowsLeft;
        double brickXRight = bossX + bossWidth + gap;
        double startYRight = bossY;
        for (int i = 0; i < rowsRight - 1; i++) {
            double brickY = startYRight + i * (bh + 2);
            this.bricks.add(new StrongBrick(brickXRight, brickY));
            bricksLeft++;
        }

        // Cập nhật thông tin đếm gạch bảo vệ
        this.totalBossProtectionBricks = bricksLeft;
        this.remainingBossProtectionBricks = bricksLeft;

        System.out.println("✅ Created " + totalBossProtectionBricks + " boss protection bricks surrounding the boss.");
    }


    public void decreaseRemainingBossProtectionBricks() {
        this.remainingBossProtectionBricks--;
        System.out.println("Remaining protection bricks: " + this.remainingBossProtectionBricks);
    }

    public int getRemainingBossProtectionBricks() {
        return remainingBossProtectionBricks;
    }
}