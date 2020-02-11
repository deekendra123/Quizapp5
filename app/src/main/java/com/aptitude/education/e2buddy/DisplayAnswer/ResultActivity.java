package com.aptitude.education.e2buddy.DisplayAnswer;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aptitude.education.e2buddy.GoogleAds.InterstitialAd;
import com.aptitude.education.e2buddy.Question.HomeNevActivity;
import com.aptitude.education.e2buddy.Question.QuizQuestionTimeBasedActivity;
import com.aptitude.education.e2buddy.R;
import com.aptitude.education.e2buddy.ViewData.AnswerView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ResultActivity extends AppCompatActivity {


    Transformation transformation;
    ImageView img1, img2, img3, img4, userIcon;
    TextView tv1,tv2,tvscore,tvanswer, tvCorrectAnswer;
    Button leaderboard, btviewans;
    LinearLayout linearLayout;
    DatabaseReference databaseReference;
    List<AnswerView> answerViewList;
    String userid,quizdate ,date,value,score;
    ValueEventListener valueEventListener;
    int corr_answer ;
    ProgressDialog progressDialog;
    static ResultActivity resultActivity;


    public static ResultActivity getInstance(){
        return   resultActivity;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result2);

        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tscore);
        tvscore = findViewById(R.id.tscore1);
        leaderboard = findViewById(R.id.btleaderboard);
        userIcon = findViewById(R.id.userimg);
        btviewans = findViewById(R.id.btview);
        img1 = findViewById(R.id.img1);
        img2 = findViewById(R.id.img2);
        img3 = findViewById(R.id.img3);
        img4 = findViewById(R.id.img4);
        linearLayout = findViewById(R.id.linear);
        tvCorrectAnswer = findViewById(R.id.tvcorrectans);
        tvanswer = findViewById(R.id.tvans);



        progressDialog = new ProgressDialog(ResultActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Data Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);
        progressDialog.setMax(100);
        progressDialog.show();


        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM");
        date = sdf.format(new Date());

        databaseReference = FirebaseDatabase.getInstance().getReference();

        quizdate = getIntent().getStringExtra("quiz_date");
        value  = getIntent().getStringExtra("curent_date");
        userid = getIntent().getStringExtra("userid");

        answerViewList = new ArrayList<>();

        getTodayQuizScore();
        transformation = new RoundedTransformationBuilder()
                .borderColor(getResources().getColor(R.color.white))
                .borderWidthDp(0)
                .cornerRadiusDp(50)
                .oval(false)
                .build();



        getPlayerImage();


        btviewans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                System.gc();
                FragmentManager fm = getSupportFragmentManager();
                DialogFragment dialog = View_Answer_Dialog.newInstance();

                Bundle bundle = new Bundle();
                bundle.putString("quiz_date",quizdate);
                bundle.putString("curent_date",value);
                bundle.putString("userid",userid);
                dialog.setArguments(bundle);
                dialog.show(fm,"tag");


            }
        });


        leaderboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                System.gc();
                Intent intent = new Intent(getApplicationContext(), LeaderBoardForQuizActivity.class);
                intent.putExtra("quiz_date",quizdate);
                intent.putExtra("curent_date", value);
                intent.putExtra("userid", userid);
                startActivity(intent);

            }
        });

        loadAd();

    }

    private void loadAd() {
        InterstitialAd interstitialAd = new InterstitialAd(ResultActivity.this);
        interstitialAd.loadInterstitialAd();
    }

    private void getTodayQuizScore(){
      valueEventListener =  databaseReference.child("daily_user_credit").child(userid).child(quizdate).child(value).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                try {
                     score = dataSnapshot.child("credit_points").getValue().toString();
                    corr_answer = Integer.parseInt(dataSnapshot.child("correct_answers").getValue().toString());

                    tvscore.setText(""+score);
                    tvCorrectAnswer.setText(""+corr_answer+"/10");
                    progressDialog.dismiss();
                    if (corr_answer<=4){
                        linearLayout.setVisibility(View.GONE);
                        img1.setVisibility(View.GONE);
                        img2.setVisibility(View.GONE);
                        img3.setVisibility(View.GONE);

                        ViewGroup.MarginLayoutParams marginParams1 = (ViewGroup.MarginLayoutParams) tv1.getLayoutParams();
                        marginParams1.setMargins(0, 40, 0, 0);

                        ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) tv1.getLayoutParams();
                        marginParams.setMargins(0, 50, 0, 35);


                        tv1.setTextSize(21f);
                        img4.setBackgroundResource(R.drawable.goforit);
                        tv1.setText("Better Luck Next Time");
                    }
                    else {

                        img4.setBackgroundResource(R.drawable.ic_achievement);
                        tv1.setText("Congratulations!");

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
    private void getPlayerImage(){

       valueEventListener = databaseReference.child("user_info").child(userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {

                    String imageUrl = dataSnapshot.child("image_Url").getValue(String.class);

                    Picasso.get()
                            .load(imageUrl)
                            .placeholder(R.drawable.userimg)
                            .fit()
                            .transform(transformation)
                            .into(userIcon);


                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(ResultActivity.this, HomeNevActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseReference.removeEventListener(valueEventListener);
    }



}


