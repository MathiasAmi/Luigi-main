package com.mathias.luigi;

import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import com.almasb.fxgl.time.LocalTimer;
import javafx.util.Duration;

public class EnemyWalkControl extends Component {

    private int speed = 50;

    private  boolean collision = false;


    public EnemyWalkControl() {


    }





    @Override
    public void onAdded() {
       AnimationChannel animwalker = new AnimationChannel("enemywalker.png", 3, 24, 25, Duration.seconds(0.6), 0, 2);

       AnimatedTexture animwalkertexture = new AnimatedTexture(animwalker);

       animwalkertexture.loopAnimationChannel(animwalker);

       getEntity().setViewWithBBox(animwalkertexture);

       entity.translateX(-1);

       animwalkertexture.setScaleX(2);
       animwalkertexture.setScaleY(2);
    }

    @Override
    public void onUpdate(double tpf) {
        super.onUpdate(tpf);

        turnAround(tpf);


    }

    public void turnAround(double tpf){
        if (isCollision()== true){
            if (getSpeed()==50){
                getEntity().setScaleX(-1);
                setSpeed(-50);
            } else {
                getEntity().setScaleX(1);
                setSpeed(50);
            }
        }
        entity.translateX(speed*tpf);
    }

    public int getSpeed(){
        return speed;
    }

    public void setSpeed(int speed){
        this.speed = speed;
    }

    public boolean isCollision(){
        return collision;
    }

    public void setCollision(boolean collision){
        this.collision = collision;
    }
}
