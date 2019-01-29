import com.almasb.fxgl.app.GameApplication;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.settings.GameSettings;
import com.mathias.luigi.EnemyWalkControl;
import com.mathias.luigi.LuigiFactory;
import com.mathias.luigi.LuigiType;
import com.mathias.luigi.PlayerControl;
import javafx.print.PageLayout;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.awt.*;
import java.util.Map;

public class LuigiGame extends GameApplication {

    private Entity Player;
    private Entity Walker;
    private Entity Enemy1;
    private Entity Enemy2;
    private Entity Enemy3;
    private int levelcomplete = 0;
    /*
    private ArrayList<String> levels = new ArrayList<>() {{
        add("luigi.json");
        add("luigi2.json");
    }};
    private int level = 0;
    private String getLevelAsString(int level) {
        if (level <= levels.size() && level >= 0) {
            this.level = level;
            return levels.get(level);
        }
        else{
            this.level= 0;
            return levels.get(0);
        }
    }
    private int getLevel(){
        return this.level;
    }
    private void setLevel(int level){
        this.level = level;
    }
    */

    @Override
    protected void initSettings(GameSettings gameSettings) {
        gameSettings.setWidth(20 * 70);
        gameSettings.setHeight(15 * 70);
        gameSettings.setTitle("LuigiFactory");

        //gameSettings.setMenuEnabled(true);

    }

    private boolean jumpActive = false;






    @Override
    protected void initInput() {
        getInput().addAction(new UserAction("Left") {
            @Override
            protected void onAction() {
                Player.getComponent(PlayerControl.class).left();

            }
        }, KeyCode.LEFT);

        getInput().addAction(new UserAction("Right") {
            @Override
            protected void onAction() {
                Player.getComponent(PlayerControl.class).right();

            }
        }, KeyCode.RIGHT);

        getInput().addAction(new UserAction("Jump") {
            @Override
            protected void onAction() {
            }

            @Override
            protected void onActionBegin() {
                super.onActionBegin();
                if(!isJumpActive()){
                    Player.getComponent(PlayerControl.class).jump();
                    getAudioPlayer().playSound("smw_jump.wav");
                    setJumpActive(true);

                }
            }

            @Override
            protected void onActionEnd() {
                super.onActionEnd();

            }
        }, KeyCode.UP);
    }

    // Here is where the game loads the map.
    @Override
    protected void initGame() {
        getGameWorld().addEntityFactory(new LuigiFactory());
        getGameWorld().setLevelFromMap("luigi.json");
        //getAudioPlayer().playSound("luigi_call_09.wav");



        Player = getGameWorld().spawn("player", 50, 13*70);

        Enemy1 = getGameWorld().spawn("animenemy", 580, 11*70);
        Enemy2 = getGameWorld().spawn("animenemy", 300, 2*70);
        Enemy3 = getGameWorld().spawn("animenemy", 1200, 8.5*70);

        //Image tempBackground = new Image("assets/textures/BGI.png",70*20,70*15,true,true);

        //getGameScene().setBackgroundRepeat(tempBackground);





    }

    public int coinCounter = 0;

    @Override
    protected void initPhysics() {
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(LuigiType.PLAYER, LuigiType.COIN) {
            @Override
            protected void onCollisionBegin(Entity player, Entity coin) {
                getAudioPlayer().playSound("smw_coin.wav");
                coinCounter++;
                getGameState().increment("coinsInTotal", +1);
                coin.removeFromWorld();

            }
        });

        getPhysicsWorld().addCollisionHandler(new CollisionHandler(LuigiType.PLAYER, LuigiType.PLATFORM) {
            @Override
            protected void onCollisionBegin(Entity player, Entity platform) {
                setJumpActive(false);
            }
        });

        getPhysicsWorld().addCollisionHandler(new CollisionHandler(LuigiType.PLAYER, LuigiType.DEATHPLATFORM) {
            @Override
            protected void onCollisionBegin(Entity player, Entity deathplatform) {
                getAudioPlayer().playSound("smb_mariodie.wav");
                Player.removeFromWorld();
                getDisplay().showMessageBox("You died to toxic ice... \nTry Again!");
                System.out.println("You died to toxic ice");
                if (levelcomplete == 1) {
                    Player = getGameWorld().spawn("player", 50, 50);
                }
                if (levelcomplete == 2) {
                    Player = getGameWorld().spawn("player", 720, 6 * 70);
                }
            }
        });

        getPhysicsWorld().addCollisionHandler(new CollisionHandler(LuigiType.WALKER, LuigiType.BOX) {
            @Override
            protected void onCollisionBegin(Entity walker, Entity box) {
                super.onCollisionBegin(walker, box);
                walker.getComponent(EnemyWalkControl.class).setCollision(true);
                walker.getComponent(EnemyWalkControl.class).onUpdate(tpf());
            }

            @Override
            protected void onCollision(Entity walker, Entity box) {
                super.onCollisionBegin(walker, box);
            }

            @Override
            protected void onCollisionEnd(Entity walker, Entity box) {
                super.onCollisionEnd(walker, box);

                walker.getComponent(EnemyWalkControl.class).setCollision(false);
            }
        });

        getPhysicsWorld().addCollisionHandler(new CollisionHandler(LuigiType.PLAYER, LuigiType.WALKER) {
            @Override
            protected void onCollisionBegin(Entity player, Entity walker) {
                getAudioPlayer().playSound("smb_mariodie.wav");
                Player.removeFromWorld();
                Walker.removeFromWorld();
                getDisplay().showMessageBox("You died to an enemy... \nTry again!");
                System.out.println("You died to an enemy");
                if (levelcomplete == 1) {
                    Player = getGameWorld().spawn("player", 50, 50);
                    Walker = getGameWorld().spawn("walker", 100, 8.5 * 70);
                }
                if (levelcomplete == 2) {
                    Player = getGameWorld().spawn("player", 720, 6 * 70);
                    Walker = getGameWorld().spawn("walker", 150, 3.5 * 70);
                }


            }
        });

        getPhysicsWorld().addCollisionHandler(new CollisionHandler(LuigiType.PLAYER, LuigiType.END) {
            @Override
            protected void onCollisionBegin(Entity player, Entity end) {
                getDisplay().showMessageBox("You have completed the game!", () -> {
                    System.out.println("Dialog closed");
                    exit();
                });
            }
        });

        getPhysicsWorld().addCollisionHandler(new CollisionHandler(LuigiType.PLAYER, LuigiType.ENEMY) {
            @Override
            protected void onCollisionBegin(Entity player, Entity enemy) {
                getAudioPlayer().playSound("smb_mariodie.wav");
                Player.removeFromWorld();
                Enemy1.removeFromWorld();
                getDisplay().showMessageBox("You died to an enemy... \nTry again!");
                System.out.println("You died to an enemy");
                if (levelcomplete == 1) {
                    Enemy2.removeFromWorld();
                    Player = getGameWorld().spawn("player", 50, 50);
                    Enemy1 = getGameWorld().spawn("animenemy", 550, 11 * 3);
                    Enemy2 = getGameWorld().spawn("animenemy", 1000, 6 * 70);
                }
                if (levelcomplete == 2) {
                    Enemy2.removeFromWorld();
                    Enemy3.removeFromWorld();
                    Enemy1 = getGameWorld().spawn("animenemy", 1270, 2 * 70);
                    Enemy2 = getGameWorld().spawn("animenemy", 500, 2 * 70);
                    Enemy3 = getGameWorld().spawn("animenemy", 1250, 8.5 * 70);
                    Player = getGameWorld().spawn("player", 720, 6 * 70);

                } else {
                    Enemy2.removeFromWorld();
                    Enemy3.removeFromWorld();
                    Enemy1 = getGameWorld().spawn("animenemy", 580, 11 * 70);
                    Enemy2 = getGameWorld().spawn("animenemy", 300, 2 * 70);
                    Enemy3 = getGameWorld().spawn("animenemy", 1200, 8.5 * 70);
                    Player = getGameWorld().spawn("player", 50, 13 * 70);
                }

            }
        });

        getPhysicsWorld().addCollisionHandler(new CollisionHandler(LuigiType.PLAYER, LuigiType.DOOR) {
            @Override
            protected void onCollisionBegin(Entity player, Entity door) {
                levelcomplete++;
                player.removeFromWorld();
                if (levelcomplete == 1) {
                        getGameWorld().setLevelFromMap("luigi2.json");
                        coinCounter = 0;
                        getGameState().increment("coinsInTotal", -9);
                        Player = getGameWorld().spawn("player", 50, 50);
                        Player.getComponent(PlayerControl.class);
                        Enemy1 = getGameWorld().spawn("animenemy", 550, 11 * 3);
                        Enemy2 = getGameWorld().spawn("animenemy", 1000, 6 * 70);
                        Walker = getGameWorld().spawn("walker", 100, 8.5 * 70);
                    } else if (levelcomplete == 2) {
                        getGameWorld().setLevelFromMap("luigi3.json");
                        coinCounter = 0;
                        getGameState().increment("coinsInTotal", -13);
                        Player = getGameWorld().spawn("player", 720, 6 * 70);
                        Player.getComponent(PlayerControl.class);
                        Enemy1 = getGameWorld().spawn("animenemy", 1270, 2 * 70);
                        Enemy2 = getGameWorld().spawn("animenemy", 500, 2 * 70);
                        Enemy3 = getGameWorld().spawn("animenemy", 1250, 8.5 * 70);
                        Walker = getGameWorld().spawn("walker", 150, 3.5 * 70);
                    }
                }
        });
    }



    @Override
    protected void initGameVars(Map<String, Object> vars) {
        super.initGameVars(vars);

        vars.put("coinsInTotal", 0);
    }

    @Override
    protected void initUI() {
        super.initUI();


        Image image = new Image("assets/textures/BG.png", 90*20, 90*15, false, false);

        getGameScene().setBackgroundRepeat(image);

        Text coins = getUIFactory().newText("Total coins:", Color.YELLOWGREEN, 18);
        coins.setTranslateX(30);
        coins.setTranslateY(30);

        Text coinsInTotal = getUIFactory().newText("", Color.YELLOWGREEN,18);
        coinsInTotal.setTranslateX(140);
        coinsInTotal.setTranslateY(30);
        coinsInTotal.textProperty().bind(getGameState().intProperty("coinsInTotal").asString());


        getGameScene().addUINodes(coins,coinsInTotal);


    }

    public boolean isJumpActive(){
        return jumpActive;
    }

    public void setJumpActive(boolean jumpActive){
        this.jumpActive = jumpActive;
    }


    public static void main(String[] args) {
        launch(args);
    }
}
