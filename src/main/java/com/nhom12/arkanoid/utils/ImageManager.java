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
        images.put("brick1", new Image("Image/Brick1.png"));
        images.put("paddle", new Image("Image/Paddles.png"));
        images.put("ball", new Image("Image/Ball.png"));
        images.put("extra_life", new Image("Image/Extra_Life.png"));
        images.put("multi_balls", new Image("Image/MultiBall.png"));
        images.put("paddle_shrink", new Image("Image/PaddleShrink.png"));
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
