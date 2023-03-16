package helloandroid.ut3.battlewhat.object;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import java.util.Random;

import helloandroid.ut3.battlewhat.R;
import helloandroid.ut3.battlewhat.object.spaceship.SpaceShip;

public class ExplosionAnimation extends AnimationDrawable {
    private static final int FRAME_DURATION = 50;
    private static final int[] frameExplosions =  {
            R.drawable.explosion0,
            R.drawable.explosion1,
            R.drawable.explosion2,
            R.drawable.explosion3,
            R.drawable.explosion4,
            R.drawable.explosion5,
            R.drawable.explosion6,
            R.drawable.explosion7,
            R.drawable.explosion8,
    };
    private ImageView explosionImageView;
    private boolean isFinish = false;
    private final Handler handler;

    public ImageView getExplosionImageView() {
        return explosionImageView;
    }

    public ExplosionAnimation(Context context) {
        super();
        for (int i = 0; i < ExplosionAnimation.frameExplosions.length; i++) {
            Drawable frame = ContextCompat.getDrawable(context, ExplosionAnimation.frameExplosions[i]);
            addFrame(frame, FRAME_DURATION);
        }
        setOneShot(true);
        handler = new Handler();
    }

    public void makeAnimation(Context context, ConstraintLayout gameView, SpaceShip spaceShip) {
        ExplosionAnimation explosionAnimation = new ExplosionAnimation(context);
        explosionImageView = new ImageView(context);
        explosionImageView.setLayoutParams(new FrameLayout.LayoutParams(100, 100));
        // Make explosion position a little random
        Random random = new Random();
        explosionImageView.setX(spaceShip.getPositionX() + random.nextInt(spaceShip.getWidth() - 10) - spaceShip.getWidth() / 2f +10);
        explosionImageView.setY(spaceShip.getPositionY() + random.nextInt(spaceShip.getHeight() - 10) - spaceShip.getHeight() / 2f - 10);
        gameView.addView(explosionImageView);
        explosionImageView.setBackground(explosionAnimation);
        explosionAnimation.start();
        handler.postDelayed(mWaitingAnimationEnd, (long) explosionAnimation.getNumberOfFrames() * FRAME_DURATION);
    }

    private final Runnable mWaitingAnimationEnd = () -> isFinish = true;

    public boolean isFinish() {
        return isFinish;
    }
}