package com.example.hoohootoo;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Mensch {

    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

    private float g = 0.4f;
    private float xAcc = 0;
    private float yAcc = 0;
    private float xVel = 0;
    private float yVel = 0;
    private float x = 0.2f*screenWidth;
    private float y = 0.2f*screenHeight;

    private int lives = 5;
    private Bitmap image;
    private Hitbox hb;

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

    public Hitbox getHb() {
        return hb;
    }

    public void decrementLives() {
        lives--;
    }

    public int getLives() {
        return lives;
    }

    Mensch (Bitmap bmp) {
        image = bmp;
        hb = new Hitbox(x,y,bmp.getWidth(), bmp.getHeight());
    }

    void draw(Canvas canvas) {
        canvas.drawBitmap(image, x, y, new Paint());
        hb.draw(canvas);
    }

    void update() {

        yAcc += g;
        xVel += xAcc;
        yVel += yAcc;
//        x    += xVel;
        y    += yVel;
        //move hitbox
        hb.setX(x);
        hb.setY(y);

    }
}
