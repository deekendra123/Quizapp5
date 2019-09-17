package com.aptitude.education.e2buddy.Quiz_for_Group;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

public class StartQuizForGroupActivity extends AppCompatActivity {

    String sender_name,notification_id,receiver_user_id,sender_id,quizid,quizname,userid,quizdate;
    DatabaseReference databaseReference1;
    CountDownTimer countDownTimer ;
    SharedPreferences sharedPreferences;
    DatabaseReference databaseReference2;
    RecyclerView recyclerView;
    List<AcceptedUsersData> list;
    AdapterForRequestAcceptedUsers adapterForRequestAcceptedUsers;
    DatabaseReference databaseReference3;
    DatabaseReference databaseReference4;
    DatabaseReference databaseReference5;
    SharedPreferences preferences;
    SharedPreferences.Editor editor1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_quiz_for_group);
        recyclerView = findViewById(R.id.acceptedusers);

        databaseReference3 = FirebaseDatabase.getInstance().getReference();
        databaseReference4 = FirebaseDatabase.getInstance().getReference();
        databaseReference5 = FirebaseDatabase.getInstance().getReference();


        list = new ArrayList<>();
        databaseReference1 = FirebaseDatabase.getInstance().getReference("group_notifications");
        databaseReference2 = FirebaseDatabase.getInstance().getReference("quiz_request_accepted_users");

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        adapterForRequestAcceptedUsers = new AdapterForRequestAcceptedUsers(getApplicationContext(), list);

        sharedPreferences = getSharedPreferences("quizpref", MODE_PRIVATE);
        quizid = sharedPreferences.getString("quizid", "");
        quizname = sharedPreferences.getString("quizname", "");
        userid = sharedPreferences.getString("userid", "");
        quizdate = sharedPreferences.getString("quizdate","");


        sender_name = getIntent().getStringExtra("sender_name");
        notification_id = getIntent().getStringExtra("notification_id");
        receiver_user_id = getIntent().getStringExtra("receiver_user_id");
        sender_id = getIntent().getStringExtra("sender_id");



        preferences = getSharedPreferences("groupnotification", Context.MODE_PRIVATE);
        editor1 = preferences.edit();
        editor1.putString("group_notification", notification_id);
        editor1.commit();

    /*    Toast.makeText(getApplicationContext(), "sender id : " + sender_id + "\n" +"sender_name : "+ sender_name
                + "\n"+ "receiver_user_id : " + receiver_user_id + "\n" +
                "notification_id : "+ notification_id ,Toast.LENGTH_SHORT).show();
*/
        if (sender_name!=null){
            showAlertDialog();
        }

        getRequestAcceptedUsers(notification_id);

        getStartQuizStatus(notification_id,sender_id);

    }

    private void getStartQuizStatus(final String notification_id, String sender_id){

        databaseReference5.child("quiz_request_accepted_users").child(notification_id).child(sender_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                    String status = dataSnapshot.child("start_quiz").getValue().toString();

                 //   Toast.makeText(getApplicationContext(), ""+status,Toast.LENGTH_SHORT).show();

                    if (status.equals("true")){
                        final ProgressDialog dialog = new ProgressDialog(StartQuizForGroupActivity.this);
                        dialog.setTitle("Please wait...");
                        dialog.setMessage("You are being redirected to the Quiz");
                        dialog.setIndeterminate(true);
                        dialog.setCancelable(false);
                        dialog.show();

                        long delayInMillis = 7500;
                        Timer timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                dialog.dismiss();

                                Intent intent = new Intent(StartQuizForGroupActivity.this, QuizQuestionsForAdminActivity.class);
                                intent.putExtra("notification_id",notification_id);
                                startActivity(intent);
                            }
                        }, delayInMillis);
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Wait for other users ",Toast.LENGTH_SHORT).show();
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

    private void getRequestAcceptedUsers(String notification_id){

        databaseReference3.child("quiz_request_accepted_users").child(notification_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    list.clear();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        final String id = dataSnapshot1.getKey();

                      //  Toast.makeText(getApplicationContext(), "user id  " + id, Toast.LENGTH_SHORT).show();

                        databaseReference4.child("user_info").child(id).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                String student_name = dataSnapshot.child("student_name").getValue().toString();

                              //  Toast.makeText(getApplicationContext(), "name  " + student_name, Toast.LENGTH_SHORT).show();
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

    private void showAlertDialog(){

        final LayoutInflater inflater = LayoutInflater.from(StartQuizForGroupActivity.this);
        View alertLayout = inflater.inflate(R.layout.layout_custom_dialog_response, null);
        final TextView userrank = alertLayout.findViewById(R.id.userrank);
        final TextView msg = alertLayout.findViewById(R.id.hj);
        final Button reject = alertLayout.findViewById(R.id.btreject);
        final Button accept = alertLayout.findViewById(R.id.btaccept);

        android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(StartQuizForGroupActivity.this, R.style.CustomDialogTheme);

        alert.setView(alertLayout);
        alert.setCancelable(false);

        final android.app.AlertDialog dialog = alert.create();


        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getApplicationContext(), "Request Accepted!",Toast.LENGTH_SHORT).show();

                databaseReference2.child(notification_id).child(userid).child("status").setValue(Boolean.TRUE);
                dialog.dismiss();
            }
        });
        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Request Rejected!",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        countDownTimer =  new CountDownTimer(15000, 1000) {

            public void onTick(long millisUntilFinished) {
                userrank.setText("" +millisUntilFinished / 1000);

            }

            public void onFinish() {

                dialog.dismiss();

        //        Toast.makeText(getApplicationContext(), "Sorry!",Toast.LENGTH_SHORT).show();
            }
        }.start();

        dialog.show();
    }


}
