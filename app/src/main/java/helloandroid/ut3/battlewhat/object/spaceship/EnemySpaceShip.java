package helloandroid.ut3.battlewhat.object.spaceship;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.view.View;

import java.util.Random;

import helloandroid.ut3.battlewhat.R;

public class EnemySpaceShip extends SpaceShip {

    private boolean youNeedToAvoid=false;
    private boolean dodgeNextMove=false;
    private int noNewActionUntil =0;


    public EnemySpaceShip(Context context, View spaceShipView) {
        super(context, spaceShipView);
        spaceship = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy1);
    }

    public Rect getCollisionShape () {
        return new Rect(
                (int) getPositionX(),
                (int) getPositionY(),
                (int) (getPositionX() + getWidth()),
                (int) (getPositionY() + getHeight()));
    }
    public Rect getPredictCollisionShape () {
        return new Rect(
                (int) getPositionX()+getWidth(),
                (int) getPositionY(),
                (int) (getPositionX() + getWidth()*2),
                (int) (getPositionY() + getHeight()));
    }
    public boolean getYouNeedToAvoid() {
        return youNeedToAvoid;
    }

    public boolean getDodgeNextMove() {
        Random rand = new Random();
        int dodgeNextMoveProba= rand.nextInt(100)+1;
        return dodgeNextMoveProba>90;
    }
    public void setYouNeedToAvoid(Boolean youNeedToAvoid) {
        this.youNeedToAvoid = youNeedToAvoid;
    }

    public int getNoNewActionUntil() {
        return noNewActionUntil;
    }

    public void setNoNewActionUntil(int noNewActionUntil) {
        this.noNewActionUntil = noNewActionUntil<0 ? noNewActionUntil : noNewActionUntil;
    }
    public void reduceNoNewActionUntil(int reducer){
        this.noNewActionUntil -= noNewActionUntil<reducer ?0 : reducer;
    }
}
