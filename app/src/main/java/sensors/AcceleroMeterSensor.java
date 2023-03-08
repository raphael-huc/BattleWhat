package sensors;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class AcceleroMeterSensor implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor acceleroMeterSensor;
    private OnShakeListener mShakeListener;
    private final Context context;
    private float x,y,z,last_x,last_y,last_z;
    private boolean isFirstValue;
    private float shakeThreshold = 3f;

    public interface OnShakeListener
    {
        public void onShake();
    }

    public AcceleroMeterSensor(Context context,OnShakeListener mShakeListener) {
        this.context = context;
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        acceleroMeterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.mShakeListener = mShakeListener;
        onStart();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        x = event.values[0];
        y = event.values[1];
        z = event.values[2];
        if(isFirstValue) {
            float deltaX = Math.abs(last_x - x);
            float deltaY = Math.abs(last_y - y);
            float deltaZ = Math.abs(last_z - z);
            // If the values of acceleration have changed on at least two
            //axes, then we assume that we are in a shake motion
            if((deltaX > shakeThreshold && deltaY > shakeThreshold)
                    || (deltaX > shakeThreshold && deltaZ > shakeThreshold)
                    || (deltaY > shakeThreshold && deltaZ > shakeThreshold)) {
                mShakeListener.onShake();
            }
        }
        last_x = x;
        last_y = y;
        last_z = z;
        isFirstValue = true;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        //accelerometer does not report accuracy changes
    }

    //register the listener once the activity starts
    protected void onStart() {
        if(acceleroMeterSensor != null) {
            sensorManager.registerListener(this, acceleroMeterSensor, sensorManager.SENSOR_DELAY_NORMAL);
        }
        acceleroMeterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

    }

    //stop the sensor when the activity stops to reduce battery usage
    protected void onStop() {
        sensorManager.unregisterListener(this);
    }

    //resume the sensor when the activity stops to reduce battery usage
    protected void onResume() {
        sensorManager.registerListener(this, acceleroMeterSensor,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

}