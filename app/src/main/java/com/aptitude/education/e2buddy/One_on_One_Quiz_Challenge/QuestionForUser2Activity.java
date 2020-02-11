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

public class QuestionForUser2Activity extends AppCompatActivity {


    SharedPreferences sharedPreferences;
    String quizid, quizname;
    AdapterForQuizChallenge questionAdapter;
    ViewPager viewPager;
    private TextView[] mdots;
    LinearLayout linearLayout;
    private int mCurrentPage;
    String questionid, question, option1, option2,option3,option4, answer,userid,quizdate,description,username;
    TextView tiemr,tvreceiver,tvsender;
    int[] layout;
    private static final String FORMAT = "%02d:%02d";

    List<String> list;
    DatabaseReference databaseReference;
    DatabaseReference databaseReference1;
    FirebaseDatabase database;
    DatabaseReference dataref;
    DatabaseReference reference;
    DatabaseReference databaseReference12;
    DatabaseReference totalchild;
    DatabaseReference totalque;
    DatabaseReference databaseReference2;
    DatabaseReference databaseReference3;
    DatabaseReference databaseReference4;
    DatabaseReference databaseReference5;
    DatabaseReference databaseReference6;
    DatabaseReference databaseReference7;

    String notification_id, sender_id,sender_name;
    DatabaseReference databaseReference8;


    DatabaseReference databaseReference9;
    DatabaseReference databaseReference10;

    SharedPreferences sharedPreferences2;

    ArrayList<String> arrayList;


    CountDownTimer countDownTimer;
    Button next;
    private List<QuestionView> questionViewList;
    private List<InsertAnswer> insertAnswers;
    long timeleft;
    String q_id;
    List<TimeData> timeleftlist;
    List<QuestionIdData> stringList;
    LinearLayout layouts;
    LinearLayout layoutforuser1;
    Button bt1;
    String  receiver_time_left;
    MediaPlayer player;

    private long timeCountInMilliSeconds = 1 * 60000;
    private ProgressBar progressBarCircle;
    ProgressDialog progressDialog;
    AlertDialog alertDialog;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_for_user2);

        layouts = findViewById(R.id.dots);
        layoutforuser1 = findViewById(R.id.user1);
        tvreceiver = findViewById(R.id.tvreceiver);
        tvsender = findViewById(R.id.tvsender);

        linearLayout = findViewById(R.id.linear);
        viewPager = findViewById(R.id.slidequestion);
        next = findViewById(R.id.bnext);
        tiemr = findViewById(R.id.timer);
        progressBarCircle = findViewById(R.id.progressBarCircle);

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

        tvreceiver.setText(""+username);


        layout = new int[9];

        questionViewList = new ArrayList<>();
        timeleftlist = new ArrayList<TimeData>();
        insertAnswers = new ArrayList<>();

        stringList = new ArrayList<>();
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
        databaseReference12 = FirebaseDatabase.getInstance().getReference();

        totalchild = FirebaseDatabase.getInstance().getReference();
        totalque = FirebaseDatabase.getInstance().getReference();
        dataref = FirebaseDatabase.getInstance().getReference();
        reference = FirebaseDatabase.getInstance().getReference();

        list = new ArrayList<>();

        arrayList = new ArrayList<>();

        sharedPref = getSharedPreferences("sender_info", MODE_PRIVATE);

        notification_id = sharedPref.getString("not_id","");
        sender_id = sharedPref.getString("s_id","");
        sender_name = sharedPref.getString("sender_name","");

        tvsender.setText(""+sender_name);
        player = MediaPlayer.create(getApplicationContext(), R.raw.track1);



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

        progressDialog = new ProgressDialog(QuestionForUser2Activity.this);
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
                    //  Toast.makeText(getApplicationContext(), "question id : "+ questionView.getQuestionid(), Toast.LENGTH_SHORT).show();

                }

                if (mCurrentPage<layout.length) {
                    viewPager.setCurrentItem(mCurrentPage + 1);
                }
                else
                {
                    countDownTimer.cancel();
                    new AlertDialog.Builder(QuestionForUser2Activity.this)
                            .setTitle("Really Exit?")
                            .setMessage("Are you sure you want to submit the quiz?")
                            .setCancelable(false)
                            .setPositiveButton("Submit", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface arg0, int arg1) {


                                    reference.child("user_credit_score").child(userid).child(quizid).removeValue();

                                    databaseReference12.child("1V1-user_answer").child(notification_id).child(userid).child("status").setValue("yes");
                                    getQuestionCount(notification_id,sender_id);

                                }
                            }).create().show();

                }
            }
        });

        getQuestionId();

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
                alertDialog = new AlertDialog.Builder(QuestionForUser2Activity.this).create();
                alertDialog.setCancelable(false);
                alertDialog.setTitle("Alert");
                alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                alertDialog.setMessage("You ran out of Time, Go to next Question");

                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Next Question", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

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
                            //  tiemr.setText(""+timeleft/100);

                            player.release();
                            player = null;
                            player = MediaPlayer.create(getApplicationContext(), R.raw.track1);

                            new AlertDialog.Builder(QuestionForUser2Activity.this)
                                    .setTitle("Submit...")
                                    .setMessage("Click Submit button to see your Result")
                                    .setCancelable(false)

                                    .setPositiveButton("Submit", new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface arg0, int arg1) {

                                            /*Intent intent = new Intent(getApplicationContext(), ResultForReceiverActivity.class);
                                            startActivity(intent);
*/                                    databaseReference12.child("1V1-user_answer").child(notification_id).child(userid).child("status").setValue("yes");

                                            getQuestionCount(notification_id,sender_id);
                                            countDownTimer.cancel();
                                            // showLoader();
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

    private void getReceiverAnswerResponse1(final String  q_id){

//        final QuestionView questionView = questionViewList.get(position);

        // Toast.makeText(getApplicationContext(),"qid "+ q_id,Toast.LENGTH_SHORT).show();

        databaseReference7.child("1V1-user_answer").child(notification_id).child(sender_id).child(q_id).addValueEventListener(new ValueEventListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    try {

                        receiver_time_left = dataSnapshot.child("timeleft").getValue(). toString();

                        //Toast.makeText(getApplicationContext(), " receiver timeleft "+ receiver_time_left,Toast.LENGTH_SHORT).show();

                        //  Toast.makeText(getApplicationContext(),"qid "+ q_id,Toast.LENGTH_SHORT).show();

                        questionAdapter.notifyDataSetChanged();

                        if (!receiver_time_left.equals("0")){

                            bt1 = new Button(QuestionForUser2Activity.this);
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
                        // Toast.makeText(getApplicationContext(), " receiver timeleft "+ receiver_time_left,Toast.LENGTH_SHORT).show();
                        questionAdapter.notifyDataSetChanged();

                        if (!receiver_time_left.equals("0")){

                            bt1 = new Button(QuestionForUser2Activity.this);
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(25,25);
                            params.setMargins(5, 5, 5, 5);
                            params.gravity= Gravity.CENTER;
                            bt1.setLayoutParams(params);
                            bt1.setText("");
                            bt1.setId(2);

                            bt1.setBackgroundResource(R.drawable.active_dot);

                            layoutforuser1.addView(bt1);

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


    private void getQuizQuestions(String q_id){


        questionAdapter = new AdapterForQuizChallenge(questionViewList, getApplicationContext(), notification_id);

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

                // Toast.makeText(getApplicationContext(), "" + questionid +"\n"+ question +"\n"+ option1 + "\n"+option2 +"\n"+ option3 +"\n"+ option4, Toast.LENGTH_SHORT).show();

                Log.e("dkmsg",questionid +"\n"+ question +"\n"+ option1 + "\n"+option2 +"\n"+ option3 +"\n"+ option4);
                questionViewList.add(new QuestionView(questionid, question, option1, option2, option3, option4, answer, "null"));


                viewPager.setAdapter(questionAdapter);

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

    private void getQuestionCount(String notification_id, String userid){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        final ProgressDialog progressDialog;

        progressDialog = new ProgressDialog(QuestionForUser2Activity.this);
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
                    // Toast.makeText(getApplicationContext(), "count " + count,Toast.LENGTH_SHORT).show();

                    if (status.equals("yes")){
                        progressDialog.dismiss();
                        Intent intent = new Intent(getApplicationContext(), ResultForReceiverActivity.class);
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


