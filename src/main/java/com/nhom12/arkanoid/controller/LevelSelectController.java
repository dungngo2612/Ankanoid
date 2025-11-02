package com.nhom12.arkanoid.controller;

import com.nhom12.arkanoid.utils.ScreenManager;
import com.nhom12.arkanoid.utils.SoundManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.util.Duration;

import javax.swing.plaf.basic.BasicButtonUI;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class LevelSelectController implements Initializable {
    @FXML
    Button level1Button;
    @FXML
    Button level2Button;
    @FXML
    Button level3Button;
    @FXML
    Button level4Button;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Set tooltip for each button
        String level1Info = "\uD83D\uDD75\uFE0F\u200D♂\uFE0F Case: The Blackwood Estate\nThe Blackwood halls whisper secrets.\nWill you uncover the truth—or join the missing?";
        level1Button.setTooltip(new Tooltip(level1Info));

        String level2Info = "\uD83D\uDCA7 The Riverside Case\nA quiet riverside hides a dark secret.\nFollow the trail, gather clues, and uncover what really happened that night.";
        level2Button.setTooltip(new Tooltip(level2Info));

        String level3Info = "\uD83D\uDD73\uFE0F Secret Underground \nBeneath the city lies a hidden network of tunnels.\n Dangerous traps, coded locks, and whispers of forbidden experiments await.\n Only the brave will reach the truth.";
        level3Button.setTooltip(new Tooltip(level3Info));

        String level4Info = "\uD83D\uDD12 The Final Secret Case\nYou’ve solved every mystery… but one truth remains hidden.\nSomething really terrible hiding inside ?.....";
        level4Button.setTooltip(new Tooltip(level4Info));
    }

    @FXML
    private void onLevel1Clicked() {
        System.out.println("Level 1 (Easy) selected");
        // Lưu lại lựa chọn độ khó
        Preferences prefs = Preferences.userNodeForPackage(SettingsController.class);
        prefs.put("difficulty", "Easy");

        startGame();
    }

    // Hàm này sẽ được gọi bởi Level 2 Button
    @FXML
    private void onLevel2Clicked() {
        System.out.println("Level 2 (Medium) selected");
        Preferences prefs = Preferences.userNodeForPackage(SettingsController.class);
        prefs.put("difficulty", "Medium");

        startGame();
    }

    // Hàm này sẽ được gọi bởi Level 3 Button
    @FXML
    private void onLevel3Clicked() {
        System.out.println("Level 3 (Hard) selected");
        Preferences prefs = Preferences.userNodeForPackage(SettingsController.class);
        prefs.put("difficulty", "Hard");

        startGame();
    }

    // Logic bắt đầu game chung
    private void startGame() {
        SoundManager.getInstance().stopBackgroundMusic();

        // Kiểm tra xem nhạc có đang bật trong settings không
        Preferences prefs = Preferences.userNodeForPackage(SettingsController.class);
        boolean musicEnabled = prefs.getBoolean("musicEnabled", true);
        if (musicEnabled) {
            SoundManager.getInstance().playPlayingMusic();
        }

        ScreenManager.switchScene("/view/game.fxml", "Arkanoid");
    }

    // Quay lại Menu chính
    @FXML
    private void onBackClicked() {
        System.out.println("Returning to main menu...");
        ScreenManager.switchScene("/view/menu.fxml", "Arkanoid Menu");
    }

}