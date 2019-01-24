package com.mathias.luigi;

import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import com.almasb.fxgl.time.LocalTimer;
import javafx.util.Duration;

public class EnemyControl extends Component {

     private PhysicsComponent physics;

     private AnimatedTexture texture;

     private AnimationChannel animenemy;

     private LocalTimer jumpTimer;

    public EnemyControl() {

        animenemy = new AnimationChannel("enemyjump.png", 4, 23, 32, Duration.seconds(1), 0, 3);


        texture = new AnimatedTexture(animenemy);

        texture.setScaleX(1.5);
        texture.setScaleY(1.5);

    }

    @Override
    public void onAdded() {
        entity.setView(texture);

        jumpTimer = FXGL.newLocalTimer();
        jumpTimer.capture();

    }

    @Override
    public void onUpdate(double tpf) {
        if (jumpTimer.elapsed(Duration.seconds(3))){
            jump();
            jumpTimer.capture();
        }

    }

    public void jump(){
        physics.setVelocityY(-300);

    }
}
