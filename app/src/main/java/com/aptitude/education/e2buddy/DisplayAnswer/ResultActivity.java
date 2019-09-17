package com.aptitude.education.e2buddy.DisplayAnswer;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aptitude.education.e2buddy.Intro.CheckInternet;
import com.aptitude.education.e2buddy.One_on_One_Quiz_Challenge.LoaderForReceiverActivity;
import com.aptitude.education.e2buddy.Question.HomeNevActivity;
import com.aptitude.education.e2buddy.Question.StartQuizActivity;
import com.aptitude.education.e2buddy.R;
import com.aptitude.education.e2buddy.ViewData.AnswerView;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ResultActivity extends AppCompatActivity {

    DatabaseReference databaseReference6;
    Transformation transformation;
    ImageView userIcon;
    ImageView img1, img2, img3, img4;
    TextView tv1,tv2,tvscore,tvanswer, tvCorrectAnswer;
    Button leaderboard, btviewans;
    LinearLayout linearLayout;
    DatabaseReference databaseReference;
    List<AnswerView> answerViewList;
    String userid,quizdate ,date,value;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result2);

        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tscore);
        tvscore = findViewById(R.id.tscore1);
        leaderboard = findViewById(R.id.btleaderboard);
        userIcon = findViewById(R.id.userimg);
        btviewans = findViewById(R.id.btview);
        img1 = findViewById(R.id.img1);
        img2 = findViewById(R.id.img2);
        img3 = findViewById(R.id.img3);
        img4 = findViewById(R.id.img4);
        linearLayout = findViewById(R.id.linear);
        tvCorrectAnswer = findViewById(R.id.tvcorrectans);
        tvanswer = findViewById(R.id.tvans);

        progressDialog = new ProgressDialog(ResultActivity.this);
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Data Loading...");
        progressDialog.show();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM");
        date = sdf.format(new Date());

        quizdate = getIntent().getStringExtra("quiz_date");
        value  = getIntent().getStringExtra("curent_date");
        userid = getIntent().getStringExtra("userid");


        Runnable progressRunnable = new Runnable() {

            @Override
            public void run() {
                progressDialog.dismiss();

            }
        };

        Handler pdCanceller = new Handler();
        pdCanceller.postDelayed(progressRunnable, 3000);

        transformation = new RoundedTransformationBuilder()
                .borderColor(getResources().getColor(R.color.white))
                .borderWidthDp(0)
                .cornerRadiusDp(50)
                .oval(false)
                .build();

        answerViewList = new ArrayList<>();

        getPlayerImage();
        databaseReference6 = FirebaseDatabase.getInstance().getReference();
        getTodayQuizScore();

        databaseReference = FirebaseDatabase.getInstance().getReference();

        btviewans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                System.gc();
                FragmentManager fm = getSupportFragmentManager();
                DialogFragment dialog = View_Answer_Dialog.newInstance();

                Bundle bundle = new Bundle();
                bundle.putString("quiz_date",quizdate);
                bundle.putString("curent_date",value);
                bundle.putString("userid",userid);
                dialog.setArguments(bundle);
                dialog.show(fm,"tag");


            }
        });


        leaderboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                System.gc();
                Intent intent = new Intent(getApplicationContext(), LeaderBoardForQuizActivity.class);
                intent.putExtra("quiz_date",quizdate);
                intent.putExtra("curent_date", value);
                intent.putExtra("userid", userid);
                startActivity(intent);

            }
        });



    }



    private void getTodayQuizScore(){
        databaseReference6.child("daily_user_credit").child(userid).child(quizdate).child(value).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                try {
                    String score = dataSnapshot.child("credit_points").getValue().toString();
                    int corr_answer = Integer.parseInt(dataSnapshot.child("correct_answers").getValue().toString());
                    tvscore.setText(""+score);
                    tvCorrectAnswer.setText(""+corr_answer+"/10");

                    if (corr_answer<=4){
                        linearLayout.setVisibility(View.GONE);
                        img1.setVisibility(View.GONE);
                        img2.setVisibility(View.GONE);
                        img3.setVisibility(View.GONE);

                        ViewGroup.MarginLayoutParams marginParams1 = (ViewGroup.MarginLayoutParams) tv1.getLayoutParams();
                        marginParams1.setMargins(0, 40, 0, 0);

                        ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) tv1.getLayoutParams();
                        marginParams.setMargins(0, 50, 0, 35);


                        tv1.setTextSize(21f);
                        img4.setBackgroundResource(R.drawable.goforit);

                        tv1.setText("Better Luck Next Time");
                    }
                    else {

                        img4.setBackgroundResource(R.drawable.ic_achievement);
                        tv1.setText("Congratulations!");

                    }

                }

                catch (Exception e){
                    e.printStackTrace();
                }


            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    private void getPlayerImage(){
        DatabaseReference databaseReference;
        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("user_info").child(userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String imageUrl = dataSnapshot.child("image_Url").getValue(String.class);

                Picasso.with(getApplicationContext())
                        .load(imageUrl)
                        .placeholder(R.drawable.userimg)
                        .fit()
                        .transform(transformation)
                        .into(userIcon);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        System.gc();
        trimCache(getApplicationContext());

        Intent intent = new Intent(getApplicationContext(), HomeNevActivity.class);
        startActivity(intent);
        finish();


    }

    public static void trimCache(Context context) {
        try {
            File dir = context.getCacheDir();
            if (dir != null && dir.isDirectory()) {
                deleteDir(dir);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        // The directory is now empty so delete it
        return dir.delete();
    }



}


