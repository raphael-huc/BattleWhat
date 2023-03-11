package helloandroid.ut3.battlewhat.object.spaceship;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;

import helloandroid.ut3.battlewhat.R;
import helloandroid.ut3.battlewhat.activity.GameActivity;
import helloandroid.ut3.battlewhat.object.Shot;

public class PlayerSpaceShip {
    Context context;
    Bitmap spaceship;
    View spaceShipView;
    Random random;

    public ArrayList<Shot> playerShots;

    public PlayerSpaceShip(Context context, View spaceShipView) {
        this.context = context;
        this.spaceShipView = spaceShipView;
        spaceship = BitmapFactory.decodeResource(context.getResources(), R.drawable.player);
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

    public int getOurSpaceshipWidth(){
        return spaceship.getWidth();
    }

    public int getOurSpaceshipHeight(){
        return spaceship.getHeight();
    }

    public int getWidth(){
        return spaceship.getWidth();
    }

    public int getHeight(){
        return spaceship.getHeight();
    }
    public Rect getCollisionShape () {
        return new Rect(
                (int) getPositionX(),
                (int) getPositionY(),
                (int) (getPositionX() + getWidth()),
                (int) (getPositionY() + getHeight()));
    }
}
