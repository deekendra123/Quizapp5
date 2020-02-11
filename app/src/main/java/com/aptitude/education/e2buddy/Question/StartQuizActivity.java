package com.aptitude.education.e2buddy.Question;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.aptitude.education.e2buddy.GoogleAds.BannerAd;
import com.aptitude.education.e2buddy.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class StartQuizActivity extends AppCompatActivity {


    TextView tv1, tv2, tv3, tv4, tv5, tv6, tv7;
    Button start;
    String value,quiz_date;
    SharedPreferences sharedPreferences ;
    SharedPreferences.Editor editor;


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


        BannerAd bannerAd = new BannerAd(this);
        bannerAd.loadActivityBannerAd();

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

        sharedPreferences = getSharedPreferences("e2buddy",  MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString("quiz_date", quiz_date);
        editor.putString("curent_date", value);
        editor.commit();


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start.setEnabled(false);
                System.gc();
                Intent intent = new Intent(getApplicationContext(), QuizQuestionTimeBasedActivity.class);
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
        finish();
    }
}
