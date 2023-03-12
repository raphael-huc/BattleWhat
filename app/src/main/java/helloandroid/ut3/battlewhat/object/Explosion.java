package helloandroid.ut3.battlewhat.object;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import helloandroid.ut3.battlewhat.R;

public class Explosion {
    Bitmap[] explosions = new Bitmap[9];
    ImageView imageView;
    int explosionFrame;


    public Explosion(Context context, int eX, int eY){
        explosions[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.explosion0);
        explosions[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.explosion1);
        explosions[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.explosion2);
        explosions[3] = BitmapFactory.decodeResource(context.getResources(), R.drawable.explosion3);
        explosions[4] = BitmapFactory.decodeResource(context.getResources(), R.drawable.explosion4);
        explosions[5] = BitmapFactory.decodeResource(context.getResources(), R.drawable.explosion5);
        explosions[6] = BitmapFactory.decodeResource(context.getResources(), R.drawable.explosion6);
        explosions[7] = BitmapFactory.decodeResource(context.getResources(), R.drawable.explosion7);
        explosions[8] = BitmapFactory.decodeResource(context.getResources(), R.drawable.explosion8);
        explosionFrame = 0;

        ImageView imageView = new ImageView(context);
        imageView.setImageBitmap(explosions[0]);
        setImageView(imageView);

        setPositionX(eX);
        setPositionY(eY);
    }

    public boolean changeBitmapUntilFinish(Context context) {
        System.out.println(getExplosionFrame());
        setExplosionFrame(getExplosionFrame() + 1);
        if(getExplosionFrame() >= 9) {
            return false;
        }
        ImageView imageView = new ImageView(context);
        imageView.setImageBitmap(explosions[getExplosionFrame()]);
        setImageView(imageView);
        return true;
    }

    public Bitmap[] getExplosions() {
        return explosions;
    }

    public void setExplosions(Bitmap[] explosions) {
        this.explosions = explosions;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public int getExplosionFrame() {
        return explosionFrame;
    }

    public void setExplosionFrame(int explosionFrame) {
        this.explosionFrame = explosionFrame;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public float getPositionX() {
        return this.imageView.getX();
    }

    public void setPositionX(float x) {
        this.imageView.setX(x);
    }

    public float getPositionY() {
        return this.imageView.getY();
    }

    public void setPositionY(float y) {
        this.imageView.setY(y);
    }

    public Bitmap getExplosion(int explosionFrame){
        return explosions[explosionFrame];
    }
}
