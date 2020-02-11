package com.aptitude.education.e2buddy.DisplayAnswer;

import android.app.ProgressDialog;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aptitude.education.e2buddy.Question.PlayerTotalScore;
import com.aptitude.education.e2buddy.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class OverAllLeaderBoardActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    LeaderAdapter leaderBoardAdapter;
    List<LeaderBoardData> list;
    String userid, username, yourscore,userids,totalscore,student_name;
    int userscore;
    TextView tvscore, leaderdata,coin, score,tvrank,imageView;
    ProgressDialog progressDialog;
    Transformation transformation;
    ArrayList<String> uids;
    long totalquiz;
    ImageView userIcon;
    List<String> rank;
    FirebaseAuth auth;
    DatabaseReference databaseReference;
    ValueEventListener valueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_over_all_leader_board);

        recyclerView = findViewById(R.id.leaderboard);
        tvscore = findViewById(R.id.qscore);
        leaderdata = findViewById(R.id.tvdata);
        coin = findViewById(R.id.coinsds);
        tvrank = findViewById(R.id.r);
        imageView = findViewById(R.id.img);
        score = findViewById(R.id.textView13);
        userIcon = findViewById(R.id.e2buddy);

        auth = FirebaseAuth.getInstance();
        final FirebaseUser user = auth.getCurrentUser();
        userid = user.getUid();

        progressDialog = new ProgressDialog(OverAllLeaderBoardActivity.this);
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Data Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);
        progressDialog.setMax(100);
        progressDialog.show();

        transformation = new RoundedTransformationBuilder()
                .borderColor(getResources().getColor(R.color.white))
                .borderWidthDp(0)
                .cornerRadiusDp(50)
                .oval(false)
                .build();


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        databaseReference = FirebaseDatabase.getInstance().getReference();

        getPlayerName();
        getPlayerImage();
        list = new ArrayList<>();
        rank = new ArrayList<>();
        uids = new ArrayList<>();
        leaderBoardAdapter = new LeaderAdapter(OverAllLeaderBoardActivity.this, list,userid, imageView);

        databaseReference.child("final_Points").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot data : dataSnapshot.getChildren()){

                    try {
                        userids = data.getKey();
                        userscore = Integer.parseInt(data.child("final_score").getValue().toString());
                        getUserName(userids,userscore);

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


        leaderBoardAdapter.setOnItemClickListener(new LeaderBoardAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                LeaderBoardData leaderBoardData = list.get(position);
                String user = leaderBoardData.getUserid();
                final int scoresss = list.get(position).getScore();
                showAlertDialog(itemView, user, position);

            }
        });


       getPlayerScore();
    }

    private void getUserName(final String id, final int score){

        databaseReference.child("user_info").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                username = dataSnapshot.child("student_name").getValue(String.class);
                String imageUrl = dataSnapshot.child("image_Url").getValue(String.class);


                list.add(new LeaderBoardData(id, username, score,imageUrl));


                Collections.sort(list, Collections.reverseOrder(new Comparator<LeaderBoardData>() {
                    @Override
                    public int compare(LeaderBoardData leaderBoardData, LeaderBoardData t1) {

                        return leaderBoardData.score - t1.score;
                    }
                }));



                recyclerView.setAdapter(leaderBoardAdapter);
                progressDialog.dismiss();

                //getRank(score);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void showAlertDialog(View view, String uid, int position) {

        final ProgressDialog progressDialog;
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.layout_custom_dialog, null);
        final TextView tvtotalquiz = alertLayout.findViewById(R.id.totalquiz);
        final TextView score = alertLayout.findViewById(R.id.scores);
        final TextView userrank = alertLayout.findViewById(R.id.userrank);
        final TextView goodjob = alertLayout.findViewById(R.id.goodjob);
        final TextView textView = alertLayout.findViewById(R.id.hj);

        progressDialog = new ProgressDialog(OverAllLeaderBoardActivity.this);
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Data Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);
        progressDialog.setMax(100);
        progressDialog.show();

        AlertDialog.Builder alert = new AlertDialog.Builder(OverAllLeaderBoardActivity.this, R.style.CustomDialogTheme);

        alert.setView(alertLayout);

        databaseReference.child("final_Points").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                totalscore = dataSnapshot.child("final_score").getValue().toString();

                score.setText("Total Score : " + totalscore);

                progressDialog.dismiss();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        valueEventListener = databaseReference.child("daily_user_credit").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                totalquiz = dataSnapshot.getChildrenCount();

                tvtotalquiz.setText("Total Quiz : "+ totalquiz);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        if (position==0){

            userrank.setText("1");
            goodjob.setText("Excellent Job!!");

        }
        if (position==1){

            userrank.setText("2");
            goodjob.setText("Excellent Job!!");

        } if (position==2){

            userrank.setText("3");
            goodjob.setText("Excellent Job!!");

        } if (position==3){

            userrank.setText("4");
            goodjob.setText("Good Job!!");


        } if (position==4){

            userrank.setText("5");
            goodjob.setText("Good Job!!");

        } if (position==5){

            userrank.setText("6");
            goodjob.setText("Good Job!!");

        } if (position==6){

            userrank.setText("7");
            goodjob.setText("Good Job!!");

        } if (position==7){

            userrank.setText("8");
            goodjob.setText("Good Job!!");

        } if (position ==8){
            userrank.setText("9");
            goodjob.setText("Good Job!!");

        } if (position ==9){

            userrank.setText("10");
            goodjob.setText("Good Job!!");
        }

        alert.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog dialog = alert.create();
        dialog.show();
    }


    public void getPlayerScore() {
        PlayerTotalScore playerTotalScore = new PlayerTotalScore(databaseReference, userid, score);
        playerTotalScore.getPlayerScore();
    }

    private void getPlayerName(){
        valueEventListener = databaseReference.child("user_info").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                    student_name = dataSnapshot.child(userid).child("student_name").getValue().toString();
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


    private void getPlayerImage(){

        valueEventListener = databaseReference.child("user_info").child(userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String imageUrl = dataSnapshot.child("image_Url").getValue(String.class);
                Picasso.get()
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



    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseReference.removeEventListener(valueEventListener);
    }

}
