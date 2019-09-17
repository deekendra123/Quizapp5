package com.aptitude.education.e2buddy.DisplayAnswer;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aptitude.education.e2buddy.Intro.CheckInternet;
import com.aptitude.education.e2buddy.One_on_One_Quiz_Challenge.LoaderForReceiverActivity;
import com.aptitude.education.e2buddy.Question.HomeNevActivity;
import com.aptitude.education.e2buddy.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class LeaderBoardForQuizActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    DatabaseReference reference2;
    LeaderBoardAdapter leaderBoardAdapter;
    List<LeaderBoardData> list;
    DatabaseReference reference1;
    DatabaseReference reference3;
    DatabaseReference reference4;
    DatabaseReference reference5;
    DatabaseReference reference6;
    DatabaseReference reference7;
    String  userid,quizdate, username, yourscore ,playername;
    int scores;
    String userids;
    String quizids;
    TextView score;
    TextView tvscore;
    Transformation transformation;
    List<String> stringList;
    ImageView userIcon;
    TextView textViewRank;
    String value;
    TextView leaderdata,coin, tvrank;
    ProgressDialog progressDialog;


    AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board_for_quiz);

        recyclerView = findViewById(R.id.leaderboard);
        tvscore = findViewById(R.id.qscore);
        leaderdata = findViewById(R.id.tvdata);
        coin = findViewById(R.id.coinsds);
        tvrank = findViewById(R.id.r);
        textViewRank = findViewById(R.id.img);
        score = findViewById(R.id.textView13);
        userIcon = findViewById(R.id.e2buddy);

        quizdate = getIntent().getStringExtra("quiz_date");
        value  = getIntent().getStringExtra("curent_date");
        userid = getIntent().getStringExtra("userid");

        Typeface type = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
        leaderdata.setTypeface(type);
        coin.setTypeface(type);
        tvrank.setTypeface(type);

        Typeface type1 = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Black.ttf");
        tvscore.setTypeface(type1);

        progressDialog = new ProgressDialog(LeaderBoardForQuizActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Data Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);
        progressDialog.setMax(100);
        progressDialog.show();



        Runnable progressRunnable = new Runnable() {

            @Override
            public void run() {
                progressDialog.dismiss();

            }
        };

        Handler pdCanceller = new Handler();
        pdCanceller.postDelayed(progressRunnable,
                3400);


        CheckInternet checkInternet = new CheckInternet(getApplicationContext());
        checkInternet.checkConnection();

        transformation = new RoundedTransformationBuilder()
                .borderColor(getResources().getColor(R.color.white))
                .borderWidthDp(0)
                .cornerRadiusDp(50)
                .oval(false)
                .build();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));


        leaderdata.setText(""+playername);
        reference1 = FirebaseDatabase.getInstance().getReference();
        reference2 = FirebaseDatabase.getInstance().getReference();
        reference3 = FirebaseDatabase.getInstance().getReference();
        reference4 = FirebaseDatabase.getInstance().getReference();
        reference5 = FirebaseDatabase.getInstance().getReference();
        reference6 = FirebaseDatabase.getInstance().getReference("daily_user_total_score");
        reference7 = FirebaseDatabase.getInstance().getReference();

        getPlayerImage();
        getPlayerName();

        list = new ArrayList<>();
        stringList = new ArrayList<>();
        leaderBoardAdapter = new LeaderBoardAdapter(getApplicationContext(), list, userid,textViewRank);


        reference1.child("daily_user_credit").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    userids = data.getKey();

                    getQuiz(userids);
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        reference4.child("daily_user_credit").child(userid).child(quizdate).child(value).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    yourscore = dataSnapshot.child("credit_points").getValue().toString();
                    score.setText(""+yourscore);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        leaderBoardAdapter.setOnItemClickListener(new LeaderBoardAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {

                LeaderBoardData leaderBoardData = list.get(position);
                String user = leaderBoardData.getUserid();
                final int scoresss = list.get(position).getScore();
                //  showAlertDialog(itemView, user, position);
            }
        });

    }


    private void getQuiz(final String id){
        Query lastQuery = reference2.child("daily_user_credit").child(id).child(quizdate).orderByKey().limitToLast(1);

        lastQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                quizids = dataSnapshot.getKey();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    String date = dataSnapshot1.getKey();

                    scores = Integer.parseInt(dataSnapshot1.child("credit_points").getValue().toString());

                    //  Toast.makeText(getApplicationContext(), "userid : "+ id +"\n" +"quiz id : "+ quizids +"\n"+ "score : "+ scores, Toast.LENGTH_SHORT).show();

                    getUserName(id, quizids, scores, date);


                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void getUserName(final String ids, final String qids , final int scr, final String date){

        reference3.child("user_info").child(ids).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {

                    username = dataSnapshot.child("student_name").getValue(String.class);
                    String imageUrl = dataSnapshot.child("image_Url").getValue(String.class);


                    list.add(new LeaderBoardData(ids, username, scr, imageUrl, date,qids));

                    Collections.sort(list, Collections.reverseOrder(new Comparator<LeaderBoardData>() {
                        @Override
                        public int compare(LeaderBoardData leaderBoardData, LeaderBoardData t1) {
                            return leaderBoardData.score - t1.score;
                        }
                    }));


                    recyclerView.setAdapter(leaderBoardAdapter);

                    leaderBoardAdapter.notifyDataSetChanged();

                    //    progressDialog.dismiss();



                }catch (Exception e){
                    e.printStackTrace();
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }



    private void getPlayerImage(){
        DatabaseReference databaseReference;
        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("user_info").child(userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String imageUrl = dataSnapshot.child("image_Url").getValue(String.class);


                Picasso.with(getApplicationContext())
                        .load(imageUrl)
                        .placeholder(R.drawable.userimg)
                        .fit()
                        .transform(transformation)
                        .into(userIcon);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getPlayerName(){
        DatabaseReference reference4;

        reference4 = FirebaseDatabase.getInstance().getReference();
        reference4.child("user_info").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                    String student_name = dataSnapshot.child(userid).child("student_name").getValue().toString();


                    leaderdata.setText(""+student_name);


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


