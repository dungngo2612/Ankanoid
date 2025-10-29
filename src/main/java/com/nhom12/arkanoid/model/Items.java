package com.nhom12.arkanoid.model;

import com.nhom12.arkanoid.utils.Constants;
import java.util.Random;

public class Items {
    // Khai báo có 3 loại vật phẩm chính
    public enum ItemType {
        EXTRA_LIFE,      // Tăng 1 mạng
        SPEED_UP,        // Tăng tốc bóng
        SPEED_DOWN,
        PADDLE_EXPAND,    // Mở rộng thanh paddle
        PADDLE_SHRINK,   // Thu nhỏ paddle
        MULTI_BALL,      // Tạo thêm 2 quả bóng
        LASER_PADDLE     // Paddle có thể bắn laze
    }

    private double x;
    private double y;
    private double width;
    private double height;
    private double speedY;
    private ItemType type;
    private boolean active;

    public Items(double x, double y) {
        this.x = x;
        this.y = y;
        this.width = 50;
        this.height = 30;
        this.speedY =  3;
        this.active = true;

        // sẽ sử dụng hàm random để lựa chọn vật phẩm
        ItemType[] itemType = ItemType.values();
        Random rand = new Random();
        this.type = itemType[rand.nextInt(itemType.length)];
        //this.type = ItemType.SPEED_DOWN;
    }

    public void removeItem() {
        y += speedY;
        if(y > Constants.SCENE_HEIGHT) {
            active = false;
        }
    }

    public ItemType getType() {
        return type;
    }

    public boolean isActive(){
        return active;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public void deActivated() {
        this.active = false;
    }

    //kiểm tra va chạm với paddle
    public boolean intersects(Paddle p) {
        return this.x < p.getX() + p.getWidth()
                && this.x + this.width > p.getX()
                && this.y + this.height > p.getY()
                && this.y < p.getY() + p.getHeight();
    }


}
