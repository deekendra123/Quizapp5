package com.aptitude.education.e2buddy.Question;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.aptitude.education.e2buddy.DisplayAnswer.LeaderAdapter;
import com.aptitude.education.e2buddy.DisplayAnswer.LeaderBoardAdapter;
import com.aptitude.education.e2buddy.DisplayAnswer.LeaderBoardData;
import com.aptitude.education.e2buddy.DisplayAnswer.ResultActivity;
import com.aptitude.education.e2buddy.Intro.CheckInternet;
import com.aptitude.education.e2buddy.R;
import com.aptitude.education.e2buddy.ViewData.CreditView;
import com.aptitude.education.e2buddy.ViewData.InsertAnswer;
import com.aptitude.education.e2buddy.ViewData.InsertTotalScoreData;
import com.aptitude.education.e2buddy.ViewData.QuestionView;
import com.aptitude.education.e2buddy.ViewData.TimeData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class QuizQuestionTimeBasedActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences,sharedPreferences1;
    String quizid, quizname;
    AdapterForTimeBasedQuiz questionAdapter;
    ViewPager viewPager;
    private TextView[] mdots;
    LinearLayout linearLayout;
    private int mCurrentPage;
    String questionid, question, option1, option2,option3,option4, answer,userid,quizdate,description,userids,userids1,quizids,username;
    TextView tiemr;
    int onlineplayecount = 1534;
    int[] layout;
    TextView onlinuser;
    TextView tvcancelquiz;
    DatabaseReference databaseReference;
    DatabaseReference databaseReference1;
    FirebaseDatabase database;
    DatabaseReference dataref;
    DatabaseReference reference;
    DatabaseReference totalchild;
    DatabaseReference totalque;
    DatabaseReference databaseReference2;
    DatabaseReference databaseReference3;
    DatabaseReference databaseReference4;
    DatabaseReference databaseReference5;
    DatabaseReference databaseReference6;
    DatabaseReference reference7,reference3,reference2,reference4;
    DatabaseReference reference8;
    List<LeaderBoardData> leaderBoardDataList;
    List<LeaderBoardData> dataList;
    CountDownTimer countDownTimer;
    Button next;
    private List<QuestionView> questionViewList;
    DatabaseReference reference5;
    AlertDialog alertDialog;
    long timeleft;
    List<TimeData> timeleftlist;
    ProgressDialog progressDialog;
    String date,q_id,corr_ans;
    int scores = 0;
    List<LeaderBoardData> list;
    LeaderBoardAdapter leaderBoardAdapter;
    int scoreCount = 0;
    String value;
    int count = 0;
    int count1 = 0;
    DatabaseReference databaseRef,databaseRef1;
    MediaPlayer player,player1;
    FirebaseAuth auth;
    private long timeCountInMilliSeconds = 1 * 60000;

    private enum TimerStatus {
        STARTED,
        STOPPED
    }
    private QuizQuestionTimeBasedActivity.TimerStatus timerStatus = QuizQuestionTimeBasedActivity.TimerStatus.STOPPED;

    private ProgressBar progressBarCircle;
    private TextView textViewTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_question_time_based);
        progressBarCircle = (ProgressBar) findViewById(R.id.progressBarCircle);
        textViewTime = (TextView) findViewById(R.id.textViewTime);
        linearLayout = findViewById(R.id.linear);
        viewPager = findViewById(R.id.slidequestion);
        next = findViewById(R.id.bnext);
        tvcancelquiz = findViewById(R.id.tvcancel);
        onlinuser = findViewById(R.id.onlineuser);

        auth = FirebaseAuth.getInstance();
        final FirebaseUser user = auth.getCurrentUser();
        userid = user.getUid();


        quizdate = getIntent().getStringExtra("quiz_date");

        value  = getIntent().getStringExtra("curent_date");

        layout = new int[9];

        questionViewList = new ArrayList<>();
        timeleftlist = new ArrayList<TimeData>();
        leaderBoardDataList = new ArrayList<>();
        list = new ArrayList<>();
        dataList = new ArrayList<>();
        leaderBoardAdapter = new LeaderBoardAdapter(getApplicationContext(), list, userid,tvcancelquiz);



        viewPager.addOnPageChangeListener(viewListener);
        //  viewPager.beginFakeDrag();

        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();

        databaseReference1 = FirebaseDatabase.getInstance().getReference();
        databaseReference2 = FirebaseDatabase.getInstance().getReference();
        databaseReference3 = FirebaseDatabase.getInstance().getReference("daily_user_answer");
        databaseReference4 = FirebaseDatabase.getInstance().getReference();
        databaseReference5 = FirebaseDatabase.getInstance().getReference();
        databaseReference6 = FirebaseDatabase.getInstance().getReference();
        reference5 = FirebaseDatabase.getInstance().getReference();
        reference7 = FirebaseDatabase.getInstance().getReference();
        reference8 = FirebaseDatabase.getInstance().getReference();
        reference3 = FirebaseDatabase.getInstance().getReference();
        reference2 = FirebaseDatabase.getInstance().getReference("daily_user_quiz_rank");
        reference4 = FirebaseDatabase.getInstance().getReference();
        databaseRef = FirebaseDatabase.getInstance().getReference();
        databaseRef1 = FirebaseDatabase.getInstance().getReference();

        totalchild = FirebaseDatabase.getInstance().getReference();
        totalque = FirebaseDatabase.getInstance().getReference();
        dataref = FirebaseDatabase.getInstance().getReference();
        reference = FirebaseDatabase.getInstance().getReference();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM");
        date = sdf.format(new Date());

        player = MediaPlayer.create(getApplicationContext(), R.raw.track1);
        player1 = MediaPlayer.create(getApplicationContext(), R.raw.track4);


        progressDialog = new ProgressDialog(QuizQuestionTimeBasedActivity.this);
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Data Loading...");
        progressDialog.show();


        questionAdapter = new AdapterForTimeBasedQuiz(questionViewList, getApplicationContext(), value, userid,quizdate);

        databaseReference6.child("daily_Question").child(quizdate).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                    q_id = dataSnapshot1.getKey();

                    //    Toast.makeText(getApplicationContext(), ""+q_id,Toast.LENGTH_SHORT).show();

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
                startStop();

            }
        };

        Handler pdCanceller = new Handler();
        pdCanceller.postDelayed(progressRunnable, 3000);


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.gc();
                if(countDownTimer!=null){
                    countDownTimer.cancel();
                }

                if(player.isPlaying() || player1.isPlaying()) {
                    player.stop();
                    player.reset();
                    player.release();
                    player = null;
                    player = MediaPlayer.create(QuizQuestionTimeBasedActivity.this, R.raw.track1);
                    player1.stop();
                    player1.reset();
                    player1.release();
                    player1 = null;
                    player1 = MediaPlayer.create(QuizQuestionTimeBasedActivity.this, R.raw.track4);
                }

                startStop();
                //  Toast.makeText(getApplicationContext(), "time left "+ timeleft/1000, Toast.LENGTH_SHORT).show();

                timeleftlist.add(new TimeData(timeleft/1000));

                for (int i =0;i<timeleftlist.size();i++){

                    TimeData timeData = timeleftlist.get(i);
                    //    Toast.makeText(getApplicationContext(), "all time : "+timeData.getTimelefts(), Toast.LENGTH_SHORT).show();

                    try {
                        QuestionView questionView = questionViewList.get(i);

                        InsertAnswer insertAnswer = new InsertAnswer(timeData.getTimelefts());

                        try {

                            databaseReference2.child("daily_user_answer").child(userid).child(quizdate).child(value).child(questionView.getQuestionid()).child("timeleft").setValue(insertAnswer.getTimeleft());


                        }
                        catch (Exception e){

                            e.printStackTrace();
                        }

                    }
                    catch (Exception e) {
                        e.printStackTrace();


                    }
                    //  Toast.makeText(getApplicationContext(), "question id : "+ questionView.getQuestionid(), Toast.LENGTH_SHORT).show();

                }

                if (mCurrentPage<layout.length) {
                    viewPager.setCurrentItem(mCurrentPage + 1);
                }
                else
                {
                    //  tiemr.setText(""+timeleft/100);
                    countDownTimer.cancel();
                    if(player.isPlaying() || player1.isPlaying()) {
                        player.stop();
                        player.reset();
                        player.release();
                        player = null;
                        player = MediaPlayer.create(QuizQuestionTimeBasedActivity.this, R.raw.track1);
                        player1.stop();
                        player1.reset();
                        player1.release();
                        player1 = null;
                        player1 = MediaPlayer.create(QuizQuestionTimeBasedActivity.this, R.raw.track4);
                    }


                    new android.support.v7.app.AlertDialog.Builder(QuizQuestionTimeBasedActivity.this)
                            .setTitle("Submit...")
                            .setMessage("Click Submit button to see your Result")
                            .setCancelable(false)
                            .setPositiveButton("Submit", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface arg0, int arg1) {


                                    System.gc();

                                    databaseReference6.child("daily_Question").child(quizdate).addValueEventListener(new ValueEventListener() {
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


                                    showLoader();

                                }
                            }).create().show();

                }
            }
        });



        tvcancelquiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!isFinishing()) {
                    new AlertDialog.Builder(QuizQuestionTimeBasedActivity.this)
                            .setTitle("Alert!!")
                            .setMessage("Are you sure you want to Exit the Quiz?")

                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    player.stop();
                                    player1.stop();

                                    reference5.child("daily_user_answer").child(userid).child(quizdate).child(value).removeValue();
                                    Intent i = new Intent(getApplicationContext(), HomeNevActivity.class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(i);
                                    QuizQuestionTimeBasedActivity.this.finish();                        }
                            })

                            // A null listener allows the button to dismiss the dialog and take no further action.
                            .setNegativeButton("NO", null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }

            }
        });

        getOnlinePlayers();

    }

    private void getOnlinePlayers(){
        reference4.child("online_users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                int count = (int) dataSnapshot.getChildrenCount();
                onlineplayecount = onlineplayecount +count;
                // Toast.makeText(getApplicationContext(),""+onlineplayecount,Toast.LENGTH_SHORT).show();
                onlinuser.setText(""+onlineplayecount);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void startStop() {

        int time = 1;

        timeCountInMilliSeconds = 16000;

        progressBarCircle.setMax((int) timeCountInMilliSeconds / 1000);
        progressBarCircle.setProgress((int) timeCountInMilliSeconds / 1000);

        countDownTimer = new CountDownTimer(timeCountInMilliSeconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {


                textViewTime.setText(""+millisUntilFinished/1000);
                progressBarCircle.setProgress((int) (millisUntilFinished / 1000));

                timeleft = millisUntilFinished;

                player.start();

                if (millisUntilFinished/1000 == 5){
                    player.stop();
                    player1.start();
                }


            }

            @Override
            public void onFinish() {


                alertDialog = new AlertDialog.Builder(QuizQuestionTimeBasedActivity.this).create();
                alertDialog.setCancelable(false);
                alertDialog.setTitle("Alert");
                alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                alertDialog.setMessage("You ran out of Time, Go to next Question");

                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Next Question", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {


                        System.gc();
                        if (countDownTimer != null) {
                            countDownTimer.cancel();
                        }

                        player.stop();
                        player.reset();
                        player.release();
                        player = null;
                        player = MediaPlayer.create(QuizQuestionTimeBasedActivity.this, R.raw.track1);
                        player1.stop();
                        player1.reset();
                        player1.release();
                        player1 = null;
                        player1 = MediaPlayer.create(QuizQuestionTimeBasedActivity.this, R.raw.track4);

                        if (mCurrentPage < layout.length) {
                            startStop();
                            timeleftlist.add(new TimeData(timeleft / 1000));

                            viewPager.setCurrentItem(mCurrentPage + 1);
                        } else {
                            //  tiemr.setText(""+timeleft/100);

                            countDownTimer.cancel();
                            countDownTimer = null;

                            player.stop();
                            player.reset();
                            player.release();
                            player = null;
                            player1.stop();
                            player1.reset();
                            player1.release();
                            player1 = null;


                            new AlertDialog.Builder(QuizQuestionTimeBasedActivity.this)
                                    .setTitle("Submit...")
                                    .setMessage("Click Submit button to see your Result")
                                    .setCancelable(false)
                                    .setPositiveButton("Submit", new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface arg0, int arg1) {

                                            databaseReference6.child("daily_Question").child(quizdate).addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {

                                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                                                        q_id = dataSnapshot1.getKey();

                                                        getUserAnswer(q_id);
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });

                                            countDownTimer.cancel();
                                            showLoader();

                                        }
                                    }).create().show();
                        }

                    }
                });
                if (!isFinishing()){
                    alertDialog.show();
                }

                progressBarCircle.setMax((int) timeCountInMilliSeconds / 1000);
                progressBarCircle.setProgress((int) timeCountInMilliSeconds / 1000);

            }

        }.start();




    }

    private void getQuizQuestions(String q_id) {


        databaseReference.child("questions").child(q_id).addValueEventListener(new ValueEventListener() {
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

                viewPager.setAdapter(questionAdapter);
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    private void getUserAnswer(final String q_id) {
        databaseReference.child("daily_user_answer").child(userid).child(quizdate).child(value).child(q_id).addValueEventListener(new ValueEventListener() {
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

        databaseReference.child("questions").child(questionId).addValueEventListener(new ValueEventListener() {
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


                    progressDialog.dismiss();


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

    private void addDotsIndicator(int position){
        mdots = new TextView[10];

        for (int i =0;i<mdots.length; i++){
            mdots[i] = new TextView(this );
            mdots[i].setText(Html.fromHtml("&#8226;"));
            mdots[i].setTextSize(35);
        }

        if (mdots.length>0){
            mdots[position].setTextColor(getResources().getColor(R.color.colorPrimary));
        }


    }
    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {


            addDotsIndicator(position);

            mCurrentPage = position;
            if (position==0){
                next.setEnabled(true);
                next.setText("NEXT");
            }else if (position==mdots.length-1){
                next.setEnabled(true);
                next.setText("SUBMIT");
            }
            else {
                next.setEnabled(true);
                next.setText("NEXT");
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode==KeyEvent.KEYCODE_BACK)
            Toast.makeText(getApplicationContext(), "take quiz",
                    Toast.LENGTH_LONG).show();

        return false;
    }

    private void showLoader() {
        if (!isFinishing()){
            final ProgressDialog dialog = new ProgressDialog(QuizQuestionTimeBasedActivity.this);
            dialog.setTitle("Please wait...");
            dialog.setMessage("Generating Your Result");
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.show();

            long delayInMillis = 3500;
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    dialog.dismiss();

                    insertTotalQuizScore();

                    Intent intent = new Intent(QuizQuestionTimeBasedActivity.this, ResultActivity.class);
                    intent.putExtra("quiz_date",quizdate);
                    intent.putExtra("curent_date", value);
                    intent.putExtra("userid", userid);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    QuizQuestionTimeBasedActivity.this.finish();


                }
            }, delayInMillis);

        }
    }




    private void insertTotalQuizScore(){

        reference5.child("daily_user_credit").child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
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

}



