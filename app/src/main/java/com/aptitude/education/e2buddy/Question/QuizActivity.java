package com.aptitude.education.e2buddy.Question;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aptitude.education.e2buddy.DisplayAnswer.LeaderBoardAdapter;
import com.aptitude.education.e2buddy.DisplayAnswer.LeaderBoardData;
import com.aptitude.education.e2buddy.DisplayAnswer.ResultActivity;
import com.aptitude.education.e2buddy.R;
import com.aptitude.education.e2buddy.ViewData.InsertAnswer;
import com.aptitude.education.e2buddy.ViewData.QuestionView;
import com.aptitude.education.e2buddy.ViewData.TimeData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class QuizActivity extends AppCompatActivity {


    QuizAdapter questionAdapter;
    LinearLayout linearLayout;
    String questionid, question,date,q_id,value, option1, option2,option3,option4, answer,userid,quizdate,description;
    int onlineplayecount = 1534;
    TextView onlinuser,tvcancelquiz;
    Button next;
    DatabaseReference databaseReference;
    private List<QuestionView> questionViewList;
    ProgressDialog progressDialog;
    ValueEventListener valueEventListener;
    RecyclerView recycler;
    RecyclerView.LayoutManager layoutManager;

    MediaPlayer player,player1;
    FirebaseAuth auth;
    private ProgressBar progressBarCircle;
    private TextView textViewTime;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_quiz);
            progressBarCircle = (ProgressBar) findViewById(R.id.progressBarCircle);
            textViewTime = (TextView) findViewById(R.id.textViewTime);
            linearLayout = findViewById(R.id.linear);
            next = findViewById(R.id.bnext);
            tvcancelquiz = findViewById(R.id.tvcancel);
            onlinuser = findViewById(R.id.onlineuser);
            recycler = findViewById(R.id.recyclerView);
            layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
            recycler.setLayoutManager(layoutManager);

            databaseReference = FirebaseDatabase.getInstance().getReference();

            auth = FirebaseAuth.getInstance();
            final FirebaseUser user = auth.getCurrentUser();
            userid = user.getUid();

            quizdate = getIntent().getStringExtra("quiz_date");

            value  = getIntent().getStringExtra("curent_date");

            questionViewList = new ArrayList<>();

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM");
            date = sdf.format(new Date());

            progressDialog = new ProgressDialog(QuizActivity.this);
            progressDialog.setCancelable(true);
            progressDialog.setMessage("Data Loading...");
            progressDialog.show();


            questionAdapter = new QuizAdapter(getApplicationContext(),questionViewList, value, userid,quizdate);

            valueEventListener =  databaseReference.child("daily_Question").child(quizdate).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                        q_id = dataSnapshot1.getKey();
                        getQuizQuestions(q_id);

                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });



            Runnable progressRunnable = new Runnable() {

                @Override
                public void run() {
                    progressDialog.cancel();

                }
            };

            Handler pdCanceller = new Handler();
            pdCanceller.postDelayed(progressRunnable, 3000);


            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                                  }
            });



            tvcancelquiz.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (!isFinishing()) {
                        new AlertDialog.Builder(QuizActivity.this)
                                .setTitle("Alert!!")
                                .setMessage("Are you sure you want to Exit the Quiz?")

                                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        player.stop();
                                        player1.stop();

                                        databaseReference.child("daily_user_answer").child(userid).child(quizdate).child(value).removeValue();
                                        Intent i = new Intent(getApplicationContext(), HomeNevActivity.class);
                                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(i);
                                        QuizActivity.this.finish();                        }
                                })

                                .setNegativeButton("NO", null)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }

                }
            });

            getOnlinePlayers();

        }

        private void getOnlinePlayers(){
            databaseReference.child("online_users").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    int count = (int) dataSnapshot.getChildrenCount();
                    onlineplayecount = onlineplayecount +count;
                    onlinuser.setText(""+onlineplayecount);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }


        private void getQuizQuestions(String q_id) {


            valueEventListener = databaseReference.child("questions").child(q_id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    questionid = dataSnapshot.getKey();
                    question = dataSnapshot.child("Question").getValue(String.class);
                    option1 = dataSnapshot.child("Option1").getValue(String.class);
                    option2 = dataSnapshot.child("Option2").getValue(String.class);
                    option3 = dataSnapshot.child("Option3").getValue(String.class);
                    option4 = dataSnapshot.child("Option4").getValue(String.class);
                    answer = dataSnapshot.child("Answer").getValue(String.class);
                    description = dataSnapshot.child("Description").getValue(String.class);

                    Log.e("dkmsg",questionid +"\n"+ question +"\n"+ option1 + "\n"+option2 +"\n"+ option3 +"\n"+ option4);

                    questionViewList.add(new QuestionView(questionid, question, option1, option2, option3, option4, answer, "null"));

                    recycler.setAdapter(questionAdapter);
                }


                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }


        }




