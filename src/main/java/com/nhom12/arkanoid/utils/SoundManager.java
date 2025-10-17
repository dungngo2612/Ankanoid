package com.nhom12.arkanoid.utils;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.util.HashMap;

public class SoundManager {
    private static SoundManager instance;

    private MediaPlayer backgroundMusic;
    private HashMap<String, AudioClip> effects;

    private SoundManager() {
        effects = new HashMap<>();
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
        backgroundMusic.play();
    }

    public void stopBackgroundMusic() {
        backgroundMusic.stop();
    }

    public void playEffect(String name) {
        AudioClip clip = effects.get(name);
        if (clip != null) {
            clip.play();
        }
    }
}

