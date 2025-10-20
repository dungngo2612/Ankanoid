package com.nhom12.arkanoid.utils;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.net.URL;
import java.util.HashMap;

public class SoundManager {
    private static SoundManager instance;

    private MediaPlayer backgroundMusic;
    private HashMap<String, AudioClip> effects;

    private SoundManager() {
        effects = new HashMap<>();
        // Tải nhạc nền
        try {
            URL url = getClass().getResource("/Sound/background.m4a");
            backgroundMusic = new MediaPlayer(new Media(url.toExternalForm()));
            backgroundMusic.setCycleCount(MediaPlayer.INDEFINITE);
        } catch (Exception e) {
            System.err.println("Lỗi tải nhạc nền: " + e.getMessage());
            backgroundMusic = null;
        }

        loadSounds();
    }

    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    private void loadSounds() {

    }

    public void playBackgroundMusic() {
        if (backgroundMusic != null) {
            backgroundMusic.play();
        }
    }

    public void stopBackgroundMusic() {
        if (backgroundMusic != null) {
            backgroundMusic.stop();
        }
    }

    public void playEffect(String name) {
        AudioClip clip = effects.get(name);
        if (clip != null) {
            clip.play();
        }
    }
}

