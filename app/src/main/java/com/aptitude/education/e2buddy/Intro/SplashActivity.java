package com.aptitude.education.e2buddy.Intro;

import android.content.Intent;

import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.aptitude.education.e2buddy.Question.HomeNevActivity;
import com.aptitude.education.e2buddy.R;
import com.google.firebase.auth.FirebaseAuth;

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

                if (FirebaseAuth.getInstance().getCurrentUser()==null){
                    Intent intent = new Intent(getApplicationContext(), IntroActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Intent intent = new Intent(getApplicationContext(), HomeNevActivity.class);
                    startActivity(intent);
                    finish();
                }


            }
        }, SPLASH_TIME);
    }

}

