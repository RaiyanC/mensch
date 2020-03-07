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

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private MainThread thread;
    private Mensch mensch;

    float xDest = 200;
    float yDest = 200;
    double theta;
    final float hookStrength = 2f;

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
//                x1 = x;
//                y1 = y;
                xDest = x;
                yDest = y;
//                theta = (float) Math.atan((xDest-mensch.getX())/(yDest-mensch.getY()));


                invalidate();

                break;
            case MotionEvent.ACTION_MOVE:
//                xDest = x;
//                yDest = y;


                invalidate();
                break;
            case MotionEvent.ACTION_UP:
//                x1 = 0;
//                y1 = 0;
//                x2 = 0;
//                y2 = 0;
//                board.setxVel((x2-x1)/50);
//                board.setyVel((y2-y1)/50);
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
            mensch.draw(canvas);
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
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.mensch);
        mensch = new Mensch(bmp);

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
        if (xDest >= mensch.getX() && yDest < mensch.getY())
            theta = Math.atan((xDest - mensch.getX()) / (mensch.getY() - yDest));
        if (xDest > mensch.getX() && yDest >= mensch.getY())
            theta = Math.PI - Math.atan((xDest - mensch.getX()) / (yDest - mensch.getY()));
        if (xDest <= mensch.getX() && yDest > mensch.getY())
            theta = Math.PI + Math.atan((mensch.getX() - xDest) / (yDest - mensch.getY()));
        if (xDest < mensch.getX() && yDest <= mensch.getY())
            theta = 2*Math.PI - Math.atan((mensch.getX() - xDest) / (mensch.getY() - yDest));

        mensch.setxAcc((float) (hookStrength*Math.sin(theta)));
        mensch.setyAcc((float) (-hookStrength*Math.cos(theta)));
        mensch.update();

    }
}
