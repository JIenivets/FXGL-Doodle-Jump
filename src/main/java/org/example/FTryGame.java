package org.example;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.GameScene;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import javafx.scene.Cursor;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static com.almasb.fxgl.dsl.FXGL.*;
import static org.example.Config.*;

public class FTryGame extends GameApplication {
    private Entity Player, Platform;
    private PhysicsComponent physics;
    private boolean isJumping = false; // Флаг для отслеживания прыжка

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(300);
        settings.setHeight(500);
        settings.setTitle(" ");
        settings.setVersion(" ");
    }

    @Override
    protected void initGame() {
        Platform = entityBuilder()
                .at(0, 490)
                .view(new Rectangle(300, 10, Color.LIGHTGREEN))
                .buildAndAttach();

        Player = entityBuilder()
                .at(150, 150)
                .view(new Rectangle(PLAYER_SIZE, PLAYER_SIZE, Color.LIGHTBLUE))
                .with(new PhysicsComponent())
                .buildAndAttach();

        // Устанавливаем гравитацию для физического мира
        getPhysicsWorld().setGravity(0, 500); // Устанавливаем гравитацию

        physics = Player.getComponent(PhysicsComponent.class);
        physics.setBodyType(BodyType.DYNAMIC); // Устанавливаем тип тела как динамический
    }

    @Override
    protected void initInput() {
        onKey(KeyCode.D, () -> {
            physics.setVelocityX(PLAYER_SPEED); // Двигаем вправо
        });

        onKey(KeyCode.A, () -> {
            physics.setVelocityX(-PLAYER_SPEED); // Двигаем влево
        });

        // Обработчик отпускания клавиш
        onKeyUp(KeyCode.D, () -> {
            if (physics.getVelocityX() > 0) {
                physics.setVelocityX(0); // Останавливаем движение вправо
            }
        });

        onKeyUp(KeyCode.A, () -> {
            if (physics.getVelocityX() < 0) {
                physics.setVelocityX(0); // Останавливаем движение влево
            }
        });

        onKey(KeyCode.W, () -> {
            if (!isJumping) { // Проверяем, не прыгает ли игрок
                physics.setVelocityY(-JUMP_FORCE); // Устанавливаем вертикальную скорость для прыжка
                isJumping = true; // Устанавливаем флаг прыжка
            }
        });
    }

    @Override
    protected void initUI() {
        GameScene gameScene = getGameScene();
        gameScene.setCursor(Cursor.DEFAULT);
    }

    @Override
    protected void onUpdate(double tpf) {
        // Проверяем, приземлился ли игрок
        if (Player.getY() >= getAppHeight() - PLAYER_SIZE) {
            Player.setY(getAppHeight() - PLAYER_SIZE);
            isJumping = false; // Сбрасываем флаг прыжка
        }

        // Ограничиваем движение игрока по вертикали
        if (Player.getY() <= 0) {
            Player.setY(0);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}