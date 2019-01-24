package com.mathias.luigi;

import com.almasb.fxgl.entity.*;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class Luigi implements EntityFactory {


    //Spawns my platform from Tiled. getting the settings from Tiled aswell.
    @Spawns("platform")
    public Entity newPlatform(SpawnData data) {
        return Entities.builder()
                .type(LuigiType.PLATFORM)
                .from(data)
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
                .with(new CollidableComponent(true))
                .with(new PhysicsComponent())
                .build();
    }


    @Spawns("door")
    public Entity newDoor(SpawnData data) {
        return Entities.builder()
                .type(LuigiType.DOOR)
                .from(data)
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
                .with(new CollidableComponent(true))
                .build();
    }

    @Spawns("player")
    public Entity newPlayer(SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);

        return Entities.builder()
                .type(LuigiType.PLAYER)
                .from(data)
                .bbox(new HitBox(BoundingShape.box(25, 40)))
                .with(physics)
                .with(new CollidableComponent(true))
                .with(new PlayerControl())
                .build();
    }

    @Spawns("animenemy")
    public Entity newEnemy(SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);
        return Entities.builder()

                .type(LuigiType.ENEMY)
                .from(data)
                .bbox(new HitBox(BoundingShape.box(25,39)))
                .with(physics)
                .with(new CollidableComponent(true))
                .with(new EnemyControl())
                .build();

    }


        //Spawns the coins from Tiled.
        @Spawns("coin")
        public Entity newCoin (SpawnData data){
            return Entities.builder()
                    .type(LuigiType.COIN)
                    .from(data)
                    .viewFromNodeWithBBox(new Circle(data.<Integer>get("width") / 3, Color.GOLD))
                    .with(new CollidableComponent(true))
                    .build();
        }
    }


