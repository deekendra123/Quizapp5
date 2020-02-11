package com.aptitude.education.e2buddy.One_on_One_Quiz_Challenge;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Handler;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aptitude.education.e2buddy.Intro.CheckInternet;
import com.aptitude.education.e2buddy.R;
import com.aptitude.education.e2buddy.ViewData.InsertAnswer;
import com.aptitude.education.e2buddy.ViewData.QuestionIdData;
import com.aptitude.education.e2buddy.ViewData.QuestionView;
import com.aptitude.education.e2buddy.ViewData.TimeData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class QuestionForQuizChallengeActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    String quizid, quizname, receiver_id;
    AdapterForQuizChallenge questionAdapter;
    ViewPager viewPager;
    private TextView[] mdots;
    LinearLayout linearLayout;
    private int mCurrentPage;
    String questionid, question, option1, option2,option3,option4, answer,userid,quizdate,description,username;
    TextView tiemr,tvreceiver,tvsender;
    int[] layout;
    List<String> list;
    DatabaseReference databaseReference;
    DatabaseReference databaseReference1;
    DatabaseReference dataref;
    DatabaseReference reference;
    DatabaseReference totalchild;
    DatabaseReference totalque;
    DatabaseReference databaseReference2;
    DatabaseReference databaseReference3;
    DatabaseReference databaseReference4;
    DatabaseReference databaseReference5;
    DatabaseReference databaseReference6;
    DatabaseReference databaseReference7;
    DatabaseReference databaseReference10;
    DatabaseReference databaseReference11;
    DatabaseReference databaseReference12;
    String notification_id;
    DatabaseReference databaseReference8;
    DatabaseReference databaseReference9;
    SharedPreferences sharedPreferences2;
    CountDownTimer countDownTimer;
    Button next;
    private List<QuestionView> questionViewList;
    AlertDialog alertDialog;

    long timeleft;
    String q_id, receiver_time_left, rec_name;
    List<TimeData> timeleftlist;
    List<QuestionIdData> stringList;
    int sender_count =0;
    int receiver_count=0;
    Button bt1;
    String sender_answer, receiver_answer;
    ProgressDialog progressDialog;
    LinearLayout layouts;
    SharedPreferences sharedPref;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    private long timeCountInMilliSeconds = 1 * 60000;
    MediaPlayer player;

    LinearLayout layoutforsender;
    private ProgressBar progressBarCircle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_for_quiz_challenge);
        linearLayout = findViewById(R.id.linear);
        viewPager = findViewById(R.id.slidequestion);
        next = findViewById(R.id.bnext);
        tiemr = findViewById(R.id.timer);
        progressBarCircle = findViewById(R.id.progressBarCircle);

        layouts = findViewById(R.id.dots);
        layoutforsender = findViewById(R.id.user1);
        tvreceiver = findViewById(R.id.tvreceiver);
        tvsender = findViewById(R.id.tvsender);

        CheckInternet checkInternet = new CheckInternet(getApplicationContext());
        checkInternet.checkConnection();

        Typeface type = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
        tvreceiver.setTypeface(type);
        tvsender.setTypeface(type);
        next.setTypeface(type);

        sharedPreferences = getSharedPreferences("quizpref", MODE_PRIVATE);
        quizid = sharedPreferences.getString("quizid", "");
        quizname = sharedPreferences.getString("quizname", "");
        userid = sharedPreferences.getString("userid", "");
        quizdate = sharedPreferences.getString("quizdate","");
        username = sharedPreferences.getString("username","");

        tvsender.setText(""+username);

        sharedPref = getSharedPreferences("receiver_info", MODE_PRIVATE);

        notification_id = sharedPref.getString("notification_ids","");
        receiver_id = sharedPref.getString("receiver_id","");
        rec_name = sharedPref.getString("receiver_name","");


        // Toast.makeText(getApplicationContext(), "not id "+ not_id + "\n" + "rec id "+rec_id + "\n"+  "rec name "+ rec_name,Toast.LENGTH_SHORT).show();
        pref = getSharedPreferences("score_pref", MODE_PRIVATE);

        editor = pref.edit();

        layout = new int[9];

        questionViewList = new ArrayList<>();
        timeleftlist = new ArrayList<TimeData>();

        stringList = new ArrayList<>();
        addDotsIndicator(0);
        viewPager.addOnPageChangeListener(viewListener);

        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });



        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference1 = FirebaseDatabase.getInstance().getReference();
        databaseReference2 = FirebaseDatabase.getInstance().getReference();
        databaseReference3 = FirebaseDatabase.getInstance().getReference("user_answer");
        databaseReference4 = FirebaseDatabase.getInstance().getReference();
        databaseReference5 = FirebaseDatabase.getInstance().getReference();
        databaseReference6 = FirebaseDatabase.getInstance().getReference();
        databaseReference7 = FirebaseDatabase.getInstance().getReference();
        databaseReference8 = FirebaseDatabase.getInstance().getReference();
        databaseReference9 = FirebaseDatabase.getInstance().getReference();
        databaseReference10 = FirebaseDatabase.getInstance().getReference();
        databaseReference11 = FirebaseDatabase.getInstance().getReference();
        databaseReference12 = FirebaseDatabase.getInstance().getReference();

        totalchild = FirebaseDatabase.getInstance().getReference();
        totalque = FirebaseDatabase.getInstance().getReference();
        dataref = FirebaseDatabase.getInstance().getReference();
        reference = FirebaseDatabase.getInstance().getReference();

        list = new ArrayList<>();

        tvreceiver.setText(""+rec_name);

        player = MediaPlayer.create(getApplicationContext(), R.raw.track1);

        questionAdapter = new AdapterForQuizChallenge(questionViewList, getApplicationContext(), notification_id);


        databaseReference6.child("1V1_Question_id").child(notification_id).addValueEventListener(new ValueEventListener() {
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

        progressDialog = new ProgressDialog(QuestionForQuizChallengeActivity.this);
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Data Loading...");
        progressDialog.show();

        Runnable progressRunnable = new Runnable() {

            @Override
            public void run() {
                progressDialog.cancel();
                startStop();

            }
        };

        Handler pdCanceller = new Handler();
        pdCanceller.postDelayed(progressRunnable, 3000);

        // timer();

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(countDownTimer!=null){
                    countDownTimer.cancel();
                }
                player.release();
                player = null;
                player = MediaPlayer.create(getApplicationContext(), R.raw.track1);

                startStop();
                //timer();

                //  Toast.makeText(getApplicationContext(), "time left "+ timeleft/1000, Toast.LENGTH_SHORT).show();

                timeleftlist.add(new TimeData(timeleft/1000));

                for (int i =0;i<timeleftlist.size();i++){

                    TimeData timeData = timeleftlist.get(i);

                    try {
                        QuestionView questionView = questionViewList.get(i);

                        InsertAnswer insertAnswer = new InsertAnswer(timeData.getTimelefts());

                        try {

                            databaseReference2.child("1V1-user_answer").child(notification_id).child(userid).child(questionView.getQuestionid()).child("timeleft").setValue(insertAnswer.getTimeleft());

                        }
                        catch (Exception e){

                            e.printStackTrace();
                        }

                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (mCurrentPage<layout.length) {
                    viewPager.setCurrentItem(mCurrentPage + 1);
                }
                else
                {
                    countDownTimer.cancel();
                    new AlertDialog.Builder(QuestionForQuizChallengeActivity.this)
                            .setTitle("Really Exit?")
                            .setMessage("Are you sure you want to submit the quiz?")
                            .setCancelable(false)
                            .setPositiveButton("Submit", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface arg0, int arg1) {

                                    databaseReference12.child("1V1-user_answer").child(notification_id).child(userid).child("status").setValue("yes");


                                    getQuestionCount(notification_id,receiver_id);

                                }
                            }).create().show();

                }
            }
        });

        getQuestionId();

    }

    private void getQuestionId(){

        databaseReference6.child("1V1_Question_id").child(notification_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {


                        q_id = dataSnapshot1.getKey();

                        layouts.removeAllViews();

                        getReceiverAnswerResponse1(q_id);
                        getSenderAnswerResponse(q_id);

                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
    private void getQuizQuestions(String q_id){

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

//                Toast.makeText(getApplicationContext(), "" + questionid +"\n"+ question +"\n"+ option1 + "\n"+option2 +"\n"+ option3 +"\n"+ option4, Toast.LENGTH_SHORT).show();

                Log.e("dkmsg",questionid +"\n"+ question +"\n"+ option1 + "\n"+option2 +"\n"+ option3 +"\n"+ option4);
                questionViewList.add(new QuestionView(questionid, question, option1, option2, option3, option4, answer, "null"));

                viewPager.setAdapter(questionAdapter);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void startStop() {

        timeCountInMilliSeconds = 11000;

        progressBarCircle.setMax((int) timeCountInMilliSeconds / 1000);
        progressBarCircle.setProgress((int) timeCountInMilliSeconds / 1000);

        countDownTimer = new CountDownTimer(timeCountInMilliSeconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                tiemr.setText(""+millisUntilFinished/1000);
                progressBarCircle.setProgress((int) (millisUntilFinished / 1000));

                timeleft = millisUntilFinished;

                player.start();

            }

            @Override
            public void onFinish() {

                //player.stop();
                alertDialog = new AlertDialog.Builder(QuestionForQuizChallengeActivity.this).create();
                alertDialog.setCancelable(false);
                alertDialog.setTitle("Alert");
                alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                alertDialog.setMessage("You ran out of Time, Go to next Question");

                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Next Question",
                        new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                     //   databaseReference2.child("1V1-user_answer").child(notification_id).child(userid).child(questionView.getQuestionid()).child("timeleft").setValue(insertAnswer.getTimeleft());


                        if (countDownTimer != null) {
                            countDownTimer.cancel();
                        }

                        player.release();
                        player = null;
                        player = MediaPlayer.create(getApplicationContext(), R.raw.track1);

                        if (mCurrentPage < layout.length) {
                            startStop();

                            timeleftlist.add(new TimeData(timeleft/1000));

                            for (int i =0;i<timeleftlist.size();i++){

                                TimeData timeData = timeleftlist.get(i);
                                //    Toast.makeText(getApplicationContext(), "all time : "+timeData.getTimelefts(), Toast.LENGTH_SHORT).show();

                                try {
                                    QuestionView questionView = questionViewList.get(i);

                                    InsertAnswer insertAnswer = new InsertAnswer(timeData.getTimelefts());

                                    try {

                                        databaseReference2.child("1V1-user_answer").child(notification_id).child(userid).child(questionView.getQuestionid()).child("timeleft").setValue(insertAnswer.getTimeleft());

                                    }
                                    catch (Exception e){

                                        e.printStackTrace();
                                    }

                                }
                                catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            viewPager.setCurrentItem(mCurrentPage + 1);
                        } else {


                            player.release();
                            player = null;
                            player = MediaPlayer.create(getApplicationContext(), R.raw.track1);

                            new AlertDialog.Builder(QuestionForQuizChallengeActivity.this)
                                    .setTitle("Submit...")
                                    .setMessage("Click Submit button to see your Result")
                                    .setCancelable(false)
                                    .setPositiveButton("Submit", new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface arg0, int arg1) {

                                            databaseReference12.child("1V1-user_answer").child(notification_id).child(userid).child("status").setValue("yes");

                                            getQuestionCount(notification_id,receiver_id);
                                            countDownTimer.cancel();

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


    private void getReceiverAnswerResponse1(final String  q_id){

        databaseReference7.child("1V1-user_answer").child(notification_id).child(receiver_id).child(q_id).addValueEventListener(new ValueEventListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    try {

                        receiver_time_left = dataSnapshot.child("timeleft").getValue(). toString();

                           questionAdapter.notifyDataSetChanged();

                        if (!receiver_time_left.equals("0")){

                            bt1 = new Button(QuestionForQuizChallengeActivity.this);
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(25,25);
                            params.setMargins(5, 5, 5, 5);
                            params.gravity= Gravity.CENTER;
                            bt1.setLayoutParams(params);
                            bt1.setText("");
                            bt1.setId(1);

                            bt1.setBackgroundResource(R.drawable.active_dot);

                            layouts.addView(bt1);

                        }


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

    private void getSenderAnswerResponse(String q_id){


        databaseReference10.child("1V1-user_answer").child(notification_id).child(userid).child(q_id).addValueEventListener(new ValueEventListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    try {

                        receiver_time_left = dataSnapshot.child("timeleft").getValue().toString();
                        //Toast.makeText(getApplicationContext(), " receiver timeleft "+ receiver_time_left,Toast.LENGTH_SHORT).show();

                        questionAdapter.notifyDataSetChanged();

                        if (!receiver_time_left.equals("0")){

                            bt1 = new Button(QuestionForQuizChallengeActivity.this);
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(25,25);
                            params.setMargins(5, 5, 5, 5);
                            params.gravity= Gravity.CENTER;
                            bt1.setLayoutParams(params);
                            bt1.setText("");
                            bt1.setId(2);

                            bt1.setBackgroundResource(R.drawable.active_dot);

                            layoutforsender.addView(bt1);

                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void getReceiverAnswerResponse(final int position){

        final QuestionView questionView = questionViewList.get(position);

        databaseReference7.child("1V1-user_answer").child(notification_id).child(userid).child(questionView.getQuestionid()).addValueEventListener(new ValueEventListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    try {

                        sender_answer = dataSnapshot.child("useranswer").getValue().toString();

                        //Toast.makeText(getApplicationContext(), "sender answer "+ sender_answer,Toast.LENGTH_SHORT).show();

                        if (sender_answer.equals(questionView.getAnswer())){
                            sender_count++;
                            //  Toast.makeText(getApplicationContext(), "score "+ sender_count,Toast.LENGTH_SHORT).show();


                            editor.putString("sender_score", String.valueOf(sender_count));



                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        databaseReference11.child("1V1-user_answer").child(notification_id).child(receiver_id).child(questionView.getQuestionid()).addValueEventListener(new ValueEventListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    try {

                        receiver_answer = dataSnapshot.child("useranswer").getValue().toString();

                        //Toast.makeText(getApplicationContext(), "sender answer "+ sender_answer,Toast.LENGTH_SHORT).show();

                        if (receiver_answer.equals(questionView.getAnswer())){
                            receiver_count++;
                            //   Toast.makeText(getApplicationContext(), "score "+ receiver_count,Toast.LENGTH_SHORT).show();

                            editor.putString("receiver_score", String.valueOf(receiver_count));
                            editor.commit();


                        }

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

    private void addDotsIndicator(int position){
        mdots = new TextView[10];

        for (int i =0;i<mdots.length; i++){

            mdots[i] = new TextView(this );
            mdots[i].setText(Html.fromHtml("&#8226;"));
            mdots[i].setTextSize(35);
            mdots[i].setTextColor(getResources().getColor(R.color.dot_inactive));


        }

        if (mdots.length>0){
            mdots[position].setTextColor(getResources().getColor(R.color.colorPrimary));
        }
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {

        Boolean first = true;
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            if (first && positionOffset ==0 && positionOffsetPixels ==0 ){
                onPageSelected(0);
                first=false;
            }

        }

        @Override
        public void onPageSelected(int position) {


            //  addDotsIndicator(position);

            getReceiverAnswerResponse(position);

            //   getQuestionId();

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

    private void getQuestionCount(String notification_id, String userid){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        final ProgressDialog progressDialog;

        progressDialog = new ProgressDialog(QuestionForQuizChallengeActivity.this);
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Please Wait" + "\n"+ "Generating Result");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);
        progressDialog.setMax(100);
        progressDialog.show();

        databaseReference.child("1V1-user_answer").child(notification_id).child(userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                try {
                    String status = dataSnapshot.child("status").getValue().toString();
                    // Toast.makeText(getApplicationContext(), "count "+ count,Toast.LENGTH_SHORT).show();

                    if (status.equals("yes")){
                        progressDialog.dismiss();
                        Intent intent = new Intent(getApplicationContext(), One_On_One_ResultActivity.class);
                        startActivity(intent);
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

}


