package sensors;

public interface OnShakeListener {
    public void onShake();
}
/* To use the shake listener:
    1- acceleroMeterSensor = new AcceleroMeterSensor(this,this);
        1 param activity context
        2 a object that implement this interface
    2-put all your code in the onShake function...
 */
