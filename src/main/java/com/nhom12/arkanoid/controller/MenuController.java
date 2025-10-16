package com.nhom12.arkanoid.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class MenuController implements Initializable {

    @FXML
    private MediaView mediaView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        URL mediaUrl = getClass().getResource("/test.mp4");
        Objects.requireNonNull(mediaUrl, "Video file not found!");

        Media media = new Media(mediaUrl.toExternalForm());
        MediaPlayer player = new MediaPlayer(media);
        player.setCycleCount(MediaPlayer.INDEFINITE);
        player.setOnEndOfMedia(() -> player.seek(Duration.ZERO));
        player.setAutoPlay(true);

        mediaView.setMediaPlayer(player);
    }
}
