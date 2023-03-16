package helloandroid.ut3.battlewhat.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import helloandroid.ut3.battlewhat.R;

public class HighscoreActivity extends AppCompatActivity {

    TextView tv_historical;

    private int lastScore;
    private String lastTime;
    private int lastNbPartie;

    private int bestScore1;
    private String bestTime1;
    private int bestNbPartie1;

    private int bestScore2;
    private String bestTime2;
    private int bestNbPartie2;

    private int bestScore3;
    private String bestTime3;
    private int bestNbPartie3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);

        tv_historical = (TextView) findViewById(R.id.tv_historical);

        SharedPreferences sharedPreferencesHistorical = getSharedPreferences("Historical", 0);

        lastScore = sharedPreferencesHistorical.getInt("lastScore", 0);
        lastTime = sharedPreferencesHistorical.getString("lastTime", "");
        lastNbPartie = sharedPreferencesHistorical.getInt("lastNbPartie", 0);

        bestScore1 = sharedPreferencesHistorical.getInt("bestScore1", 0);
        bestTime1 = sharedPreferencesHistorical.getString("bestTime1", "");
        bestNbPartie1 = sharedPreferencesHistorical.getInt("bestNbPartie1", 0);

        bestScore2 = sharedPreferencesHistorical.getInt("bestScore2", 0);
        bestTime2 = sharedPreferencesHistorical.getString("bestTime2", "");
        bestNbPartie2 = sharedPreferencesHistorical.getInt("bestNbPartie2", 0);

        bestScore3 = sharedPreferencesHistorical.getInt("bestScore3", 0);
        bestTime3 = sharedPreferencesHistorical.getString("bestTime3", "");
        bestNbPartie3 = sharedPreferencesHistorical.getInt("bestNbPartie3", 0);

        if(lastScore > bestScore3){

            bestScore3 = lastScore;
            bestTime3 = lastTime;
            bestNbPartie3 = lastNbPartie;

            SharedPreferences.Editor historicalEdit = sharedPreferencesHistorical.edit();
            historicalEdit.putInt("bestScore2", bestScore3);
            historicalEdit.putString("bestTime3", bestTime3);
            historicalEdit.putInt("bestNbPartie3", bestNbPartie3);
            historicalEdit.apply();
        }

        if(lastScore > bestScore2){
            int tempScore = bestScore2;
            String tempTime = bestTime2;
            int tempNbPartie = bestNbPartie2;

            bestScore2 = lastScore;
            bestTime2 = lastTime;
            bestNbPartie2 = lastNbPartie;

            bestScore3 = tempScore;
            bestTime3 = tempTime;
            bestNbPartie3 = tempNbPartie;

            SharedPreferences.Editor historicalEdit = sharedPreferencesHistorical.edit();
            historicalEdit.putInt("bestScore2", bestScore2);
            historicalEdit.putString("bestTime2", bestTime2);
            historicalEdit.putInt("bestNbPartie2", bestNbPartie2);

            historicalEdit.putInt("bestScore3", bestScore3);
            historicalEdit.putString("bestTime3", bestTime3);
            historicalEdit.putInt("bestNbPartie3", bestNbPartie3);
            historicalEdit.apply();
        }

        if(lastScore > bestScore1){
            int tempScore = bestScore1;
            String tempTime = bestTime1;
            int tempNbPartie = bestNbPartie1;

            bestScore1 = lastScore;
            bestTime1 = lastTime;
            bestNbPartie1 = lastNbPartie;

            bestScore2 = tempScore;
            bestTime2 = tempTime;
            bestNbPartie2 = tempNbPartie;

            SharedPreferences.Editor historicalEdit = sharedPreferencesHistorical.edit();
            historicalEdit.putInt("bestScore1", bestScore1);
            historicalEdit.putString("bestTime1", bestTime1);
            historicalEdit.putInt("bestNbPartie1", bestNbPartie1);

            historicalEdit.putInt("bestScore2", bestScore2);
            historicalEdit.putString("bestTime2", bestTime2);
            historicalEdit.putInt("bestNbPartie2", bestNbPartie2);
            historicalEdit.apply();
        }

        tv_historical.setText("Last Score: "+ lastScore +" time of partie: "+ lastTime +" partie numero: "+ lastNbPartie + "\n"+
                "best Score 1: "+ bestScore1 +" time of partie: "+ bestTime1 +" partie numero: "+ bestNbPartie1 + "\n"+
                "best Score 2: "+ bestScore2 +" time of partie: "+ bestTime2 +" partie numero: "+ bestNbPartie2 + "\n"+
                "best Score 3: "+ bestScore3 +" time of partie: "+ bestTime3 +" partie numero: "+ bestNbPartie3 + "\n"
        );
    }
}