package org.example;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.UserAction;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import java.util.Map;

import static com.almasb.fxgl.dsl.FXGL.*;

public class FTryGame extends GameApplication {

    private static final int PLATFORM_WIDTH = 50;
    private static final int PLATFORM_HEIGHT = 10;
    private static final int DOODLE_SIZE = 20;
    private static final int DOODLE_SPEED = 2;
    private static final int JUMP_HEIGHT = 5;

    private Entity platform;
    private Entity Doodle;

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
        platform = spawnPlatform(20, 400);
        Doodle = spawnBall(getAppWidth() / 2 - DOODLE_SIZE / 2, getAppHeight() / 2 - DOODLE_SIZE / 2);
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

//        if (Doodle.getX() == paddle1.getRightX()
//                && Doodle.getY() < paddle1.getBottomY()
//                && Doodle.getBottomY() > paddle1.getY()) {
//            Doodle.setProperty("velocity", new Point2D(-velocity.getX(), velocity.getY()));
//        }
//
//        if (Doodle.getRightX() == paddle2.getX()
//                && Doodle.getY() < paddle2.getBottomY()
//                && Doodle.getBottomY() > paddle2.getY()) {
//            Doodle.setProperty("velocity", new Point2D(-velocity.getX(), velocity.getY()));
//        }
        if (Doodle.getBottomY() >= platform.getY()
                && Doodle.getY() < platform.getY()
                && Doodle.getRightX() > platform.getX()
                && Doodle.getX() < platform.getRightX()) {
            Doodle.setProperty("velocity", new Point2D(0, -JUMP_HEIGHT));
        }

        if (Doodle.getX() <= -Doodle.getWidth()/2) {
            System.out.println("Left");
            Doodle.setX(getAppWidth() - Doodle.getWidth()/2-1);
        }

        if (Doodle.getRightX() >= getAppWidth() + Doodle.getWidth()/2) {
            System.out.println("Right");
            Doodle.setX(-Doodle.getWidth()/2+1);
        }

        if (Doodle.getY() <= 0) {
            Doodle.setY(0);
            Doodle.setProperty("velocity", new Point2D(0, -velocity.getY()));
        }

        if (Doodle.getBottomY() >= getAppHeight()) {
            Doodle.setY(getAppHeight() - DOODLE_SIZE);
            Doodle.setProperty("velocity", new Point2D(0, -JUMP_HEIGHT));
        }
    }



    private Entity spawnBall(double x, double y) {
        return entityBuilder()
                .at(x, y)
                .viewWithBBox(new Rectangle(DOODLE_SIZE, DOODLE_SIZE))
                .with("velocity", new Point2D(0, DOODLE_SPEED))
                .buildAndAttach();
    }

    private Entity spawnPlatform(double x, double y) {
        return entityBuilder()
                .at(x, y)
                .viewWithBBox(new Rectangle(PLATFORM_WIDTH, PLATFORM_HEIGHT, Color.LIGHTGREEN))
                .buildAndAttach();
    }


    public static void main(String[] args) {
        launch(args);
    }
}