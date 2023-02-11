package sensors;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.TextView;

import java.util.List;

import helloandroid.ut3.battlewhat.R;

public class LightSensor implements SensorEventListener {

    //set instances for the sensorManager, light sensor, and textViews
    private SensorManager sensorManager;
    private Sensor lightSensor;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private Context context;

    public LightSensor( Context context) {
        this.context = context;
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        sharedPref = context.getSharedPreferences("sensors",Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        onStart();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor.getType()== Sensor.TYPE_LIGHT) {
            //retrieve the current value of the light sensor
            float currentValue = sensorEvent.values[0];
            //display the retrieved values onto the textView
            editor.putFloat("lightSensor", currentValue);
            editor.commit();
            Log.i("LightSensorChange", sharedPref.getFloat
                    ("lightSensor", Context.MODE_PRIVATE) + "");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        //ambient light sensor does not report accuracy changes
    }

    //register the listener once the activity starts
    protected void onStart() {
        if(lightSensor != null) {
            sensorManager.registerListener(this, lightSensor, sensorManager.SENSOR_DELAY_FASTEST);
        }
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
    }
    //stop the sensor when the activity stops to reduce battery usage
    protected void onStop() {
        sensorManager.unregisterListener(this);
    }

    //resume the sensor when the activity stops to reduce battery usage
    protected void onResume() {
        sensorManager.registerListener(this, lightSensor,
                SensorManager.SENSOR_DELAY_GAME);
    }

}