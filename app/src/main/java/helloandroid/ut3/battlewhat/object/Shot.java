package helloandroid.ut3.battlewhat.object;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.widget.ImageView;

public class Shot {

    final int RESHAPE_X_COLLISION = 100;
    Context context;
    Bitmap shot;
    ImageView imageView;
    public boolean isHit;

    public Shot(Context context, int shx, int shy, int idShoot){
        this.context = context;
        isHit = false;
        shot = BitmapFactory.decodeResource(context.getResources(), idShoot);
        ImageView imageView = new ImageView(context);
        imageView.setImageBitmap(shot);
        setImageView(imageView);
        setPositionX(shx);
        setPositionY(shy);
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public ImageView getImageView() {
        return imageView;
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

    public Bitmap getShot(){
        return shot;
    }
    public int getWidth() {
        return shot.getWidth();
    }
    public int getHeight() {
        return shot.getHeight();
    }

    public Rect getCollisionShape () {
        int centerX = (int) (getPositionX() + getWidth() / 2) + RESHAPE_X_COLLISION;
        int centerY = (int) (getPositionY() + getHeight() / 2);

        return new Rect(
                (int) centerX,
                (int) centerY,
                (int) (centerX + 1),
                (int) (centerY) + 1);
    }
}
