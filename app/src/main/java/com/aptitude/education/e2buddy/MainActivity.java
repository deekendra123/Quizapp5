package com.aptitude.education.e2buddy;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.aptitude.education.e2buddy.DisplayAnswer.LeaderBoardAdapter;
import com.aptitude.education.e2buddy.DisplayAnswer.LeaderBoardData;
import com.aptitude.education.e2buddy.Question.StartQuizActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    LeaderBoardAdapter leaderBoardAdapter;
    List<LeaderBoardData> list;
    String  userid,quizdate,userids,quizids, username;
    int scores;
    TextView textViewRank;
    ProgressDialog progressDialog;
    DatabaseReference databaseReference;
    FirebaseAuth auth;
    AppCompatButton btPlayQuiz;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.leaderboard);

        auth = FirebaseAuth.getInstance();
        final FirebaseUser user = auth.getCurrentUser();
        userid = user.getUid();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        quizdate = sdf.format(new Date());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        list = new ArrayList<>();

        leaderBoardAdapter = new LeaderBoardAdapter(MainActivity.this, list, userid,textViewRank);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("daily_user_credit").child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(quizdate)){
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
                }
                else {

                    progressDialog.dismiss();
                    linearLayout.setVisibility(View.VISIBLE);
                    btPlayQuiz.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MainActivity.this, StartQuizActivity.class);
                            intent.putExtra("quiz_date", quizdate);
                            startActivity(intent);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void getQuiz(final String id){


        Query lastQuery = databaseReference.child("daily_user_credit").child(id).child(quizdate).orderByChild("credit_points").limitToLast(1);

        lastQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                lastQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        quizids = dataSnapshot.getKey();

                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                            try {

                                String date = dataSnapshot1.getKey();

                                scores = Integer.parseInt(dataSnapshot1.child("credit_points").getValue().toString());

                                Log.e("credit_points", String.valueOf(scores));

                                getUserName(id, quizids, scores, date);


                            }catch (Exception e){
                                e.printStackTrace();
                            }

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                quizids = dataSnapshot.getKey();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    try {

                        String date = dataSnapshot1.getKey();

                        scores = Integer.parseInt(dataSnapshot1.child("credit_points").getValue().toString());

                        Log.e("credit_points", String.valueOf(scores));

                        getUserName(id, quizids, scores, date);


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

    private void getUserName(final String ids, final String qids , final int scr, final String date){

        databaseReference.child("user_info").child(ids).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {

                    username = dataSnapshot.child("student_name").getValue(String.class);
                    String imageUrl = dataSnapshot.child("image_Url").getValue(String.class);

                    list.add(new LeaderBoardData(ids, username, scr, imageUrl, date,qids));
                    recyclerView.setAdapter(leaderBoardAdapter);

                }catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
