package doodlejump;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.Viewport;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.UserAction;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.input.KeyCode;

import java.util.List;
import java.util.Random;

import static com.almasb.fxgl.dsl.FXGL.*;
import static com.almasb.fxgl.dsl.FXGL.getAppHeight;
import static doodlejump.Config.*;

public class Main extends GameApplication {

    private Viewport viewport;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(W_WIDTH);
        settings.setHeight(W_HEIGHT);
        settings.setTitle("Doodle Jump");
        settings.setVersion("0.1");
    }

    @Override
    protected void initUI() {
        getGameScene().setCursor(Cursor.DEFAULT);
    }

    @Override
    protected void initGame() {
        getGameWorld().addEntityFactory(new Factory());

        Random random = new Random();
        for (int i = 0; i < PLATFORM_COUNT; i++) {
            spawn("platform",random.nextInt(getAppWidth()-1+1)+1, random.nextInt(getAppHeight()-1+1)+1);
        }

        spawn("Doodle",getAppWidth() / 2 - DOODLE_WIDTH / 2, getAppHeight() - DOODLE_HEIGHT);
        viewport = getGameScene().getViewport();
    }

    @Override
    protected void initInput() {
        getInput().addAction(new UserAction("Left") {
            @Override
            protected void onAction() {
                getDoodle().translateX(-SPEED);
            }
        }, KeyCode.A);

        getInput().addAction(new UserAction("Right") {
            @Override
            protected void onAction() {
                getDoodle().translateX(SPEED);
            }
        }, KeyCode.D);

    }

    @Override
    protected void onUpdate(double tpf) {
        Entity Doodle = getDoodle();

        Point2D velocity = Doodle.getObject("velocity");
        Doodle.setProperty("velocity", new Point2D(0, velocity.getY() + GRAVITY));
        velocity = Doodle.getObject("velocity");
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
            Doodle.setY(getAppHeight() - DOODLE_HEIGHT);
            Doodle.setProperty("velocity", new Point2D(0, -JUMP_HEIGHT));
        }

        for(Entity entity : getPlatforms()){
            if (entity.getY() > viewport.getY()+viewport.getHeight()){
                entity.removeFromWorld();
            }
        }
    }

    @Override
    protected void initPhysics() {
        onCollision(EntityTypes.DOODLE, EntityTypes.PLATFORM, (Doodle, platform) -> {
            if (Doodle.getBottomY() >= platform.getY()
                    && Doodle.getY() < platform.getY()
                    && Doodle.getRightX() > platform.getX()
                    && Doodle.getX() < platform.getRightX()) {
                Doodle.setProperty("velocity", new Point2D(0, -JUMP_HEIGHT));

            }
        });
    }

    private static Entity getDoodle() {
        return getGameWorld().getSingleton(EntityTypes.DOODLE);
    }

    private static List<Entity> getPlatforms() {
        return getGameWorld().getEntitiesByType(EntityTypes.PLATFORM);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
