package com.mathias.luigi;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.util.Duration;

public class PlayerControl extends Component {

    private PhysicsComponent physics;

    private AnimatedTexture texture;

    private AnimationChannel animidle, animwalk, animjump;

    //PlayerControl is adding animations and the scale of the player. Also sets speed and jump height.

    public PlayerControl(){

        animidle = new AnimationChannel("luigiwalk.png", 4, 19, 34, Duration.seconds(1), 2, 2);
        animwalk = new AnimationChannel("luigiwalk.png", 4, 19, 34, Duration.seconds(1), 0, 3);
        animjump = new AnimationChannel("luigihop.png", 1, 19, 34, Duration.seconds(1), 1, 1);

        texture = new AnimatedTexture(animidle);

        texture.setScaleX(1.5);
        texture.setScaleY(1.5);
    }

    @Override
    public void onAdded() {
        entity.setView(texture);
    }

    @Override
    public void onUpdate(double tpf) {

        if (physics.getVelocityX() > 0 || physics.getVelocityX() < 0) {
            if (texture.getAnimationChannel() == animidle) {
                texture.loopAnimationChannel(animwalk);
            }
            if (FXGLMath.abs(physics.getVelocityX()) < 130) {
                physics.setVelocityX(0);
                texture.loopAnimationChannel(animidle);
            }
        }

        //Does so my character cant move out the map. Get pushed back

    if(entity.getRightX() >=1400 ){
        physics.setVelocityX(-150);
    } if (entity.getX() <= 0){
        physics.setVelocityX(150);
        }

    if(entity.getHeight() >=1400){
        physics.setVelocityY(400);
    } if(entity.getY() <= 0){
        physics.setVelocityY(150);
        }

    }

    // setting movement for my character.

    public void left(){
        physics.setVelocityX(-150);
        entity.setScaleX(-1);

    }

    public void right(){
        physics.setVelocityX(150);
        entity.setScaleX(1);

    }

    public void jump(){
        physics.setVelocityY(-400);

    }
}
