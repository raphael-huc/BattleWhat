package sensors;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.TextView;

public class AcceleroMeterSensor implements SensorEventListener {

    //set instances for the sensorManager, light sensor, and textViews
    private SensorManager sensorManager;
    private Sensor acceleroMeterSensor;
    //private TextView acceleroMeterSensorText;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private final Context context;

    public AcceleroMeterSensor(Context context) {
       // this.acceleroMeterSensorText = acceleroMeterSensorText;
        this.context = context;
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        acceleroMeterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sharedPref = context.getSharedPreferences("sensors",Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        onStart();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor.getType()== Sensor.TYPE_ACCELEROMETER) {

            //get the current values of the accelerometer for each axis
            float current_xValue = sensorEvent.values[0];
            float current_yValue = sensorEvent.values[1];
            float current_zValue = sensorEvent.values[2];

            //display the retrieved values onto the textView
            editor.putFloat("acceleroSensorX", current_xValue);
            editor.putFloat("acceleroSensorY", current_yValue);
            editor.putFloat("acceleroSensorZ", current_zValue);
            editor.commit();
            String s = "x: " + sharedPref.getFloat("acceleroSensorX", Context.MODE_PRIVATE) + " "
                    + "y: " + sharedPref.getFloat("acceleroSensorY", Context.MODE_PRIVATE) + " "
                    + "z: " + sharedPref.getFloat("acceleroSensorZ", Context.MODE_PRIVATE) + " ";
            Log.i("AcceleroMeterSensorChange", s);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        //accelerometer does not report accuracy changes
    }

    //register the listener once the activity starts
    protected void onStart() {
        if(acceleroMeterSensor != null) {
            sensorManager.registerListener(this, acceleroMeterSensor, sensorManager.SENSOR_DELAY_FASTEST);
        }
        acceleroMeterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
    }

    //stop the sensor when the activity stops to reduce battery usage
    protected void onStop() {
        sensorManager.unregisterListener(this);
    }

    //resume the sensor when the activity stops to reduce battery usage
    protected void onResume() {
        sensorManager.registerListener(this, acceleroMeterSensor,
                SensorManager.SENSOR_DELAY_FASTEST);
    }

}