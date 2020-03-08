package com.example.hoohootoo;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import static com.example.hoohootoo.MainThread.canvas;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private MainThread thread;
    private Mensch mensch;
    private Background background;

    float xDest = -1;
    float yDest = -1;
    boolean hookActive = false;
    double theta;
    final float hookStrength = 1.4f;

    Paint lineP = new Paint();

    public GameView(Context context) {
        super(context);
        thread = new MainThread(getHolder(), this);
        setFocusable(true);
        getHolder().addCallback(this);
        lineP.setColor(Color.RED);
        lineP.setStrokeWidth(9f);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        float x = e.getX();
        float y = e.getY();
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDest = x;
                yDest = y;
                invalidate();
                break;

            case MotionEvent.ACTION_UP:

                xDest = -1;
                yDest = -1;
                invalidate();
                break;
        }
        return true;
    }

    //define what shows up on the canvas
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (canvas != null) {
            canvas.drawColor(Color.WHITE);
            background.draw(canvas);
            mensch.draw(canvas);
            if (hookActive)
                canvas.drawLine(mensch.getX(), mensch.getY(), xDest, yDest, lineP);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread.setRunning(true);
        thread.start();
        Bitmap bmpMensch = BitmapFactory.decodeResource(getResources(), R.drawable.mensch);
        Bitmap bmpBG = BitmapFactory.decodeResource(getResources(), R.drawable.cave);
        bmpBG = Bitmap.createScaledBitmap(bmpBG, 2160, 1080, true);
        mensch = new Mensch(bmpMensch);
        background = new Background(bmpBG, 0, 0);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                thread.setRunning(false);
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }

    //let the app know what to do each loop
    public void update() {
        hookActive = !(xDest == -1 && yDest == -1);
        if (!hookActive) {
            mensch.setxAcc(0);
            mensch.setyAcc(0);
        } else {
            //1st quadrant
            if (xDest >= mensch.getX() && yDest < mensch.getY())
                theta = Math.atan((xDest - mensch.getX()) / (mensch.getY() - yDest));
            //2nd quadrant
            if (xDest > mensch.getX() && yDest >= mensch.getY())
                theta = Math.PI - Math.atan((xDest - mensch.getX()) / (yDest - mensch.getY()));
            //3rd quadrant
            if (xDest <= mensch.getX() && yDest > mensch.getY())
                theta = Math.PI + Math.atan((mensch.getX() - xDest) / (yDest - mensch.getY()));
            //4th quadrant
            if (xDest < mensch.getX() && yDest <= mensch.getY())
                theta = 2 * Math.PI - Math.atan((mensch.getX() - xDest) / (mensch.getY() - yDest));

            mensch.setxAcc((float) (hookStrength * Math.sin(theta)));
            mensch.setyAcc((float) (-hookStrength * Math.cos(theta)));
        }
        mensch.update();

    }
}
