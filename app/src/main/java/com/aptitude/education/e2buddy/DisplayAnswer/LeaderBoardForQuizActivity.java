package com.aptitude.education.e2buddy.DisplayAnswer;

import android.app.ProgressDialog;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.aptitude.education.e2buddy.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LeaderBoardForQuizActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    LeaderBoardAdapter leaderBoardAdapter;
    List<LeaderBoardData> list;
    String  userid,quizdate,userids,quizids, username, yourscore ,playername,value;
    int scores;
    TextView score,tvscore,textViewRank,leaderdata,coin, tvrank;
    Transformation transformation;
    List<String> stringList;
    ImageView userIcon;
    ProgressDialog progressDialog;
    DatabaseReference databaseReference;
    ValueEventListener valueEventListener;


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

        databaseReference = FirebaseDatabase.getInstance().getReference();
        progressDialog = new ProgressDialog(LeaderBoardForQuizActivity.this);
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

        getPlayerImage();

        list = new ArrayList<>();
        stringList = new ArrayList<>();
        leaderBoardAdapter = new LeaderBoardAdapter(getApplicationContext(), list, userid,textViewRank);

          databaseReference.child("daily_user_credit").addListenerForSingleValueEvent(new ValueEventListener() {
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



        valueEventListener = databaseReference.child("daily_user_credit").child(userid).child(quizdate).child(value).addValueEventListener(new ValueEventListener() {
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
        Query lastQuery = databaseReference.child("daily_user_credit").child(id).child(quizdate).orderByKey().limitToLast(1);

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

        databaseReference.child("user_info").child(ids).addListenerForSingleValueEvent(new ValueEventListener() {
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
                    progressDialog.dismiss();



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

        valueEventListener = databaseReference.child("user_info").child(userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String imageUrl = dataSnapshot.child("image_Url").getValue(String.class);
                String student_name = dataSnapshot.child("student_name").getValue().toString();
                leaderdata.setText(""+student_name);

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
    protected void onPause() {
        super.onPause();
        databaseReference.removeEventListener(valueEventListener);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}


