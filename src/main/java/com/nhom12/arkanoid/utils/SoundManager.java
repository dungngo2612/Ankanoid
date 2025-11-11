package com.nhom12.arkanoid.utils;

import com.nhom12.arkanoid.controller.PauseMenuController;
import com.nhom12.arkanoid.controller.SettingsController;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;
import java.util.HashMap;
import java.util.prefs.Preferences;

public class SoundManager {
    private static SoundManager instance;

    private MediaPlayer backgroundMusic;
    private MediaPlayer playingMusic;
    private HashMap<String, AudioClip> effects;

    Preferences prefs = Preferences.userNodeForPackage(SettingsController.class);
    Preferences prefs1 = Preferences.userNodeForPackage(PauseMenuController.class);
    private SoundManager() {
        effects = new HashMap<>();
        effects.put("hit_brick", new AudioClip(getClass().getResource("/Sound/hitbrick.mp3").toExternalForm()));
        effects.put("button_click", new AudioClip(getClass().getResource("/Sound/buttonclick.mp3").toExternalForm()));
        effects.put("hit_paddle", new AudioClip(getClass().getResource("/Sound/hitpaddle.mp3").toExternalForm()));
        effects.put("ball_bounce", new AudioClip(getClass().getResource("/Sound/ballbounce.mp3").toExternalForm()));

        // Tải nhạc nền
        try {
            URL url = getClass().getResource("/Sound/background1.mp3");
            URL url1 = getClass().getResource("/Sound/level1.mp3");

            backgroundMusic = new MediaPlayer(new Media(url.toExternalForm()));
            playingMusic = new MediaPlayer(new Media(url1.toExternalForm()));
            backgroundMusic.setCycleCount(MediaPlayer.INDEFINITE);
            playingMusic.setCycleCount(MediaPlayer.INDEFINITE);
        } catch (Exception e) {
            e.printStackTrace();
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
        //Neu sfx tat thi ko play sound effects
        boolean play = prefs.getBoolean("sfxEnabled", true);
        boolean play1 = prefs1.getBoolean("sfxEnabled", true);
        if (!play || !play1) return;

        AudioClip clip = effects.get(name);
        if (clip != null) {
            clip.play();
        }
    }
}

