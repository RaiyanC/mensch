package com.example.hoohootoo;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Background {

    private int x;
    private int y;
    private Bitmap image;
    private float speed = 7f;

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    Background(Bitmap image, int x, int y) {
        this.image = image;
        this.x = x;
        this.y = y;
    }


    void draw(Canvas canvas) {

        // Draw the image onto the Graphics reference
        canvas.drawBitmap(image, x, y, null);

        // Move the x position left for next time
        this.x -= speed;
        if (x <= 0) {
            canvas.drawBitmap(image, x + image.getWidth(), y, null);
        }

//        if (x > 0) {
            canvas.drawBitmap(image, x - image.getWidth(), y, null);
//        }

        if (x < -image.getWidth()) {
            x += image.getWidth();
        }

//        if (x > image.getWidth()) {
//            x -= image.getWidth();
//        }

    }

}
