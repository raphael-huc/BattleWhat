package helloandroid.ut3.battlewhat.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.Supplier;

import helloandroid.ut3.battlewhat.R;
import helloandroid.ut3.battlewhat.gameUtils.Score;
import helloandroid.ut3.battlewhat.object.ExplosionAnimation;
import sensors.AcceleroMeterSensor;
import sensors.LightSensor;
import sensors.OnLightChangeListener;
import sensors.OnShakeListener;
import helloandroid.ut3.battlewhat.object.Shot;
import helloandroid.ut3.battlewhat.object.spaceship.EnemySpaceShip;
import helloandroid.ut3.battlewhat.object.spaceship.PlayerSpaceShip;

public class GameActivity extends AppCompatActivity implements View.OnTouchListener, OnShakeListener, OnLightChangeListener {

    // PARAMETERS FOR THE GAMEPLAY
    private final int SHOOT_PLAYER_SPEED = 1000; // Define the speed of shooting for the player
    private final int SHOOT_ENEMY_SPEED = 1000; // Define the speed of shooting for the enemy
    private final int SHOOT_PLAYER_MOVE_SPEED = 10;
    private final int SHOOT_ENEMY_MOVE_SPEED = 10;
    private final int TIME_NEEDED_FOR_BONUS = 10000;
    private final int MAX_X_SHOOT_POSITION = 1000; // Define a maximum X position beyond which shots are considered to have gone too far


    private int lightLevel=1;
    private int shakePoints=0;
    private TextView shakePointsView;
    private TextView bonusMessage;
    private int counterTimeToGetBonus;
    private Supplier<Integer> updateBonusTimerFunctional= ()-> {return -1;};

    private Handler handler;
    private Context context;
    public ConstraintLayout gameView;
    private Chronometer timer;

    private Score score;
    private TextView scoreInput;
    private PlayerSpaceShip playerSpaceShip;
    private EnemySpaceShip enemySpaceShip;
    private ArrayList<Shot> enemyShots, playerShots;
    private ArrayList<ExplosionAnimation> explosions;

    private boolean isRunning;

    //for position of player
    private int playerPosition;

    //for position of enemy
    private int enemyPosition;

    private Handler mHandler;
    private int gameContent_height;
    private AcceleroMeterSensor acceleroMeterSensor;
    private LightSensor lightSensor;

    private boolean mvtEnemy = true;
    private boolean mvtPlayer = false;

    private int vitesseEnemy = 4;
    private int vitessePlayer = 0;

    private float lastY;
    private long startTimeSwipe;
    private static final int SWIPE_THRESHOLD = 100;

    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        handler = new Handler();
        playerShots = new ArrayList<>();
        enemyShots = new ArrayList<>();
        explosions = new ArrayList<>();

        setContentView(R.layout.activity_game);
        gameView = findViewById(R.id.Game);
        score = new Score();
        scoreInput = findViewById(R.id.textScore);

        playerSpaceShip = new PlayerSpaceShip(context, findViewById(R.id.player));
        enemySpaceShip = new EnemySpaceShip(context, findViewById(R.id.enemy));
        timer = findViewById(R.id.timer);

        // init the player shoot
        handler.postDelayed(mPlayerBulletControl, SHOOT_PLAYER_SPEED);
        handler.postDelayed(mEnemyBulletControl, SHOOT_ENEMY_SPEED);

        //get size of layout gameView
        ViewTreeObserver vto = gameView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    gameView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    gameView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                gameContent_height = gameView.getMeasuredHeight();
            }
        });

        playerPosition = gameContent_height;
        playerSpaceShip.setPositionY(1667);

        enemyPosition= gameContent_height;
        enemySpaceShip.setPositionY(1667);

        isRunning = false;
        shakePointsView = findViewById(R.id.shakePointsText);
        shakePointsView.setVisibility(View.INVISIBLE);
        bonusMessage=findViewById(R.id.bonusMessage);
        bonusMessage.setVisibility(View.INVISIBLE);
        counterTimeToGetBonus=0;

        // Start the game
        start();
        mHandler = new Handler();
        mHandler.postDelayed(mUpdate, 20);
        gameView.setOnTouchListener(this);
        this.sharedPref =this.getApplicationContext().
                getSharedPreferences("switchSensor",
                        Context.MODE_PRIVATE);
        if(sharedPref.getBoolean("mouvementSensor",true)) {
            acceleroMeterSensor = new AcceleroMeterSensor(this,this);

            updateBonusTimerFunctional = ()-> {
                this.counterTimeToGetBonus += 20;
                if (counterTimeToGetBonus >= TIME_NEEDED_FOR_BONUS) {
                    String baseBonusMessage = getResources().getString(R.string.bonus_points);
                    bonusMessage.setText(baseBonusMessage + "\n" + (this.counterTimeToGetBonus / 1000));
                    bonusMessage.setVisibility(View.VISIBLE);
                }
                return 1;
            };
        }
        if(sharedPref.getBoolean("lightSensor",true)) {
            lightSensor = new LightSensor(this, this);
        }
    }

    /**
     * Méthode qui se lance toutes les 20 ms permetant de mettre à jour les éléments
     */
    private final Runnable mUpdate = new Runnable() {
        @Override
        public void run() {
            updateEnemyPositionTime();
            updatePostitionShoot();
            checkCollisionShoot();
            updateExplosion();
            updateBonusTimerFunctional.get();
            handler.postDelayed(this, 20);
        }
    };

    private void checkCollisionShoot() {
        // Check collision player shoot
        playerShots.forEach(shoot -> {
            if (Rect.intersects(shoot.getCollisionShape(),
                    enemySpaceShip.getCollisionShape()) && !shoot.isHit) {
                shoot.isHit = true;
                gameView.removeView(shoot.getImageView());
                incrementGameScore(shakePoints>=40 ?2 : 1);
                ExplosionAnimation explosionAnimation = new ExplosionAnimation(context);
                explosions.add(explosionAnimation);
                explosionAnimation.makeAnimation(context, gameView, enemySpaceShip);
            }
        });
        enemyShots.forEach(shoot -> {
            if (Rect.intersects(shoot.getCollisionShape(),
                    playerSpaceShip.getCollisionShape()) && !shoot.isHit) {
                shoot.isHit = true;
                counterTimeToGetBonus =0;
                bonusMessage.setVisibility(View.INVISIBLE);
                gameView.removeView(shoot.getImageView());
                ExplosionAnimation explosionAnimation = new ExplosionAnimation(context);
                explosions.add(explosionAnimation);
                explosionAnimation.makeAnimation(context, gameView, playerSpaceShip);
            }
        });
    }

    private void updateExplosion() {
        for(int i = 0; i < explosions.size(); i++) {
            if(explosions.get(i).isFinish()) {
                System.out.println("Here !");
                gameView.removeView(explosions.get(i).getExplosionImageView());
                explosions.remove(explosions.get(i));
            }
        }
    }
    private final Runnable mPlayerBulletControl = new Runnable() {
        @Override
        public void run() {
            newPlayerBullet();
            // Prepare the next shoot
            handler.postDelayed(mPlayerBulletControl, lightLevel==0 || lightLevel==2 ? (long)
                    (SHOOT_PLAYER_SPEED * 0.7)
                    : SHOOT_PLAYER_SPEED);
        }
    };

    private final Runnable mEnemyBulletControl = new Runnable() {
        @Override
        public void run() {
            newEnemyBullet();
            // Prepare the next shoot
            handler.postDelayed(mEnemyBulletControl, SHOOT_ENEMY_SPEED);
        }
    };

    public void newPlayerBullet() {
        Shot shot = new Shot(context,
                (int) (playerSpaceShip.getPositionX() - playerSpaceShip.getWidth()/2),
                (int) playerSpaceShip.getPositionY() - playerSpaceShip.getHeight() / 4,
                 shakePoints >=40 ?R.drawable.shoot_god :R.drawable.shoot_blue);
        shot.getImageView().setScaleX(-1);
        gameView.addView(shot.getImageView());
        playerShots.add(shot);
    }

    public void newEnemyBullet() {
        Shot shot = new Shot(context,
                (int) enemySpaceShip.getPositionX(),
                (int) enemySpaceShip.getPositionY() - enemySpaceShip.getHeight() / 4,
                R.drawable.shoot_red);
        gameView.addView(shot.getImageView());
        enemyShots.add(shot);
    }

    public void updatePostitionShoot() {
        for (Iterator<Shot> iterator = playerShots.iterator(); iterator.hasNext();) {
            Shot shot = iterator.next();
            shot.setPositionX(shot.getPositionX() - SHOOT_PLAYER_MOVE_SPEED);

            if (shot.getPositionX() < -MAX_X_SHOOT_POSITION) {
                gameView.removeView(shot.getImageView());
                // If the shot has gone too far to the left, remove it from the list
                iterator.remove();
            }
        }

        for (Iterator<Shot> iterator = enemyShots.iterator(); iterator.hasNext();) {
            Shot shot = iterator.next();
            shot.setPositionX(shot.getPositionX() + SHOOT_ENEMY_MOVE_SPEED);

            if (shot.getPositionX() > MAX_X_SHOOT_POSITION) {
                gameView.removeView(shot.getImageView());
                // If the shot has gone too far to the right, remove it from the list
                iterator.remove();
            }
        }
    }

//    /**
//     * un Runnable qui sera appelé par le timer pour la gestion du mouvement du player
//     */
//    private void updatePlayerPositionTime() {
//        // mettre à jour la position du joueur
//        mHandler.postDelayed(this, 20);
//        updatePosition(vitessePlayer);
//    }

    // TODO Adapter le code par rapport à l'écran
    /**
     * un Runnable qui sera appelé par le timer pour la gestion du mouvement de l'enemy
     */
    private void updateEnemyPositionTime() {
            if (mvtEnemy){
                enemyPosition+=1;
                if(enemyPosition >= gameContent_height - 110){
                    mvtEnemy = false;
                }
            }
            if (!mvtEnemy) {
                enemyPosition-=1;
                if (enemyPosition <= 0) {
                    mvtEnemy = true;
                }
            }
            enemySpaceShip.setPositionY(enemyPosition);
    }

    /**
     * Add score the the player and refresh the TextView
     * @param val : value to add to the score.
     */
    public void incrementGameScore(int val) {
        score.addPoint(val);
        scoreInput.setText(score.toString());
    }

    /**
     * Start the game
     */
    public void start() {
        if(!isRunning || timer.isCountDown()) {
            timer.start();
            isRunning = true;
        }
    }

    /**
     * Stop the game
     */
    public void stop() {
        if(isRunning || !timer.isCountDown()) {
            timer.stop();
            isRunning = false;
        }
    }

    /**
     * Exit screen of game
     */
    public void exitGame(View view) {
        //Intent intent = new Intent(this, MainMenuActivity.class);
        stop();

        //enregistrer les valeurs
        //startActivity(intent);
        System.exit(RESULT_OK);
    }

    /**
     * When we touche screen, move player
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch(event.getAction()) {
            case (MotionEvent.ACTION_DOWN) :
                // Enregistre la position de départ du toucher
                lastY = event.getY();
                // Enregistre le temps de départ
                startTimeSwipe = SystemClock.elapsedRealtime();
                v.setTag(startTimeSwipe); // Stocke la valeur de startTime dans la propriété tag de la vue
                return true;
            case (MotionEvent.ACTION_MOVE) :
                // Calcule la distance parcourue depuis le toucher initial
                float deltaY = event.getY() - lastY;
                // Calcule la distance parcourue depuis le toucher initial
                //float deltaY = event.getY() - v.getY();
                if (deltaY > SWIPE_THRESHOLD) {
                    // Le swipe est vers le bas
                    // Ajoutez votre code ici pour traiter le swipe vers le bas
                    mvtPlayer = false;
                    return true; // Indique que l'événement a été traité
                } else if (deltaY < -SWIPE_THRESHOLD) {
                    // Le swipe est vers le haut
                    // Ajoutez votre code ici pour traiter le swipe vers le haut
                    mvtPlayer = true;
                    return true;
                }
            case (MotionEvent.ACTION_UP) :
                long startTime = (Long) v.getTag(); // Récupère la valeur de startTime à partir de la propriété tag de la vue
                long elapsedTime = SystemClock.elapsedRealtime() - startTime; // Calcule le temps écoulé
                startUpdatePlayerPositionTimeWithDuration(4, elapsedTime);
                return true;
            case (MotionEvent.ACTION_CANCEL) :
                return true;
            case (MotionEvent.ACTION_OUTSIDE) :
                return true;
            default :
                return super.onTouchEvent(event);
        }
    }

    /**
     * un Runnable qui sera appelé par le timer pour la gestion du mouvement du player
     */
    private Runnable mUpdatePlayerPositionTime = new Runnable() {
        public void run() {
            // mettre à jour la position du joueur
            mHandler.postDelayed(this, 20);
            updatePosition(vitessePlayer);
        }
    };

    // méthode pour démarrer l'exécution du Runnable initial avec une durée spécifiée
    private void startUpdatePlayerPositionTimeWithDuration(int speed, long duration) {
        vitessePlayer = speed; // modifier la valeur de vitessePlayer
        mHandler.post(mUpdatePlayerPositionTime);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                vitessePlayer = 0;
                stopUpdatePlayerPositionTime();
            }
        }, duration);
    }

    // méthode pour arrêter spécifiquement l'exécution du Runnable initial
    private void stopUpdatePlayerPositionTime() {
        mHandler.removeCallbacks(mUpdatePlayerPositionTime);
    }

    public void updatePosition(int vitessePlayer) {
        //go down
        if (!mvtPlayer){
            if(playerPosition < gameContent_height - 110){
                playerPosition+=vitessePlayer;
            }
        }
        //go up
        if (mvtPlayer){
            if(playerPosition >= 0){
                playerPosition-=vitessePlayer;
            }
        }
        playerSpaceShip.setPositionY(playerPosition);
    }

    @Override
    public void onShake() {

        if(counterTimeToGetBonus >TIME_NEEDED_FOR_BONUS){
            incrementGameScore((counterTimeToGetBonus /1000));
            counterTimeToGetBonus =0;
            bonusMessage.setVisibility(View.INVISIBLE);
        }
        if(shakePoints < 40){
            shakePoints++;
            String shakeTemplateText = getResources().getString(R.string.shake_points);
            shakePointsView.setText(this.shakePoints+shakeTemplateText);
            shakePointsView.setVisibility(View.VISIBLE);
        }
        if(shakePoints==40){
            TextView shakePointsView = findViewById(R.id.shakePointsText);
            shakePointsView.setVisibility(View.INVISIBLE);
            ImageView v= findViewById(R.id.player);
            v.setImageResource(R.drawable.godmode);
            playerSpaceShip.putGodMode();
            shakePoints=41;
        }


    }

    @Override
    public void onLightChange(int lightLevel) {
        String message ="";
        if(lightLevel==0){
            message="Low illumination => +Frequency Shot";
        }
        if(lightLevel==1){
            message="Normal illumination => Normal Frequency";
        }
        if(lightLevel==2){
            message="Great illumination => +Frequency Shot";
        }
        this.lightLevel=lightLevel;
        Toast.makeText(this.getApplicationContext(),message,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(acceleroMeterSensor!=null) {
            acceleroMeterSensor.onStop();
        }
        if(lightSensor!=null){
            lightSensor.onStop();
        }

    }


    @Override
    protected void onStart() {
        super.onStart();
        if(acceleroMeterSensor!=null) {
            acceleroMeterSensor.onStart();
        }
        if(lightSensor!=null){
            lightSensor.onStart();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(acceleroMeterSensor!=null) {
            acceleroMeterSensor.onResume();
        }
        if(lightSensor!=null){
            lightSensor.onResume();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(acceleroMeterSensor!=null) {
            acceleroMeterSensor.onPause();
        }
        if(lightSensor!=null){
            lightSensor.onPause();
        }
    }
}