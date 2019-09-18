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

    private TextView[] mdots;
    SnapHelper snap;
    LinearLayout linearLayout;
    String questionid, question,date,q_id,value, option1, option2,option3,option4, answer,userid,quizdate,description;
    int onlineplayecount = 1534,mCurrentPage,pos;
  //  int[] layout;
    TextView onlinuser,tvcancelquiz;
    List<LeaderBoardData> leaderBoardDataList,dataList;
    CountDownTimer countDownTimer;
    Button next;
    DatabaseReference databaseReference;
    private List<QuestionView> questionViewList;
    AlertDialog alertDialog;
    long timeleft;
    List<TimeData> timeleftlist;
    ProgressDialog progressDialog;
    List<LeaderBoardData> list;
    LeaderBoardAdapter leaderBoardAdapter;
    ValueEventListener valueEventListener;
    RecyclerView recycler;
    RecyclerView.LayoutManager layoutManager;

    MediaPlayer player,player1;
    FirebaseAuth auth;
    private long timeCountInMilliSeconds = 1 * 60000;

    private enum TimerStatus {
        STARTED,
        STOPPED
    }
    private TimerStatus timerStatus = TimerStatus.STOPPED;

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
            snap=new PagerSnapHelper();

            quizdate = getIntent().getStringExtra("quiz_date");

            value  = getIntent().getStringExtra("curent_date");

            questionViewList = new ArrayList<>();
            timeleftlist = new ArrayList<TimeData>();
            leaderBoardDataList = new ArrayList<>();
            list = new ArrayList<>();
            dataList = new ArrayList<>();
            leaderBoardAdapter = new LeaderBoardAdapter(getApplicationContext(), list, userid,tvcancelquiz);

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM");
            date = sdf.format(new Date());

            player = MediaPlayer.create(getApplicationContext(), R.raw.track1);
            player1 = MediaPlayer.create(getApplicationContext(), R.raw.track4);


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
                    startStop();

                }
            };

            Handler pdCanceller = new Handler();
            pdCanceller.postDelayed(progressRunnable, 3000);

            View currentposition = snap.findSnapView(layoutManager);
            pos = layoutManager.getPosition(currentposition);

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
                        player = MediaPlayer.create(QuizActivity.this, R.raw.track1);
                        player1.stop();
                        player1.reset();
                        player1.release();
                        player1 = null;
                        player1 = MediaPlayer.create(QuizActivity.this, R.raw.track4);
                    }

                    startStop();
                    timeleftlist.add(new TimeData(timeleft/1000));

                    for (int i =0;i<timeleftlist.size();i++){

                        TimeData timeData = timeleftlist.get(i);
                        try {
                            QuestionView questionView = questionViewList.get(i);

                            InsertAnswer insertAnswer = new InsertAnswer(timeData.getTimelefts());

                            try {

                                databaseReference.child("daily_user_answer").child(userid).child(quizdate).child(value).child(questionView.getQuestionid()).child("timeleft").setValue(insertAnswer.getTimeleft());


                            }
                            catch (Exception e){

                                e.printStackTrace();
                            }

                        }
                        catch (Exception e) {
                            e.printStackTrace();


                        }
                    }

                    if (pos<10) {
                        recycler.smoothScrollToPosition(pos+1);
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


                        new android.support.v7.app.AlertDialog.Builder(QuizActivity.this)
                                .setTitle("Submit...")
                                .setMessage("Click Submit button to see your Result")
                                .setCancelable(false)
                                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface arg0, int arg1) {

                                        Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
                                        intent.putExtra("quiz_date",quizdate);
                                        intent.putExtra("curent_date", value);
                                        intent.putExtra("userid", userid);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        QuizActivity.this.finish();


                                    }
                                }).create().show();

                    }
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


                    alertDialog = new AlertDialog.Builder(QuizActivity.this).create();
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
                            player = MediaPlayer.create(QuizActivity.this, R.raw.track1);
                            player1.stop();
                            player1.reset();
                            player1.release();
                            player1 = null;
                            player1 = MediaPlayer.create(QuizActivity.this, R.raw.track4);
                            if (pos < 10) {
                                startStop();
                                timeleftlist.add(new TimeData(timeleft / 1000));

//                                viewPager.setCurrentItem(mCurrentPage + 1);
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


                                new AlertDialog.Builder(QuizActivity.this)
                                        .setTitle("Submit...")
                                        .setMessage("Click Submit button to see your Result")
                                        .setCancelable(false)
                                        .setPositiveButton("Submit", new DialogInterface.OnClickListener() {

                                            public void onClick(DialogInterface arg0, int arg1) {

                                                Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
                                                intent.putExtra("quiz_date",quizdate);
                                                intent.putExtra("curent_date", value);
                                                intent.putExtra("userid", userid);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                                QuizActivity.this.finish();

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

                    Log.e("dkmsg",questionid +"\n"+ question +"\n"+ option1 + "\n"+option2 +"\n"+ option3 +"\n"+ option4);

                    questionViewList.add(new QuestionView(questionid, question, option1, option2, option3, option4, answer, "null"));

                    recycler.setAdapter(questionAdapter);
                    snap.attachToRecyclerView(recycler);
                }


                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }






  /*      private void addDotsIndicator(int position){
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

*/
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




