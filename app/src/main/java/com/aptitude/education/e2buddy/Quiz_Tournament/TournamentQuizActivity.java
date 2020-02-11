package com.aptitude.education.e2buddy.Quiz_Tournament;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Handler;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aptitude.education.e2buddy.Intro.SessionManager;
import com.aptitude.education.e2buddy.Question.HomeNevActivity;
import com.aptitude.education.e2buddy.R;
import com.aptitude.education.e2buddy.ViewData.CreditView;
import com.aptitude.education.e2buddy.ViewData.InsertAnswer;
import com.aptitude.education.e2buddy.ViewData.QuestionView;
import com.aptitude.education.e2buddy.ViewData.TimeData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class TournamentQuizActivity extends AppCompatActivity {

    TournamentQuizAdapter questionAdapter;
    ViewPager viewPager;
    private TextView[] mdots;
    LinearLayout linearLayout;
    String questionid, question,q_id, option1,corr_ans, option2,option3,option4, answer,description,playerId,playerName,playerEmail,playerImageUrl,tournament_quiz_id;
    int onlineplayecount = 1534,mCurrentPage;
    int[] layout;
    CountDownTimer countDownTimer;
    Button next;
    DatabaseReference databaseReference;
    private List<QuestionView> questionViewList;
    AlertDialog alertDialog;
    List<TimeData> timeleftlist;
    ProgressDialog progressDialog;
    ValueEventListener valueEventListener;
    int count1 = 0,count = 0, scores = 0;
    MediaPlayer player,player1;
    private long timeCountInMilliSeconds = 1 * 60000, timeleft;
    SessionManager sessionManager;
    private ProgressBar progressBarCircle;
    private TextView textViewTime, tvcancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournament_quiz);
        progressBarCircle = findViewById(R.id.progressBarCircle);
        textViewTime = findViewById(R.id.textViewTime);
        linearLayout = findViewById(R.id.linear);
        viewPager = findViewById(R.id.slidequestion);
        next = findViewById(R.id.bnext);
        tvcancel = findViewById(R.id.tvcancel);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        layout = new int[9];

        sessionManager = new SessionManager(TournamentQuizActivity.this);

        HashMap<String, String> user = sessionManager.getData();

        playerId = user.get(SessionManager.KEY_PLAYER_ID);
        playerName = user.get(SessionManager.KEY_PLAYER_NAME);
        playerEmail = user.get(SessionManager.KEY_PLAYER_EMAIL);
        playerImageUrl = user.get(SessionManager.KEY_PLAYER_IMAGE_URL);
        tournament_quiz_id = user.get(SessionManager.KEY_TOURNAMENT_QUIZ_ID);

        questionViewList = new ArrayList<>();
        timeleftlist = new ArrayList<TimeData>();

        viewPager.addOnPageChangeListener(viewListener);

        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        player = MediaPlayer.create(getApplicationContext(), R.raw.track1);
        player1 = MediaPlayer.create(getApplicationContext(), R.raw.track4);


        progressDialog = new ProgressDialog(TournamentQuizActivity.this);
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Data Loading...");
        progressDialog.show();

        questionAdapter = new TournamentQuizAdapter(questionViewList, getApplicationContext(), playerId, tournament_quiz_id);

        valueEventListener =  databaseReference.child("tournament_question_ids").child(tournament_quiz_id).addValueEventListener(new ValueEventListener() {
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
                startStop();

            }
        };

        Handler pdCanceller = new Handler();
        pdCanceller.postDelayed(progressRunnable, 3000);


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //     System.gc();
                if(countDownTimer!=null){
                    countDownTimer.cancel();
                }

                if(player.isPlaying() || player1.isPlaying()) {
                    player.stop();
                    player.reset();
                    player.release();
                    player = null;
                    player = MediaPlayer.create(TournamentQuizActivity.this, R.raw.track1);
                    player1.stop();
                    player1.reset();
                    player1.release();
                    player1 = null;
                    player1 = MediaPlayer.create(TournamentQuizActivity.this, R.raw.track4);
                }

                startStop();
                timeleftlist.add(new TimeData(timeleft/1000));

                for (int i =0;i<timeleftlist.size();i++){

                    TimeData timeData = timeleftlist.get(i);
                    try {
                        QuestionView questionView = questionViewList.get(i);

                        InsertAnswer insertAnswer = new InsertAnswer(timeData.getTimelefts());

                        try {

                            databaseReference.child("tournament_user_answer").child(playerId).child(tournament_quiz_id).child(questionView.getQuestionid()).child("timeleft").setValue(insertAnswer.getTimeleft());


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
                    if(player.isPlaying() || player1.isPlaying()) {
                        player.stop();
                        player.reset();
                        player.release();
                        player = null;
                        player1.stop();
                        player1.reset();
                        player1.release();
                        player1 = null;
                    }


                    new AlertDialog.Builder(TournamentQuizActivity.this)
                            .setTitle("Submit...")
                            .setMessage("Click Submit button to see your Result")
                            .setCancelable(false)
                            .setPositiveButton("Submit", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface arg0, int arg1) {

                                    databaseReference.child("tournament_question_ids").child(tournament_quiz_id).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                                                q_id = dataSnapshot1.getKey();
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

        tvcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!isFinishing()) {
                    new AlertDialog.Builder(TournamentQuizActivity.this)
                            .setTitle("Alert!!")
                            .setMessage("Are you sure you want to Exit the Quiz?")

                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    player.stop();
                                    player1.stop();

                                    databaseReference.child("tournament_user_answer").child(playerId).child(tournament_quiz_id).removeValue();
                                    Intent i = new Intent(getApplicationContext(), HomeNevActivity.class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(i);
                                    TournamentQuizActivity.this.finish();                        }
                            })

                            .setNegativeButton("NO", null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }

            }
        });


    }


    private void startStop() {

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


                alertDialog = new AlertDialog.Builder(TournamentQuizActivity.this).create();
                alertDialog.setCancelable(false);
                alertDialog.setTitle("Alert");
                alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                alertDialog.setMessage("You ran out of Time, Go to next Question");

                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Next Question", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        if (countDownTimer != null) {
                            countDownTimer.cancel();
                        }

                        player.stop();
                        player.reset();
                        player.release();
                        player = null;
                        player = MediaPlayer.create(TournamentQuizActivity.this, R.raw.track1);
                        player1.stop();
                        player1.reset();
                        player1.release();
                        player1 = null;
                        player1 = MediaPlayer.create(TournamentQuizActivity.this, R.raw.track4);

                        if (mCurrentPage < layout.length) {
                            startStop();
                            timeleftlist.add(new TimeData(timeleft / 1000));

                            viewPager.setCurrentItem(mCurrentPage + 1);
                        } else {

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


                            new AlertDialog.Builder(TournamentQuizActivity.this)
                                    .setTitle("Submit...")
                                    .setMessage("Click Submit button to see your Result")
                                    .setCancelable(false)
                                    .setPositiveButton("Submit", new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface arg0, int arg1) {

                                            databaseReference.child("tournament_question_ids").child(tournament_quiz_id).addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {

                                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                                                        q_id = dataSnapshot1.getKey();
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
                if (!isFinishing()){
                    alertDialog.show();
                }

                progressBarCircle.setMax((int) timeCountInMilliSeconds / 1000);
                progressBarCircle.setProgress((int) timeCountInMilliSeconds / 1000);

            }

        }.start();




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

                questionViewList.add(new QuestionView(questionid, question, option1, option2, option3, option4, answer, "null"));

                viewPager.setAdapter(questionAdapter);
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    private void getUserAnswer(final String q_id) {
        valueEventListener = databaseReference.child("tournament_user_answer").child(playerId).child(tournament_quiz_id).child(q_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                    String useranswer = dataSnapshot.child("useranswer").getValue(String.class);
                    int timeleft = Integer.parseInt(dataSnapshot.child("timeleft").getValue().toString());

                    if (useranswer == null) {
                        useranswer.replace(null, "question was not given");

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

        valueEventListener = databaseReference.child("questions").child(questionId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {

                    question = dataSnapshot.child("Question").getValue(String.class);
                    corr_ans = dataSnapshot.child("Answer").getValue(String.class);


                    if (corr_ans.equals(useranswer)) {
                        count = count+2;

                        scores = scores+timeleft*1;
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

        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("tournament_user_credit");
        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference();

        CreditView creditView = new CreditView(scores,tournament_quiz_id,count1);
        reference1.child(playerId).child(tournament_quiz_id).setValue(creditView);
        reference2.child("tournament_date_joiners").child(tournament_quiz_id).child(playerId).child("credit_points").setValue(scores);

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


    private void showLoader() {

        final ProgressDialog dialog = new ProgressDialog(TournamentQuizActivity.this);
        dialog.setTitle("Please wait...");
        dialog.setMessage("Generating Your Result");
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.show();
        databaseReference.child("tournament_register_students").child(playerId).child(tournament_quiz_id).child("play_status").setValue(true);
        long delayInMillis = 3000;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                dialog.dismiss();
                Intent intent = new Intent(TournamentQuizActivity.this, ResultActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                TournamentQuizActivity.this.finish();

            }
        }, delayInMillis);


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode==KeyEvent.KEYCODE_BACK)
            Toast.makeText(getApplicationContext(), "take quiz",
                    Toast.LENGTH_LONG).show();

        return false;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseReference.removeEventListener(valueEventListener);
    }
}
