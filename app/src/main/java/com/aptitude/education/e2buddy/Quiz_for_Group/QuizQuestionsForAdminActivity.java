package com.aptitude.education.e2buddy.Quiz_for_Group;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aptitude.education.e2buddy.R;
import com.aptitude.education.e2buddy.ViewData.InsertAnswer;
import com.aptitude.education.e2buddy.ViewData.InsertGroupScore;
import com.aptitude.education.e2buddy.ViewData.QuestionIdData;
import com.aptitude.education.e2buddy.ViewData.QuestionView;
import com.aptitude.education.e2buddy.ViewData.TimeData;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class QuizQuestionsForAdminActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    String quizid, quizname, noti_id, receiver_id;
    AdapterForAdminQuizQuestions questionAdapter;
    ViewPager viewPager;
    private TextView[] mdots;
    LinearLayout linearLayout;
    private int mCurrentPage;
    String questionid, question, option1, option2,option3,option4, answer,userid,quizdate,description,student_name;
    TextView tiemr;
    int[] layout;
    private static final String FORMAT = "%02d:%02d";
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
    String notification_id;
    DatabaseReference databaseReference8;
    DatabaseReference databaseReference9;
    DatabaseReference databaseReference12;
    DatabaseReference databaseReference13;
    DatabaseReference databaseReference14;
    DatabaseReference databaseReference15;

    CountDownTimer countDownTimer;
    Button next;
    private List<QuestionView> questionViewList;
    long timeleft;
    String q_id,question_id,userans;
    List<TimeData> timeleftlist;
    List<QuestionIdData> stringList;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    int player_time_left;
    int count =0;
    int count1 = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_questions_for_admin);

        linearLayout = findViewById(R.id.linear);
        viewPager = findViewById(R.id.slidequestion);
        next = findViewById(R.id.bnext);
        tiemr = findViewById(R.id.timer);

        sharedPreferences = getSharedPreferences("quizpref", MODE_PRIVATE);
        quizid = sharedPreferences.getString("quizid", "");
        quizname = sharedPreferences.getString("quizname", "");
        userid = sharedPreferences.getString("userid", "");
        quizdate = sharedPreferences.getString("quizdate","");
        student_name = sharedPreferences.getString("username","");

        pref = getSharedPreferences("score_pref", MODE_PRIVATE);

        editor = pref.edit();

        layout = new int[9];

        questionViewList = new ArrayList<>();
        timeleftlist = new ArrayList<TimeData>();

        final ProgressDialog dialog = new ProgressDialog(QuizQuestionsForAdminActivity.this);
        dialog.setTitle("Loading Data...");
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.show();

        long delayInMillis = 2000;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        }, delayInMillis);

        stringList = new ArrayList<>();
        addDotsIndicator(0);
        viewPager.addOnPageChangeListener(viewListener);

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
        databaseReference13 = FirebaseDatabase.getInstance().getReference();
        databaseReference14 = FirebaseDatabase.getInstance().getReference("group_player_score");
        databaseReference15 = FirebaseDatabase.getInstance().getReference();


        totalchild = FirebaseDatabase.getInstance().getReference();
        totalque = FirebaseDatabase.getInstance().getReference();
        dataref = FirebaseDatabase.getInstance().getReference();
        reference = FirebaseDatabase.getInstance().getReference();

        list = new ArrayList<>();

        notification_id = getIntent().getStringExtra("notification_id");
        Toast.makeText(getApplicationContext(), "deeke "+ notification_id,Toast.LENGTH_SHORT ).show();

        questionAdapter = new AdapterForAdminQuizQuestions(questionViewList, getApplicationContext(), notification_id);


        databaseReference6.child("group_Question_id").child(notification_id).addValueEventListener(new ValueEventListener() {
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

        timer();

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(countDownTimer!=null){
                    countDownTimer.cancel();
                }

                timer();

                //  Toast.makeText(getApplicationContext(), "time left "+ timeleft/1000, Toast.LENGTH_SHORT).show();

                timeleftlist.add(new TimeData(timeleft/1000));

                for (int i =0;i<timeleftlist.size();i++){

                    TimeData timeData = timeleftlist.get(i);
                    try {
                        QuestionView questionView = questionViewList.get(i);

                        InsertAnswer insertAnswer = new InsertAnswer(timeData.getTimelefts());

                        try {

                            databaseReference2.child("group_user_answer").child(notification_id).child(userid).child(questionView.getQuestionid()).child("timeleft").setValue(insertAnswer.getTimeleft());

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
                    new android.support.v7.app.AlertDialog.Builder(QuizQuestionsForAdminActivity.this)
                            .setTitle("Really Exit?")
                            .setMessage("Are you sure you want to submit the quiz?")
                            .setNegativeButton("Cancel", null)
                            .setPositiveButton("Submit", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface arg0, int arg1) {



                                    Query query = databaseReference4.child("group_user_answer").child(notification_id).child(userid).child(quizid).orderByChild("timeleft").equalTo(1);

                                    query.addChildEventListener(new ChildEventListener() {
                                        @Override
                                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                                            dataSnapshot.getRef().setValue(null);
                                        }

                                        @Override
                                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                                        }

                                        @Override
                                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                                        }

                                        @Override
                                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                    getPlayerAnswers(notification_id,userid);

                          /*          Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
                                    startActivity(intent);*/
                                    getUserCount(notification_id);
                                }
                            }).create().show();

                }

         //       getPlayerAnswers(notification_id,userid);
            }

        });


    }
    private void getQuizQuestions(String q_id){

        databaseReference.child("1V1-Quiz-Questions").orderByChild(q_id).addValueEventListener(new ValueEventListener() {
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

                Log.e("dkmsg", questionid + "\n" + question + "\n" + option1 + "\n" + option2 + "\n" + option3 + "\n" + option4);
                questionViewList.add(new QuestionView(questionid, question, option1, option2, option3, option4, answer, "null"));

         /*       for (DataSnapshot dataSnapshot1 : data.getChildren())
                {
                    for (DataSnapshot ds : dataSnapshot1.getChildren())
                    {
                        for (DataSnapshot dataSnapshot : ds.getChildren()) {

                            questionid = dataSnapshot.getKey();
                            question = dataSnapshot.child("Question").getValue(String.class);
                            option1 = dataSnapshot.child("Option1").getValue(String.class);
                            option2 = dataSnapshot.child("Option2").getValue(String.class);
                            option3 = dataSnapshot.child("Option3").getValue(String.class);
                            option4 = dataSnapshot.child("Option4").getValue(String.class);
                            answer = dataSnapshot.child("Answer").getValue(String.class);
                            description = dataSnapshot.child("Description").getValue(String.class);

                            Log.e("dkmsg", questionid + "\n" + question + "\n" + option1 + "\n" + option2 + "\n" + option3 + "\n" + option4);
                            questionViewList.add(new QuestionView(questionid, question, option1, option2, option3, option4, answer, "null"));

                        }
                    }
                }
*/

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


    private void timer(){

        countDownTimer =   new CountDownTimer(15000, 1000){

            @Override
            public void onTick(long millisUntilFinished) {

                tiemr.setText(String.format(FORMAT,
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)
                        ), TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)-TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));


                timeleft = millisUntilFinished;
            }

            @Override
            public void onFinish() {


                AlertDialog alertDialog;
                alertDialog = new AlertDialog.Builder(QuizQuestionsForAdminActivity.this).create();
                alertDialog.setCancelable(false);
                alertDialog.setTitle("Alert");
                alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                alertDialog.setMessage("Your Question time is over, click on next question");

                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Next Question", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        if(countDownTimer!=null){
                            countDownTimer.cancel();
                        }

                        timer();

                        timeleftlist.add(new TimeData(timeleft/1000));

                        if (mCurrentPage<layout.length) {
                            viewPager.setCurrentItem(mCurrentPage + 1);
                        }
                        else {
                            new android.support.v7.app.AlertDialog.Builder(QuizQuestionsForAdminActivity.this)
                                    .setTitle("Alert!!")
                                    .setMessage("Your Quiz is over, click on Submit button")
                                    .setCancelable(false)

                                    .setPositiveButton("Submit", new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface arg0, int arg1) {
                                            getPlayerAnswers(notification_id,userid);
                                            /*Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
                                            startActivity(intent);*/

                                            getUserCount(notification_id);
                                        }
                                    }).create().show();

                        }

                    }
                });

                alertDialog.show();
            }
        }.start();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode==KeyEvent.KEYCODE_BACK)
            Toast.makeText(getApplicationContext(), "take quiz",
                    Toast.LENGTH_LONG).show();

        return false;
    }

    private void getPlayerAnswers(String notification_id, String userid){
        databaseReference12.child("group_user_answer").child(notification_id).child(userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if (dataSnapshot.exists()){
                    for (DataSnapshot data : dataSnapshot.getChildren()){


                        question_id = data.getKey();
                        userans = data.child("useranswer").getValue().toString();
                        player_time_left = Integer.parseInt(data.child("timeleft").getValue().toString());

                       // Toast.makeText(getApplicationContext(), " "+ userans + "\n"+ player_time_left,Toast.LENGTH_SHORT).show();
                        getPlayerQuizQuestion(question_id, userans, player_time_left);
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getPlayerQuizQuestion(String question_id, final String user_answer, final int player_time_left){

        databaseReference13.child("1V1-Quiz-Questions").child(question_id).addValueEventListener(new ValueEventListener() {
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

                if (answer.equals(user_answer)){
                    count = count+player_time_left*3;
                    count1 = count1+1;
                }

                InsertGroupScore insertGroupScore = new InsertGroupScore(count, count1,student_name );

                databaseReference14.child(notification_id).child(userid).setValue(insertGroupScore);
             //   tvsender.setText("sender score : "+sender_count + "\n" + "Question Answered : "+ sender_count1+"/10");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void getUserCount(String notification_id){

        final ProgressDialog progressDialog;

        progressDialog = new ProgressDialog(QuizQuestionsForAdminActivity.this);
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Data Loading..." + "\n"+ "Wait for other Users");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);
        progressDialog.setMax(100);
        progressDialog.show();


        databaseReference15.child("group_player_score").child(notification_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                    int usercount = (int) dataSnapshot.getChildrenCount();
                   // Toast.makeText(getApplicationContext(), "users "+ usercount,Toast.LENGTH_SHORT).show();

                    if (usercount==2){
                        progressDialog.dismiss();
                        Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
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


