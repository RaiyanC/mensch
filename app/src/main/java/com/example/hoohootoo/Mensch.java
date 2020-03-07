package com.example.hoohootoo;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Mensch {

    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

    private float g = 0.6f;
    private float xAcc = 0;
    private float yAcc = 0.2f;
    private float xVel = 0;
    private float yVel = 0;
    private float x = 0.5f*screenWidth;
    private float y = 0.5f*screenHeight;
    private Bitmap image;

    public float getxVel() {
        return xVel;
    }

    public float getyVel() {
        return yVel;
    }

    float getX() {
        return x;
    }

    float getY() {
        return y;
    }

    public void setxAcc(float xAcc) {
        this.xAcc = xAcc;
    }

    public void setyAcc(float yAcc) {
        this.yAcc = yAcc;
    }

    public void setxVel(float xVel) {
        this.xVel = xVel;
    }

    public void setyVel(float yVel) {
        this.yVel = yVel;
    }

    Mensch (Bitmap bmp) {
        image = bmp;
    }

    void draw(Canvas canvas) {
        canvas.drawBitmap(image, x-128, y-160, new Paint());

    }

    void update() {


//        // Hitting edge sets velocity to zero
//        if (((x >= screenWidth - image.getWidth()/2f) || (x < 0)) ) {
//            xAcc = 0;
//            xVel = 0;
//            return;
//        }
//        if ((y >= screenHeight - image.getHeight()/2f) || (y < 0)) {
//            yAcc = 0;
//            yVel = 0;
//            return;
//        }

        yAcc += g;
        xVel += xAcc;
        yVel += yAcc;
        x    += xVel;
        y    += yVel;




    }
}
