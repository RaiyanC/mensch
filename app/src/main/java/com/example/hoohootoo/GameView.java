package com.example.hoohootoo;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

//import java.util.List;
import androidx.annotation.RequiresApi;

import java.util.Random;
import java.util.Vector;

import static com.example.hoohootoo.MainThread.canvas;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private MainThread thread;
    private Mensch mensch;
    //    private Erin erin;
    Vector<Erin> erins = new Vector<>();
    private Background background;
    Random random = new Random();

    Bitmap bmpMensch = BitmapFactory.decodeResource(getResources(), R.drawable.mensch);
    Bitmap bmpErin = BitmapFactory.decodeResource(getResources(), R.drawable.erin);
    Bitmap bmpBG = BitmapFactory.decodeResource(getResources(), R.drawable.cave);
    Bitmap bmpHeart = BitmapFactory.decodeResource(getResources(), R.drawable.heart);

    float xDest = -1;
    float yDest = -1;
    boolean hookActive = false;
    double theta;
    final float hookStrength = 1.2f;
    final String DEATH_MSG = "GAME OVER";
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
                if (x < mensch.getX()) return false;
                xDest = x;
                yDest = y;
                invalidate();
                break;

            case MotionEvent.ACTION_UP:
                xDest = -999;
                yDest = -999;
                invalidate();
                break;
        }
        return true;
    }

    //define what shows up on the canvas
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (canvas != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                canvas.drawColor(Color.rgb(45.9f,54.1f,53.3f));
            }
            background.draw(canvas);
            if (hookActive)
                canvas.drawLine(
                        mensch.getX() + mensch.getHb().getWidth() / 2,
                        mensch.getY() + mensch.getHb().getHeight() / 2,
                        xDest, yDest, lineP);
            mensch.draw(canvas);
            erins.forEach(e -> e.draw(canvas));
            float x = getWidth();
            for (int i = 1; i <= mensch.getLives(); i++) {
                canvas.drawBitmap(bmpHeart, x - (bmpHeart.getWidth() + 10) * i, 50, null );
            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread.setRunning(true);
        thread.start();
        bmpBG = Bitmap.createScaledBitmap(bmpBG, 2160, 1080, true);

        mensch = new Mensch(bmpMensch);
        erins.add(new Erin(bmpErin, 700));
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
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void update() {
        hookActive = !(xDest <= -0 && yDest <= -0);
        if (!hookActive) {
            mensch.setxAcc(0);
            mensch.setyAcc(0);

        } else {
            //swing logic
            //1st quadrant
            if (xDest >= mensch.getHb().getCenterX() && yDest < mensch.getHb().getCenterY())
                theta = Math.atan((xDest - mensch.getHb().getCenterX()) / (mensch.getHb().getCenterY() - yDest));
            //2nd quadrant
            if (xDest > mensch.getHb().getCenterX() && yDest >= mensch.getHb().getCenterY())
                theta = Math.PI - Math.atan((xDest - mensch.getHb().getCenterX()) / (yDest - mensch.getHb().getCenterY()));
            //3rd quadrant
            if (xDest <= mensch.getHb().getCenterX() && yDest > mensch.getHb().getCenterY())
                theta = Math.PI + Math.atan((mensch.getHb().getCenterX() - xDest) / (yDest - mensch.getHb().getCenterY()));
            //4th quadrant
            if (xDest < mensch.getHb().getCenterX() && yDest <= mensch.getHb().getCenterY())
                theta = 2 * Math.PI - Math.atan((mensch.getHb().getCenterX() - xDest) / (mensch.getHb().getCenterY() - yDest));

            mensch.setxAcc((float) (hookStrength * Math.sin(theta)));
            mensch.setyAcc((float) (-hookStrength * Math.cos(theta)));
        }

        mensch.update();
        erins.forEach(Erin::update);

        if (mensch.getHb().getCenterY() > canvas.getHeight() || mensch.getLives() == 0) {
            System.out.println(DEATH_MSG);
//            mensch.decrementLives();
//            gameOver();
            System.exit(0);
        }

        // Hitbox collision test

        //deduct one life
        boolean currentlyCrashing = erins.stream()
                .filter(e->!e.isHit())
                .anyMatch(e -> mensch.getHb().collidesWith(e.getHb()));

        if (currentlyCrashing) {
            System.out.println("you hit erin, erin hit back");
            mensch.decrementLives();
        }
        //if hit true, won't deduct again
        erins.forEach(e -> e.setHit(mensch.getHb().collidesWith(e.getHb())));

        // get them lads movin
        background.setSpeed(mensch.getxVel());
        erins.forEach(e -> e.setxVel(mensch.getxVel()));
        xDest -= mensch.getxVel();

        if (random.nextInt(101) == 2)
            erins.add(new Erin(bmpErin, getHeight() - random.nextInt((int) (0.8 * getHeight()))));
    }

    public void gameOver() {
        Intent i = new Intent(getContext(), GameOverActivity.class);
        getContext().startActivity(i);
    }
}
