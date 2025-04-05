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

    private static final int PADDLE_WIDTH = 30;
    private static final int PADDLE_HEIGHT = 100;
    private static final int BALL_SIZE = 20;
    private static final int PADDLE_SPEED = 5;
    private static final int BALL_SPEED = 2;

    private Entity paddle1;
    private Entity paddle2;
    private Entity ball;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setTitle("Pong");
    }

    @Override
    protected void initInput() {
        getInput().addAction(new UserAction("Left") {
            @Override
            protected void onAction() {
                ball.translateX(-BALL_SPEED);
            }
        }, KeyCode.A);

        getInput().addAction(new UserAction("Right") {
            @Override
            protected void onAction() {
                ball.translateX(BALL_SPEED);
            }
        }, KeyCode.D);
//
//        getInput().addAction(new UserAction("Up 2") {
//            @Override
//            protected void onAction() {
//                paddle2.translateY(-PADDLE_SPEED);
//            }
//        }, KeyCode.UP);
//
//        getInput().addAction(new UserAction("Down 2") {
//            @Override
//            protected void onAction() {
//                paddle2.translateY(PADDLE_SPEED);
//            }
//        }, KeyCode.DOWN);
    }



    @Override
    protected void initGame() {

        ball = spawnBall(getAppWidth() / 2 - BALL_SIZE / 2, getAppHeight() / 2 - BALL_SIZE / 2);
    }

    @Override
    protected void initUI() {
        getGameScene().setCursor(Cursor.DEFAULT);
    }

    @Override
    protected void onUpdate(double tpf) {
        Point2D velocity = ball.getObject("velocity");
        ball.setProperty("velocity", new Point2D(0, velocity.getY() + .2));
        ball.translate(velocity);

//        if (ball.getX() == paddle1.getRightX()
//                && ball.getY() < paddle1.getBottomY()
//                && ball.getBottomY() > paddle1.getY()) {
//            ball.setProperty("velocity", new Point2D(-velocity.getX(), velocity.getY()));
//        }
//
//        if (ball.getRightX() == paddle2.getX()
//                && ball.getY() < paddle2.getBottomY()
//                && ball.getBottomY() > paddle2.getY()) {
//            ball.setProperty("velocity", new Point2D(-velocity.getX(), velocity.getY()));
//        }

        if (ball.getX() <= -ball.getWidth()/2) {
            System.out.println("Left");
            ball.setX(getAppWidth() - ball.getWidth()/2-1);
        }

        if (ball.getRightX() >= getAppWidth() + ball.getWidth()/2) {
            System.out.println("Right");
            ball.setX(-ball.getWidth()/2+1);
        }

        if (ball.getY() <= 0) {
            ball.setY(0);
            ball.setProperty("velocity", new Point2D(0, -velocity.getY()));
        }

        if (ball.getBottomY() >= getAppHeight()) {
            ball.setY(getAppHeight() - BALL_SIZE);
            ball.setProperty("velocity", new Point2D(0, -10));
        }
    }



    private Entity spawnBall(double x, double y) {
        return entityBuilder()
                .at(x, y)
                .viewWithBBox(new Rectangle(BALL_SIZE, BALL_SIZE))
                .with("velocity", new Point2D(0, BALL_SPEED))
                .buildAndAttach();
    }

    private void resetBall() {
        ball.setPosition(getAppWidth() / 2 - BALL_SIZE / 2, getAppHeight() / 2 - BALL_SIZE / 2);
        ball.setProperty("velocity", new Point2D(BALL_SPEED, BALL_SPEED));
    }

    public static void main(String[] args) {
        launch(args);
    }
}