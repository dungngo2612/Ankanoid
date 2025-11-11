package com.nhom12.arkanoid.model;

import com.nhom12.arkanoid.utils.Constants;
import com.nhom12.arkanoid.utils.ImageManager;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp đại diện cho Minion, các kẻ địch nhỏ hơn mà Boss có thể tạo ra.
 * Kế thừa từ lớp Enemy.
 */
public class Minion extends Enemy {
    private double verticalSpeed = 1.0;

    // Các thông số của sprite sheet cho Minion (Bạn cần thay đổi dựa trên sprite sheet của bạn!)
    private static final String MINION_SPRITE_SHEET_NAME = "minion"; // Tên file sprite sheet của minion
    private static final int MINION_FRAME_WIDTH = 64; // Chiều rộng của một frame minion
    private static final int MINION_FRAME_HEIGHT = 64; // Chiều cao của một frame minion
    private static final int MINION_NUM_FRAMES = 9; // Số lượng frame hoạt ảnh của minion

    /**
     * Phương thức private static để tải các frame CUNG CẤP cho constructor.
     * @return Một List<Image> chứa các frame của Minion.
     */
    private static List<Image> loadMinionFrames() {
        List<Image> minionFrames = new ArrayList<>();
        ImageManager imgMgr = ImageManager.getInstance();
        for (int i = 0; i < MINION_NUM_FRAMES; i++) {
            // Giả sử các frame nằm liên tiếp trên một hàng ngang
            Image frame = imgMgr.getSubImage(MINION_SPRITE_SHEET_NAME, i * MINION_FRAME_WIDTH, 0, MINION_FRAME_WIDTH, MINION_FRAME_HEIGHT);
            if (frame != null) {
                minionFrames.add(frame);
            }
        }
        return minionFrames;
    }

    public Minion(double x, double y, double width, double height, int initialHp) {
        // Gọi super() NGAY LẬP TỨC với kết quả từ phương thức static
        super(x, y, MINION_FRAME_WIDTH, MINION_FRAME_HEIGHT, initialHp, loadMinionFrames());

        if (this.sprites == null || this.sprites.isEmpty()) {
            this.width = 30; // Kích thước dự phòng
            this.height = 30;
            System.out.println("Không thể tải ảnh minion_sheet.png hoặc các frame! Sử dụng hình chữ nhật xanh thay thế.");
        }
    }

    @Override
    public void update() {
        // Cập nhật hoạt ảnh của Minion
        updateAnimation(); // Kế thừa từ Enemy

        // Logic di chuyển đơn giản: Minion di chuyển xuống phía dưới màn hình
        y += verticalSpeed;

        // Nếu Minion đi ra khỏi màn hình, nó sẽ bị loại bỏ
        if (y > Constants.SCENE_HEIGHT) { // Sử dụng SCENE_HEIGHT
            this.hp = 0; // Đặt HP về 0 để đánh dấu là đã bị tiêu diệt
        }
    }

    @Override
    public void draw(GraphicsContext gc) {
        // Vẽ Minion (sẽ tự động chọn frame hiện tại từ sprites list)
        if (sprites != null && !sprites.isEmpty()) {
            gc.drawImage(sprites.get(currentFrameIndex), x, y, width, height);
        } else {
            // Vẽ hình chữ nhật màu xanh lá cây nếu không có ảnh
            gc.setFill(Color.LIMEGREEN);
            gc.fillRect(x, y, width, height);
        }
    }
}