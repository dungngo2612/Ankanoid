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
        String level1Info = "Unleash a wave of fiery sword energy that pierces through obstacles. The ball gains explosive power, burning through blocks and enemies alike.\n" +
                "“The blade’s flame cuts through fate itself.”";
        level1Button.setTooltip(new Tooltip(level1Info));

        String level2Info = "A lotus of pure chi blooms, restoring harmony to the battlefield. Slows time briefly and regenerates your shield’s strength.\n" +
                "“From stillness comes renewal; from renewal, strength.”";
        level2Button.setTooltip(new Tooltip(level2Info));

        String level3Info = "Summon a protective force field infused with arcane energy. Deflects the next few hits and stabilizes the ball’s trajectory.\n" +
                "“The heavens guard those who command the elements.”";
        level3Button.setTooltip(new Tooltip(level3Info));

        String level4Info = "Awaken forbidden power from the underworld. The ball absorbs dark chi, doubling its speed and damage for a short time.\n" +
                "“To wield the abyss, one must first embrace it.”";
        level4Button.setTooltip(new Tooltip(level4Info));
    }

    @FXML
    private void onLevel1Clicked() {
        System.out.println("Level 1 (Easy) selected");
        // Lưu lại lựa chọn độ khó
        Preferences prefs = Preferences.userNodeForPackage(SettingsController.class);
        prefs.put("difficulty", "Easy");
        prefs.putBoolean("evilMode", false); // Tắt Evil Mode khi chọn Challenge

        startGame();
    }

    // Hàm này sẽ được gọi bởi Level 2 Button
    @FXML
    private void onLevel2Clicked() {
        System.out.println("Level 2 (Medium) selected");
        Preferences prefs = Preferences.userNodeForPackage(SettingsController.class);
        prefs.put("difficulty", "Medium");
        prefs.putBoolean("evilMode", false); // Tắt Evil Mode khi chọn Challenge

        startGame();
    }

    // Hàm này sẽ được gọi bởi Level 3 Button
    @FXML
    private void onLevel3Clicked() {
        System.out.println("Level 3 (Hard) selected");
        Preferences prefs = Preferences.userNodeForPackage(SettingsController.class);
        prefs.put("difficulty", "Hard");
        prefs.putBoolean("evilMode", false); // Tắt Evil Mode khi chọn Challenge

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