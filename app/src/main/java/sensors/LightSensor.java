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
    private Context context;
    private OnLightListener lightListener;
    private int  ligthLevel;

    public interface OnLightListener
    {
        public void onLightChange(int ligthLevel);
    }

    public LightSensor( Context context,OnLightListener lightListener) {
        this.context = context;
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        ligthLevel=1;
        this.lightListener=lightListener;
        this.lightListener.onLightChange(ligthLevel);
        onStart();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor.getType()== Sensor.TYPE_LIGHT) {
            //retrieve the current value of the light sensor
            float currentValue = sensorEvent.values[0];
            int currentligthlevel=calculateLigthLevel(currentValue);
            if(ligthLevel != currentligthlevel){
                ligthLevel=currentligthlevel;
                this.lightListener.onLightChange(ligthLevel);
            }

        }
    }
    public int calculateLigthLevel(float sensorValue){
        if(sensorValue<=1000){
            return 1;
        }
        else if(sensorValue <= 2000){
            return 2;
        }
        else if(sensorValue <= 3000){
            return 3;
        }
        else if(sensorValue <= 4000){
            return 4;
        }
        else if(sensorValue <= 5000){
            return 6;
        }
        else if(sensorValue <= 6000){
            return 7;
        }
        return 8;
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