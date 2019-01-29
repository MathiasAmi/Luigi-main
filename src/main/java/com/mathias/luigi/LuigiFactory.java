package com.mathias.luigi;

import com.almasb.fxgl.entity.*;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class LuigiFactory implements EntityFactory {


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

    @Spawns("deathplatform")
    public Entity newDeathPlatform(SpawnData data) {
        return Entities.builder()
                .type(LuigiType.DEATHPLATFORM)
                .from(data)
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
                .with(new CollidableComponent(true))
                .with(new PhysicsComponent())
                .build();
    }

    @Spawns("box")
    public Entity newBox(SpawnData data) {
        return Entities.builder()
                .type(LuigiType.BOX)
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

    @Spawns("end")
    public Entity newEnd(SpawnData data) {
        return Entities.builder()
                .type(LuigiType.END)
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
                .with(new CollidableComponent(true))
                .with(physics)
                .with(new PlayerControl())
                .build();
    }

    @Spawns("walker")
    public Entity newWalker (SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);
        return Entities.builder()

                .type(LuigiType.WALKER)
                .from(data)
                .bbox(new HitBox(BoundingShape.box(25,40)))
                .with(new CollidableComponent(true))
                .with(new EnemyWalkControl())
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
                .with(new EnemyJumpControl())
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


