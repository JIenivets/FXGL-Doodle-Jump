package org.example;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.Viewport;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.UserAction;
import javafx.geometry.Point2D;
import javafx.scene.Camera;
import javafx.scene.Cursor;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.almasb.fxgl.dsl.FXGL.*;

public class FTryGame extends GameApplication {

    private static final int PLATFORM_WIDTH = 50;
    private static final int PLATFORM_HEIGHT = 10;
    private static final int PLATFORM_COUNT = 10;

    private static final int DOODLE_SIZE = 20;
    private static final int DOODLE_SPEED = 2;
    private static final int JUMP_HEIGHT = 5;

    public enum Types {
        DOODLE, PLATFORM
    }

    private Viewport viewport;
    private Entity Doodle;
    private List<Entity> Platforms = new ArrayList<>();

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(300);
        settings.setHeight(500);
        settings.setTitle("Doodle Jump");
        settings.setVersion(" ");
    }

    @Override
    protected void initInput() {
        getInput().addAction(new UserAction("Left") {
            @Override
            protected void onAction() {
                Doodle.translateX(-DOODLE_SPEED);
            }
        }, KeyCode.A);

        getInput().addAction(new UserAction("Right") {
            @Override
            protected void onAction() {
                Doodle.translateX(DOODLE_SPEED);
            }
        }, KeyCode.D);

    }

    @Override
    protected void initGame() {
        Random random = new Random();

        for (int i = 0; i < PLATFORM_COUNT; i++) {
            Platforms.add(spawnPlatform(random.nextInt(getAppWidth()-1+1)+1, random.nextInt(getAppHeight()-1+1)+1));
        }
        Doodle = spawnBall(getAppWidth() / 2 - DOODLE_SIZE / 2, getAppHeight() - DOODLE_SIZE);
//        getGameScene().getViewport().bindToEntity(Doodle, 0, getAppHeight() / 2);

        viewport = getGameScene().getViewport();
    }

    @Override
    protected void initUI() {
        getGameScene().setCursor(Cursor.DEFAULT);
    }

    @Override
    protected void onUpdate(double tpf) {
        Point2D velocity = Doodle.getObject("velocity");
        Doodle.setProperty("velocity", new Point2D(0, velocity.getY() + .1));
        Doodle.translate(velocity);

        // Вычисляем позицию по Y (центрируем игрока)
        double targetY = Doodle.getY() - getAppHeight()/2;
        double currentY = viewport.getY();
        viewport.setY(currentY+ (targetY - currentY) * 0.1);


        if (Doodle.getX() <= -Doodle.getWidth()/2) {
            Doodle.setX(getAppWidth() - Doodle.getWidth()/2-1);
        }

        if (Doodle.getRightX() >= getAppWidth() + Doodle.getWidth()/2) {
            Doodle.setX(-Doodle.getWidth()/2+1);
        }

        if (Doodle.getBottomY() >= getAppHeight()) {
            Doodle.setY(getAppHeight() - DOODLE_SIZE);
            Doodle.setProperty("velocity", new Point2D(0, -JUMP_HEIGHT));
        }

        for(Entity entity : Platforms){
            if (entity.getY() > viewport.getY()+viewport.getHeight()){
                entity.removeFromWorld();
            }
        }
    }

    @Override
    protected void initPhysics() {
        onCollision(Types.DOODLE, Types.PLATFORM, (doodle,platform) -> {
            if (Doodle.getBottomY() >= platform.getY()
                && Doodle.getY() < platform.getY()
                && Doodle.getRightX() > platform.getX()
                && Doodle.getX() < platform.getRightX()) {
            Doodle.setProperty("velocity", new Point2D(0, -JUMP_HEIGHT));

        }
        });
    }

    private Entity spawnBall(double x, double y) {
        return entityBuilder()
                .type(Types.DOODLE)
                .at(x, y)
                .viewWithBBox(new Rectangle(DOODLE_SIZE, DOODLE_SIZE))
                .with("velocity", new Point2D(0, DOODLE_SPEED))
                .collidable()
                .buildAndAttach();
    }

    private Entity spawnPlatform(double x, double y) {
        return entityBuilder()
                .type(Types.PLATFORM)
                .at(x, y)
                .viewWithBBox(new Rectangle(PLATFORM_WIDTH, PLATFORM_HEIGHT, Color.LIGHTGREEN))
                .collidable()
                .buildAndAttach();
    }


    public static void main(String[] args) {
        launch(args);
    }
}