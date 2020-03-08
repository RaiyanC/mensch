package com.example.hoohootoo;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class GameOverActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        setContentView(R.layout.game_over_screen);
//        MediaPlayer soundtrack = MediaPlayer.create(getApplicationContext(), R.raw.afoggydaytodie);
//        soundtrack.start();
    }



    //    public void startplaying(View view) {
//        Button button = findViewById(R.id.play);
//        button.setOnClickListener(v -> {
//            setContentView(new GameView(getApplicationContext()));
//        });
//    }
//
//    public void leavegame(View view) {
//        Button button = findViewById(R.id.quit);
//        button.setOnClickListener(v -> System.exit(0));
//    }




}
