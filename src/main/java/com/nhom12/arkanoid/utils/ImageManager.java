package com.nhom12.arkanoid.utils;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;

import javax.lang.model.type.NullType;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ImageManager {
    private static ImageManager instance;
    private HashMap<String, Image> images;
    private ImageManager() {
        images = new HashMap<>();
        images.put("default", new Image("Image/default.png"));
        images.put("background", new Image("Image/backgrounds.png"));
        images.put("normal_brick", new Image("Image/normal_brick.png"));
        images.put("paddle", new Image("Image/Paddles.png"));
        images.put("ball", new Image("Image/Ball.png"));
        images.put("extra_life", new Image("Image/Extra_Life.png"));
        images.put("multi_balls", new Image("Image/MultiBall.png"));
        images.put("molten_ball", new Image("Image/MoltenBall.png"));
        images.put("paddle_shrink", new Image("Image/PaddleShrink.png"));
        images.put("paddle_expand", new Image("Image/PaddleExpand.png"));
        images.put("laser_paddle", new Image("Image/LaserPaddle.png"));
        images.put("laser_bullet", new Image("Image/LaserBullet.png"));
        images.put("speed_up", new Image("Image/SpeedUp.png"));
        images.put("speed_down", new Image("Image/SpeedDown.png"));
        images.put("impassable", new Image("Image/Impassable_brick.png"));
        images.put("strong_brick1", new Image("Image/strong_brick1.png"));
        images.put("strong_brick2", new Image("Image/strong_brick2.png"));
        images.put("strong_brick3", new Image("Image/strong_brick3.png"));
        images.put("explosive_brick", new Image("Image/explosive.png"));

        images.put("boss", new Image("Image/boss.png"));
        images.put("minion", new Image("Image/bat_idle_fly.png"));
    }

    /**
     * Tải một phần (sub-image) từ một sprite sheet.
     * @param sheetName Tên file của sprite sheet (ví dụ: "boss_sheet.png").
     * @param x Vị trí X của hình ảnh con trên sprite sheet.
     * @param y Vị trí Y của hình ảnh con trên sprite sheet.
     * @param width Chiều rộng của hình ảnh con.
     * @param height Chiều cao của hình ảnh con.
     * @return Đối tượng Image của hình ảnh con đã được cắt.
     */
    public Image getSubImage(String sheetName, int x, int y, int width, int height) {
        Image sheet = showImage(sheetName);
        if (sheet == null) {
            System.err.println("Không tìm thấy sprite sheet: " + sheetName);
            return null;
        }

        int sheetWidth = (int) sheet.getWidth();
        int sheetHeight = (int) sheet.getHeight();

        // 🔒 Kiểm tra giới hạn để tránh lỗi arraycopy
        if (x < 0 || y < 0 || x + width > sheetWidth || y + height > sheetHeight) {
            System.err.printf(
                    "⚠️ Lỗi crop hình '%s': vượt giới hạn ảnh (sheet %dx%d, yêu cầu vùng x=%d, y=%d, w=%d, h=%d)%n",
                    sheetName, sheetWidth, sheetHeight, x, y, width, height
            );
            return getDefaultImage(); // hoặc null tùy bạn
        }

        javafx.scene.image.PixelReader reader = sheet.getPixelReader();
        javafx.scene.image.WritableImage subImage = new javafx.scene.image.WritableImage(reader, x, y, width, height);
        return subImage;
    }


    public static ImageManager getInstance() {
        if (instance == null) {
            instance = new ImageManager();
        }
        return instance;
    }

    public Image showImage(String name) {
        Image image = images.get(name);
        if (image == null) {
            System.out.println("Image not found: " + name);
            //Return default one if not found
            return getDefaultImage();
        }
        return image;
    }

    private Image getDefaultImage() {
        // Ví dụ: trả ảnh mặc định
        return images.get("default");
    }

}
