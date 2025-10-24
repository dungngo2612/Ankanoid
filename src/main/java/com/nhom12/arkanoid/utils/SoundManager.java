package com.nhom12.arkanoid.utils;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;
import java.util.HashMap;

public class SoundManager {
    private static SoundManager instance;

    private MediaPlayer backgroundMusic;
    private MediaPlayer playingMusic;
    private HashMap<String, AudioClip> effects;

    private SoundManager() {
        effects = new HashMap<>();
        effects.put("hit_brick", new AudioClip(getClass().getResource("/Sound/hitbrick.mp3").toExternalForm()));
        effects.put("button_click", new AudioClip(getClass().getResource("/Sound/buttonclick.mp3").toExternalForm()));
        effects.put("hit_paddle", new AudioClip(getClass().getResource("/Sound/hitpaddle.mp3").toExternalForm()));
        effects.put("ball_bounce", new AudioClip(getClass().getResource("/Sound/ballbounce.mp3").toExternalForm()));

        // Tải nhạc nền
        try {
            URL url = getClass().getResource("/Sound/background.m4a");
            URL url1 = getClass().getResource("/Sound/detective.mp3");
            backgroundMusic = new MediaPlayer(new Media(url.toExternalForm()));
            playingMusic = new MediaPlayer(new Media(url1.toExternalForm()));
            backgroundMusic.setCycleCount(MediaPlayer.INDEFINITE);
            playingMusic.setCycleCount(MediaPlayer.INDEFINITE);
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

    public void playPlayingMusic() {
        if (playingMusic != null) {
            playingMusic.play();
        }
    }

    public void stopPlayingMusic() {
        if (playingMusic != null) {
            playingMusic.stop();
        }
    }

    public void playEffect(String name) {
        AudioClip clip = effects.get(name);
        if (clip != null) {
            clip.play();
        }
    }
}

