package com.aptitude.education.e2buddy.DisplayAnswer;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aptitude.education.e2buddy.AdMob.AdManager;
import com.aptitude.education.e2buddy.Intro.CheckInternet;
import com.aptitude.education.e2buddy.One_on_One_Quiz_Challenge.LoaderForReceiverActivity;
import com.aptitude.education.e2buddy.Question.HomeNevActivity;
import com.aptitude.education.e2buddy.Question.StartQuizActivity;
import com.aptitude.education.e2buddy.R;
import com.aptitude.education.e2buddy.ViewData.AnswerView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ViewAnswerActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ProgressDialog dialog;
    SharedPreferences sharedPreferences,sharedPreferences1;
    SharedPreferences.Editor editor;
    TextView tvuser,tvanswer,tvCorrectAnswer;
    Button bttry;
    Transformation transformation;
    DatabaseReference databaseReference;
    String quizid,quizname,userid,useranswer,question,corr_ans,quizdate,username;
    String TAG = getClass().getSimpleName();
    AnswerAdapter answerAdapter;
    public ArrayList<AnswerView> answerViewList;
    String student_name,q_id,value;
    String date;
  //  DatabaseReference databaseReference6;
    ImageView userIcon;
    AdView mAdView;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_answer3);
        recyclerView = findViewById(R.id.answerrecy);
        tvuser = findViewById(R.id.user);
        bttry = findViewById(R.id.bttryagain);
        userIcon = findViewById(R.id.userimage);
        tvCorrectAnswer = findViewById(R.id.tvcorrectans);
        tvanswer = findViewById(R.id.tvans);


        progressDialog = new ProgressDialog(ViewAnswerActivity.this);
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Data Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);
        progressDialog.setMax(100);
        progressDialog.show();

        Runnable progressRunnable = new Runnable() {

            @Override
            public void run() {
                progressDialog.dismiss();

            }
        };

        Handler pdCanceller = new Handler();
        pdCanceller.postDelayed(progressRunnable, 15000);


        /*CheckInternet checkInternet = new CheckInternet(getApplicationContext());
        checkInternet.checkConnection();
*/
        answerViewList = new ArrayList<>();

        Typeface type = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
        tvuser.setTypeface(type);
        tvCorrectAnswer.setTypeface(type);
        tvanswer.setTypeface(type);



        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        //databaseReference6 = FirebaseDatabase.getInstance().getReference();

        sharedPreferences = getSharedPreferences("quizpref", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        quizid = sharedPreferences.getString("quizid", "");
        quizname = sharedPreferences.getString("quizname", "");
        userid = sharedPreferences.getString("userid", "");
        quizdate = sharedPreferences.getString("quizdate","");
        username = sharedPreferences.getString("username", "");

        sharedPreferences1 = getSharedPreferences("quiz", Context.MODE_PRIVATE);
        value = sharedPreferences1.getString("rundom_no","");


        transformation = new RoundedTransformationBuilder()
                .borderColor(getResources().getColor(R.color.white))
                .borderWidthDp(1)
                .cornerRadiusDp(50)
                .oval(false)
                .build();

        getTodayQuizScore();


        databaseReference.child("daily_Question").child(quizdate).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                    q_id = dataSnapshot1.getKey();

                    //Toast.makeText(getApplicationContext(), ""+q_id,Toast.LENGTH_SHORT).show();

                    getUserAnswer(q_id);

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        bttry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                databaseReference.child("daily_user_credit").child(userid).child(quizdate).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {
                            long count = dataSnapshot.getChildrenCount();
                          //  Toast.makeText(getApplicationContext(), ""+ count,Toast.LENGTH_SHORT).show();
                            if (count<3){

                                editor.putString("quizid", quizid);
                                editor.putString("quizdate",quizdate);
                                editor.commit();
                                Intent intent = new Intent(ViewAnswerActivity.this, StartQuizActivity.class);
//                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();

                            }else {
                                Toast.makeText(ViewAnswerActivity.this, "You have already attemped 3 times", Toast.LENGTH_LONG).show();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



            }
        });

        //getPlayerName();

    }

    private void getUserAnswer(final String q_id) {

        databaseReference.child("daily_user_answer").child(userid).child(quizdate).child(value).child(q_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                        useranswer = dataSnapshot.child("useranswer").getValue(String.class);
                        Log.d("AnswerActivity", "questionid :"+q_id);

                        try {

//                            if (useranswer==null){
////                                String.valueOf(useranswer.replace(null, "question was not given"));
//                            }

                            getQuestion(q_id, useranswer);

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

    private void getTodayQuizScore(){
        databaseReference.child("daily_user_credit").child(userid).child(quizdate).child(value).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                try {

                    String corr_answer = dataSnapshot.child("correct_answers").getValue().toString();
                    tvCorrectAnswer.setText(""+corr_answer+"/10");
                   // progressDialog.dismiss();

                }

                catch (Exception e){
                    e.printStackTrace();
                }
             //   progressDialog.dismiss();

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    private void getQuestion(final String questionId, final String useranswer){

        databaseReference.child("questions").child(questionId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    Log.d(TAG, "children are: "+dataSnapshot.getKey());

                    Log.d(TAG, "childs: "+dataSnapshot.getKey());
                    question = dataSnapshot.child("Question").getValue(String.class);
                    corr_ans = dataSnapshot.child("Answer").getValue(String.class);

                    answerViewList.add(new AnswerView(
                            questionId,  question, corr_ans, useranswer
                    ));

                    answerAdapter = new AnswerAdapter(ViewAnswerActivity.this, answerViewList);
                    recyclerView.setAdapter(answerAdapter);
               //     answerAdapter.notifyDataSetChanged();

                  //  dialog.dismiss();


                }catch (NullPointerException | DatabaseException e){
                    e.printStackTrace();
                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void getPlayerName(){
        DatabaseReference reference4;

        reference4 = FirebaseDatabase.getInstance().getReference();
        reference4.child("user_info").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                    student_name = dataSnapshot.child(userid).child("student_name").getValue().toString();
                    String imageUrl = dataSnapshot.child(userid).child("image_Url").getValue(String.class);


                    tvuser.setText(""+student_name);

                    Picasso.with(getApplicationContext())
                            .load(imageUrl)
                            .placeholder(R.drawable.userimg)
                            .fit()
                            .transform(transformation)
                            .into(userIcon);


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



}
