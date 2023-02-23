package helloandroid.ut3.battlewhat.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Chronometer;
import android.widget.RelativeLayout;
import android.widget.TextView;

import helloandroid.ut3.battlewhat.R;
import helloandroid.ut3.battlewhat.gameUtils.Score;

public class GameActivity extends AppCompatActivity implements View.OnTouchListener {

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

    private boolean mvtEnemy = true;

    /**
     * un Runnable qui sera appelé par le timer pour la gestion du mouvement du player
     */    private Runnable mUpdatePlayerPositionTime = new Runnable() {
        public void run() {
            mHandler.postDelayed(this, 20);
            if(playerPosition < gameContent_height - 110){
                playerPosition+=2;
                player.setY(playerPosition);
            }
        }
    };

    /**
     * un Runnable qui sera appelé par le timer pour la gestion du mouvement de l'enemy
     */
    private Runnable mUpdateEnemyPositionTime = new Runnable() {
        public void run() {
            mHandler.postDelayed(this, 20);
            if (mvtEnemy){
                enemyPosition+=4;
                if(enemyPosition >= gameContent_height - 110){
                    mvtEnemy = !mvtEnemy;
                }
            }
            if (!mvtEnemy) {
                enemyPosition-=4;
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

        mHandler = new Handler();
        //exécuter cette fonction au bout de time secondes
        mHandler.postDelayed(mUpdatePlayerPositionTime, 1000);

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
     * When we touche screen, player go up
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (playerPosition > 0){
            playerPosition-=40;
            /*ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) player.getLayoutParams();
            params.topMargin = 100;
            player.setLayoutParams(params);*/
            player.setY(playerPosition);
            //System.out.println("playerPosition: " + playerPosition + " Positions"+event.getX() + " : " + event.getY()+" Position "+player.getY());
        }
        return false;
    }

}