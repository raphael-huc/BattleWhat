package helloandroid.ut3.battlewhat.object.spaceship;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import helloandroid.ut3.battlewhat.R;

public class PlayerSpaceShip extends SpaceShip {

    private final Animation animationHit;
    private final Handler handler;
    private final int REPEAT_ANIMATION = 3;
    private final int SPEED_ANIMATION = 500;
    private boolean isHit;
    private boolean godMod = false;


    public PlayerSpaceShip(Context context, ImageView spaceShipView) {
        super(context, spaceShipView);
        spaceship = BitmapFactory.decodeResource(context.getResources(), R.drawable.player);
        animationHit = new AlphaAnimation((float) 0.8, 0);
        animationHit.setDuration(SPEED_ANIMATION);
        animationHit.setInterpolator(new LinearInterpolator());
        animationHit.setRepeatCount(REPEAT_ANIMATION);
        handler = new Handler();
    }

    public void putGodMode() {
        spaceship = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy1);
        godMod = true;
    }

    public Rect getCollisionShape () {
        return new Rect(
                (int) getPositionX(),
                (int) getPositionY(),
                (int) (getPositionX() + getWidth()),
                (int) (getPositionY() + getHeight()));
    }

    public void makeAnimationHit() {
        if(!isHit) {
            spaceShipView.startAnimation(animationHit);
            handler.postDelayed(mWaitingAnimationEnd, SPEED_ANIMATION * REPEAT_ANIMATION);
            isHit = true;
        }
    }

    private final Runnable mWaitingAnimationEnd = new Runnable() {
        @Override
        public void run() {
            isHit = false;
        }
    };

    public boolean isHit() {
        return isHit;
    }

    public boolean isGodMod() {
        return godMod;
    }
}
