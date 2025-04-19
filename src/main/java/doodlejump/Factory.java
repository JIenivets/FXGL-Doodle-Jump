package doodlejump;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;
import static doodlejump.Config.*;

public class Factory implements EntityFactory {

    @Spawns("Doodle")
    public Entity spawnDoodle(SpawnData data) {
        return entityBuilder(data)
                .type(EntityTypes.DOODLE)
                .viewWithBBox(new Rectangle(DOODLE_WIDTH, DOODLE_HEIGHT, Color.LIGHTBLUE))
                .with("velocity", new Point2D(0, SPEED))
                .collidable()
                .build();
    }

    @Spawns("platform")
    public Entity spawnPlatform(SpawnData data) {
        return entityBuilder(data)
                .type(EntityTypes.PLATFORM)
                .viewWithBBox(new Rectangle(PLATFORM_WIDTH, PLATFORM_HEIGHT, Color.LIGHTGREEN))
                .collidable()
                .build();
    }
}
