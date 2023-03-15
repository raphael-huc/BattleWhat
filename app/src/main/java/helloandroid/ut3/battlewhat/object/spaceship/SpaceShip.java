package helloandroid.ut3.battlewhat.object.spaceship;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;

public class SpaceShip {

    Context context;
    Bitmap spaceship;
    View spaceShipView;

    public SpaceShip(Context context, View spaceShipView) {
        this.context = context;
        this.spaceShipView = spaceShipView;
    }

    public float getPositionX() {
        return this.spaceShipView.getX();
    }

    public float getPositionY() {
        return this.spaceShipView.getY();
    }

    public void setPositionX(float x) {
        this.spaceShipView.setX(x);
    }

    public void setPositionY(float y) {
        this.spaceShipView.setY(y);
    }

    public Bitmap getSpaceship(){
        return spaceship;
    }

    public int getWidth(){
        return spaceship.getWidth();
    }

    public int getHeight(){
        return spaceship.getHeight();
    }
}
