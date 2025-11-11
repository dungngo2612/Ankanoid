package com.nhom12.arkanoid.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

import java.util.List;

/**
 * Lớp cơ sở trừu tượng cho tất cả các loại kẻ địch (Boss, Minion, v.v.).
 * Định nghĩa các thuộc tính và hành vi chung.
 */
public abstract class Enemy {
    protected double x, y;
    protected double width, height;
    protected int hp;
    protected List<Image> sprites;
    protected int currentFrameIndex = 0; // Chỉ số của frame hiện tại
    protected double frameTimer = 0; // Đồng hồ đếm để chuyển frame
    protected final double FRAME_DURATION = 10; // Số tick game trước khi chuyển sang frame tiếp theo

    // Constructor mới, nhận vào một danh sách sprites
    public Enemy(double x, double y, double width, double height, int initialHp, List<Image> sprites) {
        this.x = x;
        this.y = y;
        this.hp = initialHp;
        this.sprites = sprites;

        // Nếu có sprites, sử dụng kích thước của frame đầu tiên
        if (sprites != null && !sprites.isEmpty()) {
            this.width = sprites.get(0).getWidth();
            this.height = sprites.get(0).getHeight();
        } else {
            // Sử dụng kích thước mặc định nếu không có sprite
            this.width = width;
            this.height = height;
        }
    }

    /**
     * Cập nhật logic của kẻ địch (di chuyển, tấn công, v.v.).
     * Đây là phương thức trừu tượng, mỗi lớp con sẽ tự định nghĩa.
     */
    public abstract void update();

    /**
     * Cập nhật hoạt ảnh của kẻ địch.
     * Phương thức này sẽ được gọi bởi update() của lớp con.
     */
    protected void updateAnimation() {
        if (sprites != null && sprites.size() > 1) { // Chỉ cập nhật nếu có nhiều hơn 1 frame
            frameTimer++;
            if (frameTimer >= FRAME_DURATION) {
                currentFrameIndex = (currentFrameIndex + 1) % sprites.size();
                frameTimer = 0;
            }
        }
    }

    /**
     * Vẽ kẻ địch lên màn hình.
     * @param gc GraphicsContext để vẽ.
     */
    public void draw(GraphicsContext gc) {
        if (sprites != null && !sprites.isEmpty()) {
            gc.drawImage(sprites.get(currentFrameIndex), x, y, width, height);
        } else {
            // Nếu không có sprite, vẽ một hình chữ nhật placeholder
            // Các lớp con có thể override để vẽ khác đi hoặc thêm thanh HP
        }
    }
    /**
     * Kẻ địch nhận sát thương.
     * @param damage Lượng sát thương nhận vào.
     */
    public void takeDamage(int damage) {
        this.hp -= damage;
        if (this.hp < 0) {
            this.hp = 0;
        }
        // Thêm hiệu ứng âm thanh/hình ảnh khi nhận sát thương tại đây
    }

    /**
     * Kiểm tra xem kẻ địch đã bị tiêu diệt chưa.
     * @return true nếu HP <= 0.
     */
    public boolean isDestroyed() {
        return this.hp <= 0;
    }

    /**
     * Lấy vùng giới hạn của kẻ địch để kiểm tra va chạm.
     * @return Đối tượng Rectangle đại diện cho vùng giới hạn.
     */
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    // Getters
    public double getX() { return x; }
    public double getY() { return y; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }
    public int getHp() { return hp; }

    // Setters (nếu cần)
    protected void setX(double x) { this.x = x; }
    protected void setY(double y) { this.y = y; }
    protected void setHp(int hp) { this.hp = hp; }
}