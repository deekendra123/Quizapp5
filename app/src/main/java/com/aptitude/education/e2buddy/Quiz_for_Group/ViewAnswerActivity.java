package com.aptitude.education.e2buddy.Quiz_for_Group;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.aptitude.education.e2buddy.R;
import com.aptitude.education.e2buddy.ViewData.AnswerViewForGroup;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewAnswerActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences sharedPreferences1;
    String quizid,quizname,userid,quizdate,username;
    String notification_id;
    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    List<AnswerViewForGroup> list;
    DatabaseReference databaseReference1;
    DatabaseReference databaseReference2;
    String question_id, userans, questionid, question, option1, option2,option3,option4, answer,description;
    AdapterForViewPlayerAnswer adapterForViewAnswerr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_answer2);
        recyclerView = findViewById(R.id.answerrecy);


        sharedPreferences1 = getSharedPreferences("groupnotification", Context.MODE_PRIVATE);
        notification_id = sharedPreferences1.getString("group_notification", "");


        Toast.makeText(getApplicationContext(), " deeke  "+notification_id + "\n"+userid,Toast.LENGTH_SHORT).show();

        sharedPreferences = getSharedPreferences("quizpref", MODE_PRIVATE);
        quizid = sharedPreferences.getString("quizid", "");
        quizname = sharedPreferences.getString("quizname", "");
        userid = sharedPreferences.getString("userid", "");
        quizdate = sharedPreferences.getString("quizdate","");
        username = sharedPreferences.getString("username","");

        databaseReference1 = FirebaseDatabase.getInstance().getReference();
        databaseReference2 = FirebaseDatabase.getInstance().getReference();

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Data Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);
        progressDialog.setMax(100);
        progressDialog.show();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        list = new ArrayList<>();

        try {


            getPlayerAnswer(notification_id,userid);
        }
        catch (Exception e){
            e.printStackTrace();
        }




    }

    private void getPlayerAnswer(String notification_id, String userid) {
        databaseReference1.child("group_user_answer").child(notification_id).child(userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if (dataSnapshot.exists()){
                    for (DataSnapshot data : dataSnapshot.getChildren()){


                        question_id = data.getKey();
                        userans = data.child("useranswer").getValue().toString();

                        getQuizQuestionsforPlayer(question_id, userans);
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void getQuizQuestionsforPlayer(final String question_id, final String user_answer) {

        databaseReference2.child("1V1-Quiz-Questions").child(question_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // questionid = dataSnapshot.getKey();
                question = dataSnapshot.child("Question").getValue(String.class);
                option1 = dataSnapshot.child("Option1").getValue(String.class);
                option2 = dataSnapshot.child("Option2").getValue(String.class);
                option3 = dataSnapshot.child("Option3").getValue(String.class);
                option4 = dataSnapshot.child("Option4").getValue(String.class);
                answer = dataSnapshot.child("Answer").getValue(String.class);
                description = dataSnapshot.child("Description").getValue(String.class);

                list.add(new AnswerViewForGroup(
                        question_id,  question, answer, user_answer
                ));
                adapterForViewAnswerr = new AdapterForViewPlayerAnswer(getApplicationContext(), list);
                recyclerView.setAdapter(adapterForViewAnswerr);

                progressDialog.dismiss();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
