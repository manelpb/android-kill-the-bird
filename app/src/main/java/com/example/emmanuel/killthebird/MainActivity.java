package com.example.emmanuel.killthebird;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private int BIRD_ID;
    private int kills;
    private ArrayList arObjects;
    private TextView lblKillsNumber;
    private ImageView imgBird;
    private TextView lblGameOver;
    private TextView lblRecord;
    private Button btnNewGame;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        kills = 0;
        arObjects = new ArrayList();
        arObjects.add(R.drawable.pizza);
        arObjects.add(R.drawable.boot);
        arObjects.add(R.drawable.trash);
        arObjects.add(R.drawable.banana);
        arObjects.add(R.drawable.bird3);
        arObjects.add(R.drawable.bird3);
        arObjects.add(R.drawable.bird3);
        arObjects.add(R.drawable.bird3);
        arObjects.add(R.drawable.bird3);
        BIRD_ID = R.drawable.bird3;

        lblGameOver = (TextView)findViewById(R.id.lblGameOver);
        btnNewGame = (Button)findViewById(R.id.btnNewGame);
        lblKillsNumber = (TextView)findViewById(R.id.lblKillsNumber);
        imgBird = (ImageView)findViewById(R.id.imgBird);
        lblRecord = (TextView)findViewById(R.id.lblRecordNum);

        this.setTitle("Kill the Blue Bird");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        // pull the record
        prefs = getSharedPreferences("KillTheBird", Context.MODE_PRIVATE);
        lblRecord.setText(prefs.getString("record", "0"));

        // start the game
        StartGame();
    }

    public void OnNewGameClick(View v) {
        btnNewGame.setVisibility(View.INVISIBLE);
        lblGameOver.setVisibility(View.INVISIBLE);

        RandomObj(imgBird.getAnimation(), true);
        imgBird.getAnimation().start();
    }

    private void StartGame() {
        imgBird.setBackgroundResource(R.drawable.bird3);
        imgBird.setVisibility(View.INVISIBLE);
        imgBird.setTag(R.drawable.bird3);

        // set up the animation
        TranslateAnimation animation = new TranslateAnimation(400.0f, -2000.0f, 0.0f, 0.0f);
        animation.setDuration(3000);  // animation duration
        animation.setRepeatCount(Animation.INFINITE);  // animation repeat count
        animation.setRepeatMode(1);   // repeat animation (left to right, right to left )
        imgBird.startAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                imgBird.setVisibility(View.VISIBLE);
                RandomObj(animation, true);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                imgBird.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                RandomObj(animation, true);
            }
        });
    }

    private void RandomObj(Animation animation, boolean updateDuration) {
        Random r = new Random();

        int obj = (Integer) arObjects.get(r.nextInt(arObjects.size() - 1));
        imgBird.setBackgroundResource(obj);
        imgBird.setTag(obj);

        if(updateDuration) {
            animation.setDuration(2000 + (500 * r.nextInt(6)));
        }
    }

    public void OnBirdClick(View v) {
        Log.d("CLICK", String.valueOf(imgBird.getTag()) + "_____" + BIRD_ID);

        if(((Integer)imgBird.getTag()) == BIRD_ID) {
            // inc kill count and update
            kills++;
            lblKillsNumber.setText(String.valueOf(kills));
            imgBird.getAnimation().setDuration(0);

            if(kills > Integer.parseInt(lblRecord.getText().toString())) {
                lblRecord.setText(String.valueOf(kills));

                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("record", lblRecord.getText().toString());
                editor.commit();
            }
        } else {
            // game over
            btnNewGame.setVisibility(View.VISIBLE);
            lblGameOver.setVisibility(View.VISIBLE);
            imgBird.getAnimation().cancel();

            // reset the game
            kills = 0;
            lblKillsNumber.setText(String.valueOf(kills));
        }
    }
}
