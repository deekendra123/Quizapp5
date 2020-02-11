package com.aptitude.education.e2buddy.Placement_Papers;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Handler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AlertDialog;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aptitude.education.e2buddy.DisplayAnswer.LeaderBoardData;
import com.aptitude.education.e2buddy.R;
import com.aptitude.education.e2buddy.ShowCaseView.MaterialShowcaseSequence;
import com.aptitude.education.e2buddy.ShowCaseView.MaterialShowcaseView;
import com.aptitude.education.e2buddy.ShowCaseView.ShowcaseConfig;
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
import java.util.concurrent.TimeUnit;

public class PlacementQuizQuestionsActivity extends AppCompatActivity {

    AdapterForPlacementQuizQuestion questionAdapter;
    ViewPager viewPager;
    private TextView[] mdots;
    String questionid, question,date,q_id, option1, option2,option3,option4, answer,userid,description,corr_ans;
    int mCurrentPage;
    int[] layout;
    List<LeaderBoardData> leaderBoardDataList,dataList;
    TextView next,previous,tvBookmark;
    DatabaseReference databaseReference;
    private List<QuestionView> questionViewList;
    List<TimeData> timeleftlist;
    ProgressDialog progressDialog;
    List<LeaderBoardData> list;


    ValueEventListener valueEventListener;
    MediaPlayer player,player1;
    FirebaseAuth auth;
    String timeleft;

    private TextView textViewTime;
    private static final String SHOWCASE_ID = "e2buddy";


    FloatingActionButton fabMain, fabOne, fabTwo;
    Float translationY = 100f;

    OvershootInterpolator interpolator = new OvershootInterpolator();

    private static final String TAG = "MainActivity";

    Boolean isMenuOpen = false;
    private static final String FORMAT = "%02d:%02d";
    int count1 = 0;
    ProgressBar progressBar;
    int i=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placement_quiz_questions);

        textViewTime = findViewById(R.id.textViewTime);
        viewPager = findViewById(R.id.slidequestion);
        next = findViewById(R.id.bnext);
        previous = findViewById(R.id.bprevious);
        textViewTime = findViewById(R.id.textViewTime);
        progressBar = findViewById(R.id.progress_horizontal);
        tvBookmark = findViewById(R.id.tvBookmark);

        Resources res = getResources();
        progressBar.setProgressDrawable(res.getDrawable( R.drawable.progress_color));

        databaseReference = FirebaseDatabase.getInstance().getReference();

        auth = FirebaseAuth.getInstance();
        final FirebaseUser user = auth.getCurrentUser();
        userid = user.getUid();

        layout = new int[6];

        questionViewList = new ArrayList<>();
        timeleftlist = new ArrayList<TimeData>();
        leaderBoardDataList = new ArrayList<>();
        list = new ArrayList<>();
        dataList = new ArrayList<>();

        viewPager.addOnPageChangeListener(viewListener);
        //  viewPager.beginFakeDrag();

        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        date = sdf.format(new Date());

        player = MediaPlayer.create(getApplicationContext(), R.raw.track1);
        player1 = MediaPlayer.create(getApplicationContext(), R.raw.track4);



        questionAdapter = new AdapterForPlacementQuizQuestion(questionViewList, getApplicationContext(), userid);

        valueEventListener =  databaseReference.child("placementPaper").child("quantitativeAbility").child("quiz1").addValueEventListener(new ValueEventListener() {
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

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mCurrentPage<layout.length) {
                    viewPager.setCurrentItem(mCurrentPage + 1);
                }
                else
                {
                    new androidx.appcompat.app.AlertDialog.Builder(PlacementQuizQuestionsActivity.this)
                            .setTitle("Submit...")
                            .setMessage("Click Submit button to see your Result")
                            .setCancelable(false)
                            .setPositiveButton("Submit", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface arg0, int arg1) {

                                    showLoader();
                               }
                            }).create().show();

                }
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentPage<layout.length) {
                    viewPager.setCurrentItem(mCurrentPage - 1);
                }

            }
        });


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isFirstRun = preferences.getBoolean("FIRSTRUN", true);
        if (isFirstRun)
        {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("FIRSTRUN", false);

            editor.commit();
            presentShowcaseSequence();
            startTimer();


        }
        else {

            progressDialog = new ProgressDialog(PlacementQuizQuestionsActivity.this);
            progressDialog.setCancelable(true);
            progressDialog.setMessage("Data Loading...");
            progressDialog.show();

            Runnable progressRunnable = new Runnable() {

                @Override
                public void run() {
                    progressDialog.cancel();
                    startTimer();
                }
            };

            Handler pdCanceller = new Handler();
            pdCanceller.postDelayed(progressRunnable, 3000);
        }

        tvBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlacementQuizQuestionsActivity.this, BookmarkQuestionActivity.class);
                startActivity(intent);
            }
        });

    }

    private void startTimer(){
        progressBar.setProgress(i);
        new CountDownTimer(120000, 1000) {

            public void onTick(long millisUntilFinished) {

                i++;
                progressBar.setProgress(i *100/(120000/1000));

                String timer = String.format(FORMAT,
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),

                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));

                textViewTime.setText(""+timer);

                timeleft = timer;

            }

            public void onFinish() {
                i++;
                progressBar.setProgress(100);
                textViewTime.setText("00:00");
            }

        }.start();
    }

    private void getUserAnswer(final String q_id) {
        valueEventListener = databaseReference.child("placemenPaper_user_answer").child(userid).child("quantitativeAbility").child("quiz1").child(q_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                //  Toast.makeText(getApplicationContext(),""+useranswer +"\n"+ timeleft,Toast.LENGTH_SHORT).show();
                try {
                    String useranswer = dataSnapshot.child("answer").getValue(String.class);
                    Log.d("AnswerActivity", "questionid :" + q_id);

                    if (useranswer == null) {
                        useranswer.replace(null, "question was not given");
                    }

                    getQuestion(q_id, useranswer);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });

    }

    private void getQuestion(final String questionId, final String useranswer){
        final String TAG = getClass().getSimpleName();

        databaseReference.child("placementPaper").child("quantitativeAbility").child("quiz1").child(questionId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    Log.d(TAG, "children are: "+dataSnapshot.getKey());

                    Log.d(TAG, "childs: "+dataSnapshot.getKey());
                    question = dataSnapshot.child("question").getValue(String.class);
                    corr_ans = dataSnapshot.child("answer").getValue(String.class);

                    if (corr_ans.equals(useranswer)) {

                        count1 = count1+1;

                    }
                    insertCredit();

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

        databaseReference.child("placemenPaper_user_answer").child(userid).child("quantitativeAbility").child("quiz1").child("timeleft").setValue(timeleft);
        databaseReference.child("placemenPaper_user_answer").child(userid).child("quantitativeAbility").child("quiz1").child("correct_answer").setValue(count1);

    }

    private void presentShowcaseSequence() {

        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(100);

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, SHOWCASE_ID);

        sequence.setOnItemShownListener(new MaterialShowcaseSequence.OnSequenceItemShownListener() {
            @Override
            public void onShow(MaterialShowcaseView itemView, int position) {
                //   Toast.makeText(itemView.getContext(), "Item #" + position, Toast.LENGTH_SHORT).show();
            }
        });

        sequence.setConfig(config);

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setSkipText("Skip")

                        .setTarget(textViewTime)
                        .setDismissText("Got it")
                        .setDismissOnTouch(true)
                        .setContentText("time to complete the quiz")
                        .withCircleShape()
                        .build()
        );

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setSkipText("Skip")
                        .setTarget(fabMain)
                        .setDismissText("Got it")
                        .setDismissOnTouch(true)
                        .setContentText("Here you can see your Bookmarks")
                        .withCircleShape()
                        .build()
        );

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setSkipText("Skip")
                        .setTarget(next)
                        .setDismissText("Got it")
                        .setDismissOnTouch(true)
                        .setContentText("Here you can go to next question on click button")
                        .withCircleShape()
                        .build()
        );


        sequence.start();

    }


    private void getQuizQuestions(String q_id) {


        valueEventListener = databaseReference.child("placementPaper").child("quantitativeAbility").child("quiz1").child(q_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                    questionid = dataSnapshot.getKey();
                    question = dataSnapshot.child("question").getValue(String.class);
                    option1 = dataSnapshot.child("option1").getValue(String.class);
                    option2 = dataSnapshot.child("option2").getValue(String.class);
                    option3 = dataSnapshot.child("option3").getValue(String.class);
                    option4 = dataSnapshot.child("option4").getValue(String.class);
                    answer = dataSnapshot.child("answer").getValue(String.class);
                    description = dataSnapshot.child("description").getValue(String.class);

                    questionViewList.add(new QuestionView(questionid, question, option1, option2, option3, option4, answer, "null"));

                    viewPager.setAdapter(questionAdapter);




                }catch (Exception e){
                    e.printStackTrace();
                }

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    private void addDotsIndicator(int position){
        mdots = new TextView[7];

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
                previous.setEnabled(false);
                previous.setVisibility(View.INVISIBLE);
                next.setText("NEXT");
                previous.setText("");

            }else if (position==mdots.length-1){

                next.setEnabled(true);
                previous.setEnabled(true);
                previous.setVisibility(View.INVISIBLE);
                next.setText("FINISH");
                previous.setText("BACK");

            }
            else {

                next.setEnabled(true);
                previous.setEnabled(true);
                previous.setVisibility(View.VISIBLE);
                next.setText("NEXT");
                previous.setText("BACK");

            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };



    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseReference.removeEventListener(valueEventListener);
    }

    private void showLoader() {

        final ProgressDialog dialog = new ProgressDialog(PlacementQuizQuestionsActivity.this);
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

                databaseReference.child("placemenPaper_user_answer").child(userid).child("quantitativeAbility").child("quiz1").addListenerForSingleValueEvent(new ValueEventListener() {
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

                dialog.dismiss();

                Intent intent = new Intent(PlacementQuizQuestionsActivity.this, PlacementResultActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                PlacementQuizQuestionsActivity.this.finish();

            }
        }, delayInMillis);


    }


    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences("bookmark", MODE_PRIVATE);
        String questionid = sharedPreferences.getString("questionid", "");
        int position = sharedPreferences.getInt("position",-1);

        if (position>=0) {
            Log.e("deeke", questionid);

            viewPager.setCurrentItem(position);
        }

    }

    @Override
    public void onBackPressed() {


        new AlertDialog.Builder(PlacementQuizQuestionsActivity.this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit the Quiz?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {


                        PlacementQuizQuestionsActivity.super.onBackPressed();

                    }
                }).create().show();

    }

}





