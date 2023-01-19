package helloandroid.ut3.battlewhat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
    }

    public void startConnectHighscore(View view) {
//        Intent intent = new Intent(this, exemple.class);
//        startActivity(intent);
    }

    public void startConnectSettings(View view) {
    }

    public void startGame(View view) {
    }
}