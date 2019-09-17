package com.aptitude.education.e2buddy.Quiz_for_Group;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.aptitude.education.e2buddy.R;
import com.aptitude.education.e2buddy.ViewData.GroupResultData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ResultActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    SharedPreferences sharedPreferences;
    String notification_id;
    DatabaseReference databaseReference1;
    DatabaseReference databaseReference2;
    DatabaseReference databaseReference3;
    DatabaseReference databaseReference4;
    String player_id,question_id,userans,questionid, question, option1, option2,option3,option4, answer,description,player_name;
    int timeleft;
    AdapterForGroupResult adapterForGroupResult;
    List<GroupResultData> list;
    int count = 0;
    int count1 = 0;
    Button viewAnswer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        recyclerView = findViewById(R.id.groupresult);
        viewAnswer = findViewById(R.id.groupanswer);
        sharedPreferences = getSharedPreferences("groupnotification", Context.MODE_PRIVATE);
        notification_id = sharedPreferences.getString("group_notification", "");

        list = new ArrayList<>();
     //   Toast.makeText(getApplicationContext(), "deeke "+ notification_id,Toast.LENGTH_SHORT).show();

        databaseReference1 = FirebaseDatabase.getInstance().getReference();
        databaseReference2 = FirebaseDatabase.getInstance().getReference();

        databaseReference3 = FirebaseDatabase.getInstance().getReference();
        databaseReference4 = FirebaseDatabase.getInstance().getReference();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapterForGroupResult = new AdapterForGroupResult(getApplicationContext(), list);


        getPlayerRank(notification_id);
        viewAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ViewAnswerActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getPlayerRank(String notification_id){

        databaseReference1.child("group_player_score").child(notification_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    String playerids = dataSnapshot1.getKey();
                    int playerscore = Integer.parseInt(dataSnapshot1.child("score").getValue().toString());
                    String student_name = dataSnapshot1.child("student_name").getValue().toString();
                    int question_answered = Integer.parseInt(dataSnapshot1.child("question_answered").getValue().toString());

                  //  Toast.makeText(getApplicationContext(), ""+ playerids+"\n"+playerscore+"\n"+student_name,Toast.LENGTH_SHORT).show();

                    list.add(new GroupResultData(student_name, playerscore , question_answered,playerids));


                    Collections.sort(list, Collections.reverseOrder(new Comparator<GroupResultData>() {
                        @Override
                        public int compare(GroupResultData groupResultData, GroupResultData t1) {
                            return groupResultData.getScore() - t1.getScore();
                        }
                    }));


                }



                recyclerView.setAdapter(adapterForGroupResult);

                adapterForGroupResult.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
