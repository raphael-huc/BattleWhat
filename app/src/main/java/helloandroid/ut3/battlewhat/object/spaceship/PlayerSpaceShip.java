package helloandroid.ut3.battlewhat.object.spaceship;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Random;

import helloandroid.ut3.battlewhat.R;
import helloandroid.ut3.battlewhat.activity.GameActivity;
import helloandroid.ut3.battlewhat.object.Shot;

public class PlayerSpaceShip extends SpaceShip {

    public PlayerSpaceShip(Context context, ImageView spaceShipView) {
        super(context, spaceShipView);
        spaceship = BitmapFactory.decodeResource(context.getResources(), R.drawable.player);
    }

    public void putGodMode() {
        spaceship = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy1);
    }

    public Rect getCollisionShape () {
        return new Rect(
                (int) getPositionX(),
                (int) getPositionY(),
                (int) (getPositionX() + getWidth()),
                (int) (getPositionY() + getHeight()));
    }
}
