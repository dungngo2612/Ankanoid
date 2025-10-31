package com.nhom12.arkanoid.utils;

import com.nhom12.arkanoid.model.BrickFragment;
import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

public class ParticleManager {

    public static void spawnBrickFragments(double x, double y, Image color, Pane root) {
        List<BrickFragment> fragments = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            BrickFragment f = new BrickFragment(x, y, color);
            fragments.add(f);
            root.getChildren().add(f);
        }

        new AnimationTimer() {
            long start = System.currentTimeMillis();
            @Override
            public void handle(long now) {
                for (BrickFragment f : fragments) {
                    f.update();
                }
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