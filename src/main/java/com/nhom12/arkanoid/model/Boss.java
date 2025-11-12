package com.nhom12.arkanoid.model;

import com.nhom12.arkanoid.utils.Constants;
import com.nhom12.arkanoid.utils.ImageManager;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Lớp đại diện cho Boss, kế thừa từ lớp Enemy.
 * Boss có thể di chuyển và tạo ra các Minion.
 */
public class Boss extends Enemy {
    private double speed = 2.0;
    private boolean movingRight = true;
    private List<Minion> minions;
    private double minionSpawnTimer = 0;
    private final double SPAWN_INTERVAL = 1000;
    private Random random;
    private boolean isEntering = true; // Trạng thái "đang đi vào"
    private double targetY = 50.0;     // Vị trí Y cuối cùng mong muốn (vị trí cũ)
    private double entrySpeed = 0.5;   // Tốc độ đi xuống

    private boolean canMove;
    // Các thông số của sprite sheet cho Boss (Bạn cần thay đổi dựa trên sprite sheet của bạn!)
    private static final String BOSS_SPRITE_SHEET_NAME = "boss"; // Tên file sprite sheet của boss
    private static final int BOSS_FRAME_WIDTH = 224; // Chiều rộng của một frame boss trên sprite sheet
    private static final int BOSS_FRAME_HEIGHT = 240; // Chiều cao của một frame boss trên sprite sheet
    private static final int BOSS_NUM_FRAMES = 15; // Số lượng frame hoạt ảnh của boss (ví dụ: 4 frame liên tiếp trên 1 hàng)

    /**
     * Phương thức private static để tải các frame CUNG CẤP cho constructor.
     * @return Một List<Image> chứa các frame của Boss.
     */
    private static List<Image> loadBossFrames() {
        List<Image> bossFrames = new ArrayList<>();
        ImageManager imgMgr = ImageManager.getInstance();
        Image sheet = imgMgr.showImage(BOSS_SPRITE_SHEET_NAME);

        int sheetWidth = (int) sheet.getWidth();
        int maxFrames = sheetWidth / BOSS_FRAME_WIDTH; // số frame có thể cắt được

        for (int i = 0; i < maxFrames; i++) {
            Image frame = imgMgr.getSubImage(BOSS_SPRITE_SHEET_NAME,
                    i * BOSS_FRAME_WIDTH, 0,
                    BOSS_FRAME_WIDTH, BOSS_FRAME_HEIGHT);
            if (frame != null) {
                bossFrames.add(frame);
            }
        }

        System.out.println("✅ Boss frames loaded: " + bossFrames.size());
        return bossFrames;
    }


    public Boss(double x, double y, int initialHp) {
        // Gọi super() NGAY LẬP TỨC với kết quả từ phương thức static
        super(x, y, BOSS_FRAME_WIDTH, BOSS_FRAME_HEIGHT, initialHp, loadBossFrames());

        // Nếu không tải được ảnh, đặt kích thước dự phòng
        if (this.sprites == null || this.sprites.isEmpty()) {
            this.width = 150;
            this.height = 80;
            System.out.println("Không thể tải ảnh boss_sheet.png hoặc các frame! Sử dụng hình chữ nhật đỏ thay thế.");
        }

        this.minions = new ArrayList<>();
        this.random = new Random();
        this.canMove = false;
    }


    @Override
    public void update() {
        if (isEntering) {
            y += entrySpeed; // Di chuyển boss xuống
            if (y >= targetY) {
                y = targetY; // Khớp vào vị trí cuối cùng
                isEntering = false; // Kết thúc trạng thái đi vào
            }
            // Không chạy logic update animation hoặc di chuyển ngang
            // Bạn có thể cho nó chạy updateAnimation() nếu muốn boss vẫy cánh khi đi vào
            // updateAnimation();
            return; // Dừng update tại đây cho đến khi vào vị trí
        }
        // Cập nhật hoạt ảnh của Boss
        updateAnimation(); // Kế thừa từ Enemy

        // AI di chuyển: Boss di chuyển qua lại trên đầu màn hình
        if (canMove) {
            if (movingRight) {
                x += speed;
                if (x + width > Constants.SCENE_WIDTH) { // Sử dụng SCENE_WIDTH
                    movingRight = false;
                    x = Constants.SCENE_WIDTH - width;
                }
            } else {
                x -= speed;
                if (x < 0) {
                    movingRight = true;
                    x = 0;
                }
            }
        }

        // Cập nhật Minion
        minions.removeIf(Minion::isDestroyed);
        for (Minion minion : minions) {
            minion.update();
        }

        // Logic tạo Minion
        minionSpawnTimer++;
        if (minionSpawnTimer >= SPAWN_INTERVAL && hp > 0) {
            spawnMinion();
            minionSpawnTimer = 0;
        }
    }

    @Override
    public void draw(GraphicsContext gc) {
        // Vẽ Boss (sẽ tự động chọn frame hiện tại từ sprites list)
        super.draw(gc); // Gọi phương thức draw của lớp cha để vẽ sprite

        // Vẽ thanh HP cho boss
        drawHealthBar(gc);

        // Vẽ tất cả Minion
        for (Minion minion : minions) {
            minion.draw(gc);
        }
    }

    /**
     * Vẽ thanh HP phía trên Boss
     */
    private void drawHealthBar(GraphicsContext gc) {
        double barWidth = Constants.SCENE_WIDTH;
        double barHeight = 10;

        // Vẽ nền thanh HP (màu xám)
        gc.setFill(Color.GRAY);
        gc.fillRect(0, 0, barWidth, barHeight);

        // Giả sử HP tối đa của boss là 100 để hiển thị thanh HP.
        // Bạn có thể thay đổi giá trị này hoặc truyền HP tối đa vào constructor.
        double currentHpWidth = (double)hp / 100 * barWidth;
        gc.setFill(Color.RED);
        gc.fillRect(0, 0, currentHpWidth, barHeight);
    }

    /**
     * Tạo ra một Minion mới.
     * Tương lai có thể thêm các loại Minion khác nhau.
     */
    private void spawnMinion() {
        // Lấy kích thước thật của Minion từ file Minion.java
        // (Trong file Minion.java, MINION_FRAME_WIDTH là 64, MINION_FRAME_HEIGHT là 64)
        double minionWidth = 64;
        double minionHeight = 64;

        // Tạo Minion ở vị trí ngẫu nhiên trên màn hình
        // Ngẫu nhiên X, đảm bảo không bị kẹt ở mép
        double spawnX = random.nextDouble() * (Constants.SCENE_WIDTH - minionWidth);

        // Ngẫu nhiên Y, ví dụ: ở nửa trên màn hình (từ 0 đến 300)
        // (Bạn có thể dùng Constants.SCENE_HEIGHT nếu muốn nó xuất hiện bất cứ đâu)
        double spawnY = random.nextDouble() * (Constants.SCENE_HEIGHT / 2);

        // Gọi constructor của Minion.
        // Các tham số 30, 30 này dường như bị ghi đè bởi constructor của Minion,
        // nhưng chúng ta cứ truyền vào cho đúng chữ ký
        minions.add(new Minion(spawnX, spawnY, 30, 30, 10));
        System.out.println("Boss đã tạo ra một Minion tại vị trí ngẫu nhiên!");
    }

    public List<Minion> getMinions() {
        return minions;
    }

    public boolean canMove() {
        return canMove;
    }

    public void setCanMove(boolean canMove) {
        this.canMove = canMove;
    }

    public boolean isEntering() {
        return isEntering;
    }
}