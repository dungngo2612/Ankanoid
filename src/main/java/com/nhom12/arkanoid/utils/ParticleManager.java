package com.nhom12.arkanoid.utils;

import com.nhom12.arkanoid.model.BrickFragment;
import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

public class ParticleManager {

    public static void spawnBrickFragments(double x, double y, Image brickImage, Pane root) {
        // danh sách chứa các mảnh vỡ
        List<BrickFragment> fragments = new ArrayList<>();
        for (int i = 0; i < 40; i++) {
            BrickFragment f = new BrickFragment(x, y, brickImage);
            fragments.add(f);
            root.getChildren().add(f);
        }

        //tạo vòng lặp animation chạy fra
        new AnimationTimer() {
            long start = System.currentTimeMillis();

            // câp nhật vị trí từng mảnh
            @Override
            public void handle(long now) {
                for (BrickFragment f : fragments) {
                    f.update();
                }
                // sau 0.5s là xoá từng mảnh
                if (System.currentTimeMillis() - start > 500) {
                    for (BrickFragment f : fragments) {
                        root.getChildren().remove(f);
                    }
                    stop();
                }
            }
        }.start();
    }
}