package com.aptitude.education.e2buddy.Quiz_Category;

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
import com.aptitude.education.e2buddy.R;
import com.aptitude.education.e2buddy.ViewData.CreditView;
import com.aptitude.education.e2buddy.ViewData.InsertAnswer;
import com.aptitude.education.e2buddy.ViewData.QuestionView;
import com.aptitude.education.e2buddy.ViewData.TimeData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class QuizCategoryQuestionActivity extends AppCompatActivity {

    AdapterForQuizCategoryQuestion questionAdapter;
    ViewPager viewPager;
    private TextView[] mdots;
    LinearLayout linearLayout;
    String questionid, question,date,q_id, option1,corr_ans, option2,option3,option4,quizids, answer,userid,description, parent_category, child_category;

    int onlineplayecount = 1534,mCurrentPage, quiz_id;
    int[] layout;
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
    int count1 = 0, scoreCount = 0,count = 0, scores = 0, category_score_counter=0;

    MediaPlayer player,player1;
    FirebaseAuth auth;
    private long timeCountInMilliSeconds = 1 * 60000;
    int x1=0;
    int x2=0;

    private enum TimerStatus {
        STARTED,
        STOPPED
    }
    private QuizCategoryQuestionActivity.TimerStatus timerStatus = QuizCategoryQuestionActivity.TimerStatus.STOPPED;

    private ProgressBar progressBarCircle;
    private TextView textViewTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_category_question);

        progressBarCircle = findViewById(R.id.progressBarCircle);
        textViewTime = findViewById(R.id.textViewTime);
        linearLayout = findViewById(R.id.linear);
        viewPager = findViewById(R.id.slidequestion);
        next = findViewById(R.id.bnext);
        tvcancelquiz = findViewById(R.id.tvcancel);
        onlinuser = findViewById(R.id.onlineuser);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        auth = FirebaseAuth.getInstance();
        final FirebaseUser user = auth.getCurrentUser();
        userid = user.getUid();


        parent_category = getIntent().getStringExtra("parent_category");
        child_category  = getIntent().getStringExtra("child_category");
        quiz_id  = getIntent().getIntExtra("quiz_id",0);

        Log.e("dekendra ", parent_category + "  "+ child_category+ "   "+ quiz_id);
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

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM");
        date = sdf.format(new Date());

        player = MediaPlayer.create(getApplicationContext(), R.raw.track1);
        player1 = MediaPlayer.create(getApplicationContext(), R.raw.track4);


        progressDialog = new ProgressDialog(QuizCategoryQuestionActivity.this);
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Data Loading...");
        progressDialog.show();


        questionAdapter = new AdapterForQuizCategoryQuestion(questionViewList, getApplicationContext(), userid, parent_category, child_category, quiz_id);

        getQuizQuestions();

        Runnable progressRunnable = new Runnable() {

            @Override
            public void run() {
                progressDialog.cancel();
                startStop(16000);

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
                    player = MediaPlayer.create(QuizCategoryQuestionActivity.this, R.raw.track1);
                    player1.stop();
                    player1.reset();
                    player1.release();
                    player1 = null;
                    player1 = MediaPlayer.create(QuizCategoryQuestionActivity.this, R.raw.track4);
                }

                startStop(16000);
                timeleftlist.add(new TimeData(timeleft/1000));

                for (int i =0;i<timeleftlist.size();i++){

                    TimeData timeData = timeleftlist.get(i);
                    try {
                        QuestionView questionView = questionViewList.get(i);

                        InsertAnswer insertAnswer = new InsertAnswer(timeData.getTimelefts());

                        try {
                            databaseReference.child("category_user_answer").child(userid).child(parent_category).child(child_category).child(String.valueOf(quiz_id)).child(questionView.getQuestionid()).child("timeleft").setValue(insertAnswer.getTimeleft());
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


                    new androidx.appcompat.app.AlertDialog.Builder(QuizCategoryQuestionActivity.this)
                            .setTitle("Submit...")
                            .setMessage("Click Submit button to see your Result")
                            .setCancelable(false)
                            .setPositiveButton("Submit", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface arg0, int arg1) {

                                    databaseReference.child("category_questions").child(parent_category).child(child_category).child(String.valueOf(quiz_id)).addListenerForSingleValueEvent(new ValueEventListener() {
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



        tvcancelquiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Log.e("timeleft ", String.valueOf(timeleft));
                countDownTimer.cancel();

                if (timeleft>5000){
                    x1 = player.getCurrentPosition();
                    player.pause();
                }else {
                    x2 = player1.getCurrentPosition();
                    player1.pause();
                }

                if (!isFinishing()) {
                    new AlertDialog.Builder(QuizCategoryQuestionActivity.this)
                            .setTitle("Alert!!")
                            .setMessage("Are you sure you want to Exit the Quiz?")
                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    player.stop();
                                    player1.stop();
                                    databaseReference.child("category_user_answer").child(userid).child(parent_category).child(child_category).child(String.valueOf(quiz_id)).removeValue();
                                    onBackPressed();
                                }
                            })
                            .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {


                                    if (timeleft>5000){
                                        player.seekTo(x1);
                                        player.start();
                                        startStop(timeleft);
                                    }else {
                                        player1.seekTo(x2);
                                        player1.start();
                                        startStop(timeleft);
                                    }


                                }
                            })
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

    private void startStop(final long timeCountInMilliSeconds) {

     //   timeCountInMilliSeconds = 16000;

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

                alertDialog = new AlertDialog.Builder(QuizCategoryQuestionActivity.this).create();
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
                        player = MediaPlayer.create(QuizCategoryQuestionActivity.this, R.raw.track1);
                        player1.stop();
                        player1.reset();
                        player1.release();
                        player1 = null;
                        player1 = MediaPlayer.create(QuizCategoryQuestionActivity.this, R.raw.track4);

                        if (mCurrentPage < layout.length) {
                            startStop(16000);
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


                            new AlertDialog.Builder(QuizCategoryQuestionActivity.this)
                                    .setTitle("Submit...")
                                    .setMessage("Click Submit button to see your Result")
                                    .setCancelable(false)
                                    .setPositiveButton("Submit", new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface arg0, int arg1) {


                                            //System.gc();
                                            databaseReference.child("category_questions").child(parent_category).child(child_category).child(String.valueOf(quiz_id)).addListenerForSingleValueEvent(new ValueEventListener() {
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


    private void getQuizQuestions() {


        valueEventListener = databaseReference.child("category_questions").child(parent_category).child(child_category).child(String.valueOf(quiz_id)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    questionid = dataSnapshot1.getKey();
                    question = dataSnapshot1.child("Question").getValue(String.class);
                    option1 = dataSnapshot1.child("Option1").getValue(String.class);
                    option2 = dataSnapshot1.child("Option2").getValue(String.class);
                    option3 = dataSnapshot1.child("Option3").getValue(String.class);
                    option4 = dataSnapshot1.child("Option4").getValue(String.class);
                    answer = dataSnapshot1.child("Answer").getValue(String.class);
                    description = dataSnapshot1.child("Description").getValue(String.class);

                 /*   Toast.makeText(QuizCategoryQuestionActivity.this, "deeke " + questionid + "\n" + question + "\n" + option1 +
                            "\n" + option2 + "\n" + option3 + "\n" + option4, Toast.LENGTH_SHORT).show();
*/
                    questionViewList.add(new QuestionView(questionid, question, option1, option2, option3, option4, answer, "null"));

                    viewPager.setAdapter(questionAdapter);
                }

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    private void getUserAnswer(final String q_id) {
        valueEventListener = databaseReference.child("category_user_answer").child(userid).child(parent_category).child(child_category).child(String.valueOf(quiz_id)).child(q_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                //  Toast.makeText(getApplicationContext(),""+useranswer +"\n"+ timeleft,Toast.LENGTH_SHORT).show();
                try {
                    String useranswer = dataSnapshot.child("useranswer").getValue(String.class);
                    int timeleft = Integer.parseInt(dataSnapshot.child("timeleft").getValue().toString());
                    Log.d("AnswerActivity", "questionid :" + q_id);


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
        final String TAG = getClass().getSimpleName();

        valueEventListener = databaseReference.child("category_questions").child(parent_category).child(child_category).child(String.valueOf(quiz_id)).child(questionId).addValueEventListener(new ValueEventListener() {
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

        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("category_user_credit");

        final int num =1;

        CreditView creditView = new CreditView(scores,count1);

        reference1.child(userid).child(parent_category).child(child_category).child(String.valueOf(quiz_id)).setValue(creditView);

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

        final ProgressDialog dialog = new ProgressDialog(QuizCategoryQuestionActivity.this);
        dialog.setTitle("Please wait...");
        dialog.setMessage("Generating Your Result");
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.show();

        long delayInMillis = 3000;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                insertTotalQuizScore();

                dialog.dismiss();

                Intent intent = new Intent(QuizCategoryQuestionActivity.this, CategoryResultActivity.class);
                intent.putExtra("parent_category",parent_category);
                intent.putExtra("child_category", child_category);
                intent.putExtra("quiz_id", quiz_id);
                intent.putExtra("userid", userid);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                QuizCategoryQuestionActivity.this.finish();

            }
        }, delayInMillis);


    }

    private void insertTotalQuizScore(){

        databaseReference.child("category_user_credit").child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {

                        for (DataSnapshot dataSnapshot3 : dataSnapshot2.getChildren()) {

                            final int score = (Integer.parseInt(dataSnapshot3.child("credit_points").getValue().toString()));
                            Log.e("score ", String.valueOf(score));
                            category_score_counter = category_score_counter+score;
                        }
                    }
                }
                databaseReference.child("final_Points").child(userid).child("category_total_score").setValue(category_score_counter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

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




