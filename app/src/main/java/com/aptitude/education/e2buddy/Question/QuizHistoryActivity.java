package com.aptitude.education.e2buddy.Question;

import android.app.ProgressDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aptitude.education.e2buddy.Intro.SessionManager;
import com.aptitude.education.e2buddy.R;
import com.aptitude.education.e2buddy.ViewData.HistoryView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class QuizHistoryActivity extends AppCompatActivity {

    RecyclerView historyRecyclerView;
    ProgressDialog progressDialog;
    TextView tvhistory;
    ImageView historyImage;
    DatabaseReference databaseReference;
    List<HistoryView> historyViewList;
    String playerId, playerImageUrl;
    UserQuizHistoryAdapter userQuizHistoryAdapter;
    String quiz_date;
    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_history);

        historyRecyclerView = findViewById(R.id.historyRecyclerView) ;
        tvhistory= findViewById(R.id.tvhist);
        historyImage = findViewById(R.id.image);

        sessionManager = new SessionManager(QuizHistoryActivity.this);

        HashMap<String, String> user = sessionManager.getData();

        playerId = user.get(SessionManager.KEY_PLAYER_ID);
        playerImageUrl = user.get(SessionManager.KEY_PLAYER_IMAGE_URL);

        progressDialog = new ProgressDialog(QuizHistoryActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Data Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);
        progressDialog.setMax(100);
        progressDialog.show();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(QuizHistoryActivity.this, LinearLayoutManager.VERTICAL, false);
        historyRecyclerView.setLayoutManager(layoutManager);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        getQuizHistory();

    }

    public interface Callback {

        void onActionClick(String name);

    }


    private void getQuizHistory() {


        databaseReference.child("daily_user_credit").child(playerId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {

                    try {
                        String id = data.getKey();
                        getLastQuiz(id);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void getLastQuiz(final String q_id){

        Query lastQuery = databaseReference.child("daily_user_credit").child(playerId).child(q_id).orderByKey().limitToLast(1);


        historyViewList = new ArrayList<>();
        userQuizHistoryAdapter = new UserQuizHistoryAdapter(QuizHistoryActivity.this, historyViewList,playerId);

        lastQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {


                    String id = dataSnapshot1.getKey();
                    String date = dataSnapshot1.child("date_time").getValue().toString();
                    String coins = dataSnapshot1.child("credit_points").getValue().toString();
                    String corr_answer = dataSnapshot1.child("correct_answers").getValue().toString();





                    if (date.equals(null)){

                        historyImage.setVisibility(View.VISIBLE);
                        tvhistory.setVisibility(View.VISIBLE);
                        tvhistory.setText("No Quiz has Taken Yet");
                        progressDialog.dismiss();
                    }
                    else {

                        if (q_id.contains("-2020")){
                            Collections.sort(historyViewList, byDate);

                            historyViewList.add(new HistoryView(q_id, date, id, coins, corr_answer));

                            historyRecyclerView.setAdapter(userQuizHistoryAdapter);
                            progressDialog.dismiss();

                        }

                       }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    static final Comparator<HistoryView> byDate = new Comparator<HistoryView>() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        public int compare(HistoryView ord1, HistoryView ord2) {
            Date d1 = null;
            Date d2 = null;
            try {
                d1 = sdf.parse(ord1.getId());
                d2 = sdf.parse(ord2.getId());
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


            return (d1.getTime() > d2.getTime() ? -1 : 1);     //descending
            //  return (d1.getTime() > d2.getTime() ? 1 : -1);     //ascending
        }
    };




}
