package helloandroid.ut3.battlewhat.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import helloandroid.ut3.battlewhat.R;
import sensors.LightSensor;

public class MainMenuActivity extends AppCompatActivity {

    private TextView lightSensorText;
    private LightSensor l;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        SharedPreferences sharedPref = this.getSharedPreferences("sensors", Context.MODE_PRIVATE);
       /* SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("lightQ", 800);
        editor.commit();*/
        lightSensorText = findViewById(R.id.lightText);
        l = new LightSensor(lightSensorText,this);

    }

    public void startConnectHighscore(View view) {
        Intent intent = new Intent(this, HighscoreActivity.class);
        startActivity(intent);
    }

    public void startConnectSettings(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void startGame(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }
}