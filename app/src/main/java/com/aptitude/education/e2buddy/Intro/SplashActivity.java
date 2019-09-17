package com.aptitude.education.e2buddy.Intro;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.aptitude.education.e2buddy.R;

public class SplashActivity extends AppCompatActivity {

    TextView textView;

    ImageView imageView;
    Animation zoomout;
    private static int SPLASH_TIME = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        textView = findViewById(R.id.splashtex);
        imageView = findViewById(R.id.imageView);

        zoomout = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.zoomin);
        imageView.setAnimation(zoomout);

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade);
        textView.startAnimation(animation);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                Intent intent = new Intent(getApplicationContext(), IntroActivity.class);
                startActivity(intent);
                finish();


            }
        }, SPLASH_TIME);
    }

}

