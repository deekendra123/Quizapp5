package com.aptitude.education.e2buddy.Question;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aptitude.education.e2buddy.AdMob.AdManager;
import com.aptitude.education.e2buddy.Intro.CheckInternet;
import com.aptitude.education.e2buddy.One_on_One_Quiz_Challenge.LoaderForReceiverActivity;
import com.aptitude.education.e2buddy.One_on_One_Quiz_Challenge.QuestionForUser2Activity;
import com.aptitude.education.e2buddy.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class StartQuizActivity extends AppCompatActivity {


    TextView tv1, tv2, tv3, tv4, tv5, tv6, tv7;
    Button start;
    String value,quiz_date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_quiz2);

        tv1 = findViewById(R.id.textView6);
        tv2 = findViewById(R.id.textView5);
        tv3 = findViewById(R.id.textView7);
        tv4 = findViewById(R.id.textView8);
        tv5 = findViewById(R.id.textView9);
        tv6 = findViewById(R.id.textView10);

        tv7 = findViewById(R.id.textView11);

        start = findViewById(R.id.startquiz);

        quiz_date = getIntent().getStringExtra("quiz_date");

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30"));
        Date currentLocalTime = cal.getTime();
        DateFormat date1 = new SimpleDateFormat("dd-MM::HH:mm", Locale.UK);

        date1.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));

        value = date1.format(currentLocalTime);
        tv2.setText("1) Number of Questions : 10");

        tv3.setText("2) Attempt all the Questions");
        tv4.setText("3) Earn Coins for every Correct Answers");
        tv5.setText("4) No Negative Marking for Wrong Answer");
        tv6.setText("5) Use Coins to unlock Premium features");

        tv7.setText("Time Allotted : 15 sec/question");


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                start.setEnabled(false);
                System.gc();

                Intent intent = new Intent(getApplicationContext(), QuizActivity.class);
                intent.putExtra("quiz_date",quiz_date);
                intent.putExtra("curent_date", value);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(getApplicationContext(), HomeNevActivity.class);
        startActivity(intent);
        finish();


    }

}
