import com.almasb.fxgl.app.GameApplication;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.settings.GameSettings;
import com.mathias.luigi.LuigiFactory;
import com.mathias.luigi.LuigiType;
import com.mathias.luigi.PlayerControl;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.util.Map;

public class LuigiGame extends GameApplication {

    private Entity player;
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
                player.getComponent(PlayerControl.class).left();

            }
        }, KeyCode.LEFT);

        getInput().addAction(new UserAction("Right") {
            @Override
            protected void onAction() {
                player.getComponent(PlayerControl.class).right();

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
                    player.getComponent(PlayerControl.class).jump();
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



        player = getGameWorld().spawn("player", 50, 13*70);

        getGameWorld().spawn("animenemy", 580, 11*70);
        getGameWorld().spawn("animenemy", 300, 2*70);
        getGameWorld().spawn("animenemy", 1200, 8.5*70);

        //Image tempBackground = new Image("assets/textures/BGI.png",70*20,70*15,true,true);

        //getGameScene().setBackgroundRepeat(tempBackground);

        //Image image = new Image("assets/textures/BGI.png");

        //getGameScene().setBackgroundRepeat(image);




    }

    public int coinCounter = 0;

    @Override
    protected void initPhysics() {
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(LuigiType.PLAYER, LuigiType.COIN) {
            @Override
            protected void onCollisionBegin(Entity player, Entity coin) {
                getAudioPlayer().playSound("smw_coin.wav");
                coinCounter++;
                coin.removeFromWorld();
                System.out.println(coinCounter);
                getGameState().increment("coinsInTotal", 1);

            }
        });

        getPhysicsWorld().addCollisionHandler(new CollisionHandler(LuigiType.PLAYER, LuigiType.DOOR) {
            @Override
            protected void onCollisionBegin(Entity player, Entity door) {
                getAudioPlayer().playSound("smw_keyhole_exit.wav");
                getDisplay().showMessageBox("Level 1 Completed! \nYou collected: " + coinCounter + " out of 9 coins!", () -> {
                    System.out.println("Dialog closed");
                    getGameWorld().setLevelFromMap("luigi2.json");
                    getGameWorld().spawn("player", 50, 50);
                });
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
                startNewGame();
            }
        });

        getPhysicsWorld().addCollisionHandler(new CollisionHandler(LuigiType.PLAYER, LuigiType.ENEMY) {
            @Override
            protected void onCollisionBegin(Entity player, Entity enemy) {
                getAudioPlayer().playSound("smb_mariodie.wav");
                startNewGame();
                getDisplay().showMessageBox("You died to an enemy... \nTry again!");
                System.out.println("You died to an enemy");

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