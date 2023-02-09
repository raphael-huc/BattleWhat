package helloandroid.ut3.battlewhat.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Chronometer;
import android.widget.TextView;

import helloandroid.ut3.battlewhat.R;
import helloandroid.ut3.battlewhat.gameUtils.Score;

public class GameActivity extends AppCompatActivity implements View.OnTouchListener {

    private Chronometer timer;
    private TextView scoreInput;
    private boolean isRunning;
    private Score score;

    private int playerUp = 0;

    private View gameContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        score = new Score();

        isRunning = false;
        timer = findViewById(R.id.timer);
        scoreInput = findViewById(R.id.textScore);
        start();

        // remplacer tout le contenu de notre activité par le TextView
        //setContentView(tv);
        // on veut que notre objet soit averti lors d'un événement OnTouch sur
        // notre TextView :
        //R.layout.activity_game.setOnTouchListener(this);
        gameContent = findViewById(R.id.Game);
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

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        playerUp++;
        System.out.println("playerUp: " + playerUp + " Positions"+event.getX() + " : " + event.getY());
        return false;
    }
}