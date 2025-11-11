package com.nhom12.arkanoid.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class ScreenManager {

    private static Stage primaryStage;
    private static Object data;

    private static Scene mainScene;

    public static Object getData() {
        return data;
    }

    public static void setData(Object data) {
        ScreenManager.data = data;
    }

    // Initialize once in Main
    public static void setStage(Stage stage) {
        primaryStage = stage;
    }

    public static void switchScene(String fxmlPath, String title) {
        try {
            URL resource = ScreenManager.class.getResource(fxmlPath);
            if (resource == null) {
                throw new IOException("FXML file not found: " + fxmlPath);
            }

            Parent root = FXMLLoader.load(resource);
            if (mainScene == null) {
                // Nếu là lần chạy đầu tiên, tạo Scene mới
                mainScene = new Scene(root, Constants.SCENE_WIDTH, Constants.SCENE_HEIGHT);
                primaryStage.setScene(mainScene);
            } else {
                // Nếu Scene đã tồn tại, chỉ thay đổi nội dung (root) của nó
                mainScene.setRoot(root);
            }

            primaryStage.setTitle(title);
            primaryStage.show();

            // Yêu cầu focus vào root mới để nhận sự kiện bàn phím (quan trọng cho game)
            root.requestFocus();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
