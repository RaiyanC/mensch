package com.example.hoohootoo;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Hitbox {
    private float x;
    private float y;
    private float height;
    private float width;

    Paint hbPaint = new Paint();

    public Hitbox(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        hbPaint.setStrokeWidth(10);
        hbPaint.setColor(Color.rgb(0,255,0));
        hbPaint.setStyle(Paint.Style.STROKE);

    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    boolean collidesWith(Hitbox other) {
        float topLeftX = other.x;
        float topLeftY = other.y;

        float topRightX = other.x + width;
        float topRightY = other.y;

        float bottomLeftX = other.x;
        float bottomLeftY = other.y + height;

        float bottomRightX = other.x + width;
        float bottomRightY = other.y + height;

        if (contains(topLeftX, topLeftY) || contains(topRightX, topRightY) ||
            contains(bottomLeftX, bottomLeftY) || contains(bottomRightX, bottomRightY)) {
            return true;
        }
        return false;
    }

    private boolean contains(float x, float y) {
        float x1 = this.x;
        float y1 = this.y;
        float x2 = this.x + width;
        float y2 = this.y + height;
        return (x < x2 && x > x1 && y < y2 && y > y1);
    }

    public void draw(Canvas canvas) {
        canvas.drawRect(x, y, x+width, y+height, hbPaint);
    }

    public float getCenterX() {
        return x+width/2;
    }
    public float getCenterY() {
        return y+height/2;
    }

}
