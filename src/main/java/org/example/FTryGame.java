package org.example;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.GameScene;
import com.almasb.fxgl.dsl.FXGL;
import javafx.scene.Cursor;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class FTryGame extends GameApplication {

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(300);
        settings.setHeight(500);
        settings.setTitle("");
    }

    @Override
    protected void initGame() {
        FXGL.entityBuilder()
                .at(150, 150)
                .view(new Rectangle(40, 40, Color.LIGHTBLUE))
                .buildAndAttach();
    }

    @Override
    protected void initUI() {
        GameScene gameScene = FXGL.getGameScene();
        gameScene.setCursor(Cursor.DEFAULT);
    }

    public static void main(String[] args) {
        launch(args);
    }
}