package com.aptitude.education.e2buddy.Quiz_Tournament;
import android.app.ProgressDialog;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.aptitude.education.e2buddy.DisplayAnswer.LeaderBoardData;
import com.aptitude.education.e2buddy.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Transformation;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class Quiz_Tournament_Result_Activity extends AppCompatActivity {

    RecyclerView recyclerView;
    Quiz_Tournament_Result_Adapter quiz_tournament_result_adapter;
    List<LeaderBoardData> list;
    String username, userids, tournament_quiz_date, current_date;
    int userscore;
    TextView tvscore, leaderdata, coin, score, tvplayername, imageView, imgcup, tvplayerscore;
    ProgressDialog progressDialog;
    Transformation transformation;
    ImageView userIcon, playerimg;
    DatabaseReference databaseReference;

    String[] tournamentDates = {
            "20-10-2019", "23-10-2019", "27-10-2019",
            "30-10-2019", "03-11-2019", "06-11-2019",
            "10-11-2019", "13-11-2019", "17-11-2019",
            "20-11-2019", "24-11-2019", "27-11-2019",
            "01-12-2019", "04-12-2019", "08-12-2019",
            "11-12-2019", "15-12-2019", "18-12-2019",
            "22-12-2019", "25-12-2019", "29-12-2019",
            "01-01-2020", "05-01-2020", "08-01-2020",
            "12-01-2020", "15-01-2020", "19-01-2020",
            "22-01-2020", "26-01-2020", "29-01-2020",
            "02-02-2020", "05-02-2020", "09-02-2020",
            "12-02-2020", "16-02-2020", "19-02-2020",
            "23-02-2020", "26-02-2020", "01-03-2020",

            "04-03-2020", "08-03-2020", "11-03-2020",
            "15-03-2020", "18-03-2020", "22-03-2020",
            "25-03-2020", "29-03-2020", "01-04-2020",
            "05-04-2020", "08-04-2020", "12-04-2020",
            "15-04-2020", "19-04-2020", "22-04-2020",
            "26-04-2020", "29-04-2020", "03-05-2020",
            "06-05-2020", "10-05-2020", "13-05-2020",
            "17-05-2020", "20-05-2020", "24-05-2020",
            "27-05-2020", "31-05-2020"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz__tournament__result_);

        recyclerView = findViewById(R.id.leaderboard);
        tvscore = findViewById(R.id.qscore);
        leaderdata = findViewById(R.id.tvdata);
        coin = findViewById(R.id.coinsds);
        tvplayername = findViewById(R.id.tvplayername);
        imageView = findViewById(R.id.img);
        score = findViewById(R.id.textView13);
        userIcon = findViewById(R.id.e2buddy);
        playerimg = findViewById(R.id.playerimg);
        imgcup = findViewById(R.id.imgcup);
        tvplayerscore= findViewById(R.id.tvplayerscore);

        databaseReference = FirebaseDatabase.getInstance().getReference();

            getPreviousTournamentQuizDate();


        progressDialog = new ProgressDialog(Quiz_Tournament_Result_Activity.this);
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

        list = new ArrayList<>();



    }

    private void getPreviousTournamentQuizDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        current_date = sdf.format(new Date());

        for (int i =1; i<=3;i++){

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
            try {
                Date current_date1 = simpleDateFormat.parse(current_date);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(current_date1);
                calendar.add(Calendar.DATE, -i);
                String result_date = simpleDateFormat.format(calendar.getTime());
                for (int j=0; j<tournamentDates.length;j++){
                    if (result_date.equals(tournamentDates[j])){
                        tournament_quiz_date = tournamentDates[j];
                      //  Toast.makeText(getApplicationContext(), "jhon  "+ tournament_quiz_date,Toast.LENGTH_SHORT).show();

                        databaseReference.child("tournament_date_joiners").child(tournament_quiz_date).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    userids = dataSnapshot1.getKey();
                                      getUserids(userids, tournament_quiz_date);
                                }
                            }


                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        break;
                    }
                }
                if (result_date.equals(tournament_quiz_date)){
                    break;
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
    }

    private void getUserids(final String userid, final String tournament_quiz_date){

        databaseReference.child("tournament_user_credit").child(userid).child(tournament_quiz_date).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                    userscore = Integer.parseInt(dataSnapshot.child("credit_points").getValue().toString());
                    getUserName(userid, userscore, tournament_quiz_date);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void getUserName(final String id, final int score, final String tournament_quiz_date) {

        Log.e("deekekumar", id);
        databaseReference.child("user_info").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                username = dataSnapshot.child("student_name").getValue(String.class);
                String imageUrl = dataSnapshot.child("image_Url").getValue(String.class);

                list.add(new LeaderBoardData(id, username, score, imageUrl));
                Collections.sort(list, Collections.reverseOrder(new Comparator<LeaderBoardData>() {
                    @Override
                    public int compare(LeaderBoardData leaderBoardData, LeaderBoardData t1) {

                        return leaderBoardData.score - t1.score;
                    }
                }));

                quiz_tournament_result_adapter = new Quiz_Tournament_Result_Adapter(Quiz_Tournament_Result_Activity.this, list, playerimg, imgcup, tvplayername,tournament_quiz_date, tvplayerscore);
                recyclerView.setAdapter(quiz_tournament_result_adapter);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.gc();
        finish();


    }
}