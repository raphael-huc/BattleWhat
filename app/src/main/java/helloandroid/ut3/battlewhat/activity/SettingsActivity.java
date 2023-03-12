package helloandroid.ut3.battlewhat.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import helloandroid.ut3.battlewhat.R;

public class SettingsActivity extends AppCompatActivity {
    Switch lightsensorSwitch;
    Switch mouvementsensorSwitch;
    Button submit;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        submit = findViewById(R.id.submitButton);
        lightsensorSwitch=findViewById(R.id.lightsensorSwitch);
        mouvementsensorSwitch=findViewById(R.id.mouvementsensorSwitch);
        this.sharedPref =this.getApplicationContext().
                getSharedPreferences("switchSensor",
                        Context.MODE_PRIVATE);
        lightsensorSwitch.setChecked(sharedPref.getBoolean("lightSensor",true));
        mouvementsensorSwitch.setChecked(sharedPref.getBoolean("mouvementSensor",true));

        editor = sharedPref.edit();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putBoolean("lightSensor",lightsensorSwitch.isChecked());
                editor.putBoolean("mouvementSensor",mouvementsensorSwitch.isChecked());
                editor.commit();
                Toast.makeText(getApplicationContext(),"Changes Saved", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void startConnectMenu(View view) {
        Intent intent = new Intent(this, MainMenuActivity.class);
        startActivity(intent);
    }
}