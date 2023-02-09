package helloandroid.ut3.battlewhat.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Chronometer;
import android.widget.TextView;

import helloandroid.ut3.battlewhat.R;
import helloandroid.ut3.battlewhat.gameUtils.Score;

public class GameActivity extends AppCompatActivity {

    private Chronometer timer;
    private TextView scoreInput;
    private boolean isRunning;
    private Score score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        score = new Score();

        isRunning = false;
        timer = findViewById(R.id.timer);
        scoreInput = findViewById(R.id.textScore);
        start();
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
}