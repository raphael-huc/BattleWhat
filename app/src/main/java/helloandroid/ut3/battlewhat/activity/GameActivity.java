package helloandroid.ut3.battlewhat.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import helloandroid.ut3.battlewhat.R;
import helloandroid.ut3.battlewhat.gameUtils.Score;
import sensors.AcceleroMeterSensor;
import sensors.LightSensor;
import sensors.OnLightChangeListener;
import sensors.OnShakeListener;

public class GameActivity extends AppCompatActivity implements View.OnTouchListener, OnShakeListener, OnLightChangeListener {

    private Chronometer timer;
    private TextView scoreInput;
    private boolean isRunning;
    private Score score;

    //for position of player
    private int playerPosition;

    //for position of enemy
    private int enemyPosition;

    private View gameContent;

    private Handler mHandler;

    private View player;
    private View enemy;
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
    private SharedPreferences.Editor editor;
    private boolean lightSensorIsOn;
    private boolean mouvementSensorIsOn;


    /**
     * un Runnable qui sera appelé par le timer pour la gestion du mouvement de l'enemy
     */
    private Runnable mUpdateEnemyPositionTime = new Runnable() {
        public void run() {
            mHandler.postDelayed(this, 20);
            if (mvtEnemy){
                enemyPosition+=vitesseEnemy;
                if(enemyPosition >= gameContent_height - 110){
                    mvtEnemy = !mvtEnemy;
                }
            }
            if (!mvtEnemy) {
                enemyPosition-=vitesseEnemy;
                if (enemyPosition <= 0) {
                    mvtEnemy = !mvtEnemy;
                }
            }
            enemy.setY(enemyPosition);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        score = new Score();

        // remplacer tout le contenu de notre activité par le View
        gameContent = findViewById(R.id.Game);


        //get size of layaout gameContent
        ViewTreeObserver vto = gameContent.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    gameContent.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    gameContent.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                gameContent_height = gameContent.getMeasuredHeight();
            }
        });

        //mHandler = new Handler();
        //exécuter cette fonction au bout de time secondes
        //mHandler.postDelayed(mUpdatePlayerPositionTime, 1000);

        player = findViewById(R.id.player);
        playerPosition = gameContent_height;
        player.setY(1667);

        mHandler = new Handler();
        //exécuter cette fonction au bout de time secondes
        mHandler.postDelayed(mUpdateEnemyPositionTime, 1000);

        enemy = findViewById(R.id.enemy);
        enemyPosition= gameContent_height;
        enemy.setY(1667);

        isRunning = false;
        timer = findViewById(R.id.timer);
        scoreInput = findViewById(R.id.textScore);
        start();

        gameContent.setOnTouchListener(this);
        this.sharedPref =this.getApplicationContext().
                getSharedPreferences("switchSensor",
                        Context.MODE_PRIVATE);
        lightSensorIsOn=sharedPref.getBoolean("lightSensor",true);
        mouvementSensorIsOn=sharedPref.getBoolean("mouvementSensor",true);
        if(mouvementSensorIsOn) {
            acceleroMeterSensor = new AcceleroMeterSensor(this,this);
        }
        if(lightSensorIsOn) {
            lightSensor = new LightSensor(this, this);
        }
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
        player.setY(playerPosition);
    }

    @Override
    public void onShake() {
        incrementGameScore(1);
    }

    @Override
    public void onLightChange(int ligthLevel) {
        Toast.makeText(this.getApplicationContext(),"Hello the ligth level changed "+ ligthLevel,
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