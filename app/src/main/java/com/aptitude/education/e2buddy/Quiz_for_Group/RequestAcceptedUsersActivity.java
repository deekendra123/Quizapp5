package com.aptitude.education.e2buddy.Quiz_for_Group;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.aptitude.education.e2buddy.R;
import com.aptitude.education.e2buddy.ViewData.AcceptedUsersData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class RequestAcceptedUsersActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    AdapterForRequestAcceptedUsers adapterForRequestAcceptedUsers;
    List<AcceptedUsersData> list;
    DatabaseReference databaseReference;
    DatabaseReference databaseReference1;
    DatabaseReference databaseReference2;
    String notification_id;
    Button startquiz;
    SharedPreferences sharedPreferences;

    String quizid,userid,quizname,quizdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_accepted_users);
        recyclerView = findViewById(R.id.acceptedusers);
        startquiz = findViewById(R.id.btplayquiz);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference1 = FirebaseDatabase.getInstance().getReference();

        databaseReference2 = FirebaseDatabase.getInstance().getReference();

        sharedPreferences = getSharedPreferences("quizpref", MODE_PRIVATE);
        quizid = sharedPreferences.getString("quizid", "");
        quizname = sharedPreferences.getString("quizname", "");
        userid = sharedPreferences.getString("userid", "");
        quizdate = sharedPreferences.getString("quizdate","");


        list = new ArrayList<>();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        adapterForRequestAcceptedUsers = new AdapterForRequestAcceptedUsers(getApplicationContext(), list);

        notification_id = getIntent().getStringExtra("group_notification_id");

       // Toast.makeText(getApplicationContext(), "deeke"+ notification_id,Toast.LENGTH_SHORT).show();

        getRequestAcceptedUsers(notification_id);

        startquiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startQuizForAllUsers(notification_id);


                final ProgressDialog dialog = new ProgressDialog(RequestAcceptedUsersActivity.this);
                dialog.setTitle("Please wait...");
                dialog.setMessage("You are being redirected to the Quiz");
                dialog.setIndeterminate(true);
                dialog.setCancelable(false);
                dialog.show();

                long delayInMillis = 7700;
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        dialog.dismiss();

                        Intent intent = new Intent(getApplicationContext(), QuizQuestionsForAdminActivity.class);
                        intent.putExtra("notification_id",notification_id);
                        startActivity(intent);
                    }
                }, delayInMillis);


            }
        });
    }

    private void startQuizForAllUsers(String notification_id){
        databaseReference2.child("quiz_request_accepted_users").child(notification_id).child(userid).child("start_quiz").setValue(Boolean.TRUE);
    }
    private void getRequestAcceptedUsers(String notification_id){

        databaseReference.child("quiz_request_accepted_users").child(notification_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    list.clear();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        final String id = dataSnapshot1.getKey();

                       // Toast.makeText(getApplicationContext(), "user id  " + id, Toast.LENGTH_SHORT).show();

                        databaseReference1.child("user_info").child(id).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                String student_name = dataSnapshot.child("student_name").getValue().toString();

                               // Toast.makeText(getApplicationContext(), "name  " + student_name, Toast.LENGTH_SHORT).show();
                                list.add(new AcceptedUsersData(id, student_name));

                                recyclerView.setAdapter(adapterForRequestAcceptedUsers);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
