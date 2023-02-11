package sensors;

import android.content.Context;
import android.hardware.SensorManager;
import android.widget.TextView;

import helloandroid.ut3.battlewhat.R;

public class SensorsBattleWhat {
    private LightSensor lightSensor;
    private AcceleroMeterSensor acceleroMeterSensor;

    public SensorsBattleWhat( Context context) {
        lightSensor = new LightSensor(context);
        acceleroMeterSensor = new AcceleroMeterSensor(context);
    }
}
