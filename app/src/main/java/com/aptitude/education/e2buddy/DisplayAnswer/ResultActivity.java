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
import com.aptitude.education.e2buddy.Intro.Quizapp;
import com.aptitude.education.e2buddy.One_on_One_Quiz_Challenge.LoaderForReceiverActivity;
import com.aptitude.education.e2buddy.Question.HomeNevActivity;
import com.aptitude.education.e2buddy.Question.QuizQuestionTimeBasedActivity;
import com.aptitude.education.e2buddy.Question.StartQuizActivity;
import com.aptitude.education.e2buddy.R;
import com.aptitude.education.e2buddy.ViewData.AnswerView;
import com.aptitude.education.e2buddy.ViewData.CreditView;
import com.aptitude.education.e2buddy.ViewData.InsertTotalScoreData;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
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


    Transformation transformation;
    ImageView userIcon;
    ImageView img1, img2, img3, img4;
    TextView tv1,tv2,tvscore,tvanswer, tvCorrectAnswer;
    Button leaderboard, btviewans;
    LinearLayout linearLayout;
    DatabaseReference databaseReference;
    List<AnswerView> answerViewList;
    String userid,quizdate ,date,value,quizids,question,corr_ans,quizid;
    int count1 = 0, scoreCount = 0,count = 0, scores = 0;
    ValueEventListener valueEventListener;

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
        Quizapp.getRefWatcher(ResultActivity.this).watch(this);


        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM");
        date = sdf.format(new Date());

        databaseReference = FirebaseDatabase.getInstance().getReference();

        quizdate = getIntent().getStringExtra("quiz_date");
        value  = getIntent().getStringExtra("curent_date");
        userid = getIntent().getStringExtra("userid");

        answerViewList = new ArrayList<>();

        showLoader();

        transformation = new RoundedTransformationBuilder()
                .borderColor(getResources().getColor(R.color.white))
                .borderWidthDp(0)
                .cornerRadiusDp(50)
                .oval(false)
                .build();



        getPlayerImage();
        getTodayQuizScore();


        btviewans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

  //              System.gc();
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

//                System.gc();
                Intent intent = new Intent(getApplicationContext(), LeaderBoardForQuizActivity.class);
                intent.putExtra("quiz_date",quizdate);
                intent.putExtra("curent_date", value);
                intent.putExtra("userid", userid);
                startActivity(intent);

            }
        });

    }

    private void showLoader() {

            final ProgressDialog dialog = new ProgressDialog(ResultActivity.this);
            dialog.setTitle("Please wait...");
            dialog.setMessage("Generating Your Result");
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.show();

            long delayInMillis = 4000;
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    getQuestionId();
                    dialog.dismiss();
                    insertTotalQuizScore();

                }
            }, delayInMillis);


    }
    private void getQuestionId() {
       valueEventListener = databaseReference.child("daily_Question").child(quizdate).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                   String q_id = dataSnapshot1.getKey();

                    //Toast.makeText(getApplicationContext(), ""+q_id,Toast.LENGTH_SHORT).show();

                    getUserAnswer(q_id);

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void getUserAnswer(final String q_id) {
       valueEventListener = databaseReference.child("daily_user_answer").child(userid).child(quizdate).child(value).child(q_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                //  Toast.makeText(getApplicationContext(),""+useranswer +"\n"+ timeleft,Toast.LENGTH_SHORT).show();
                try {
                    String useranswer = dataSnapshot.child("useranswer").getValue(String.class);
                    int timeleft = Integer.parseInt(dataSnapshot.child("timeleft").getValue().toString());
                    Log.d("AnswerActivity", "questionid :" + q_id);


                    if (useranswer == null) {
                        String.valueOf(useranswer.replace(null, "question was not given"));

                    }

                    getQuestion(q_id, useranswer, timeleft);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });

    }

    private void getQuestion(final String questionId, final String useranswer, final int timeleft){
        final String TAG = getClass().getSimpleName();

        valueEventListener = databaseReference.child("questions").child(questionId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    Log.d(TAG, "children are: "+dataSnapshot.getKey());

//                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Log.d(TAG, "childs: "+dataSnapshot.getKey());
                    question = dataSnapshot.child("Question").getValue(String.class);
                    corr_ans = dataSnapshot.child("Answer").getValue(String.class);


                    if (corr_ans.equals(useranswer)) {
                        count = count+2;

                        scores = scores+timeleft*2;
                        count1 = count1+1;

                    }



                    insertCredit();

                    //  }
                }catch (NullPointerException | DatabaseException e){
                    e.printStackTrace();
                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }



    private void insertCredit(){

        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("daily_user_credit");

        final int num =1;

        CreditView creditView = new CreditView(scores,quizdate,quizid,count1);

        reference1.child(userid).child(quizdate).child(value).setValue(creditView);

    }


    private void getTodayQuizScore(){
      valueEventListener =  databaseReference.child("daily_user_credit").child(userid).child(quizdate).child(value).addValueEventListener(new ValueEventListener() {
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

       valueEventListener = databaseReference.child("user_info").child(userid).addValueEventListener(new ValueEventListener() {
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
  //      trimCache(getApplicationContext());

        Intent intent = new Intent(getApplicationContext(), HomeNevActivity.class);
        startActivity(intent);
        finish();


    }

    private void insertTotalQuizScore(){

        databaseReference.child("daily_user_credit").child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    String id = dataSnapshot1.getKey();

                    getCreditScore(id);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    private void getCreditScore(String date){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference reference6;
        reference6 = FirebaseDatabase.getInstance().getReference("daily_user_total_score");

        final Query lastQuery = reference.child("daily_user_credit").child(userid).child(date).orderByKey().limitToLast(1);


        lastQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                quizids = dataSnapshot.getKey();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    try {
                        String id = dataSnapshot1.getKey();

                        int quiz = (Integer.parseInt(dataSnapshot1.child("credit_points").getValue().toString()));

                        scoreCount = scoreCount + quiz;

                        InsertTotalScoreData insertTotalScoreData = new InsertTotalScoreData(String.valueOf(scoreCount));
                        reference6.child(userid).setValue(insertTotalScoreData);

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseReference.removeEventListener(valueEventListener);
    }
}


