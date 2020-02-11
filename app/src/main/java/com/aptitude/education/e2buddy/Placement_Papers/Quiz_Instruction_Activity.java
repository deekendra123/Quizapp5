package com.aptitude.education.e2buddy.Placement_Papers;

import android.content.Intent;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.aptitude.education.e2buddy.Intro.SessionManager;
import com.aptitude.education.e2buddy.R;
import com.aptitude.education.e2buddy.ViewData.BookmarkData;
import com.aptitude.education.e2buddy.ViewData.QuestionView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import in.shadowfax.proswipebutton.ProSwipeButton;

public class Quiz_Instruction_Activity extends AppCompatActivity {

    TextView tv1, tv2, tv3, tv4, tv7;
    ProSwipeButton proSwipeBtn;
    SessionManager sessionManager;
    String playerId, playerName, playerEmail, playerImageUrl, q_id, questionid;
    DatabaseReference databaseReference;
    int position=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz__instruction_);

        tv1 = findViewById(R.id.textView6);
        tv2 = findViewById(R.id.textView5);
        tv3 = findViewById(R.id.textView7);
        tv4 = findViewById(R.id.textView8);


        tv7 = findViewById(R.id.textView11);

         proSwipeBtn = findViewById(R.id.startquiz);


        sessionManager = new SessionManager(this );

        HashMap<String, String> user = sessionManager.getData();

        playerId = user.get(SessionManager.KEY_PLAYER_ID);
        playerName = user.get(SessionManager.KEY_PLAYER_NAME);
        playerEmail = user.get(SessionManager.KEY_PLAYER_EMAIL);
        playerImageUrl = user.get(SessionManager.KEY_PLAYER_IMAGE_URL);

        tv2.setText("1) Number of Questions : 15");

        tv3.setText("2) Earn Coins for every correct Answers");
        tv4.setText("3) Use Coins to unlock Premium features");

        tv7.setText("Time Allotted : 20 min");

        databaseReference = FirebaseDatabase.getInstance().getReference();


        proSwipeBtn.setSwipeDistance(0.6f);
        proSwipeBtn.setOnSwipeListener(new ProSwipeButton.OnSwipeListener() {
            @Override
            public void onSwipeConfirm() {
                // user has swiped the btn. Perform your async operation now
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // task success! show TICK icon in ProSwipeButton
                        proSwipeBtn.showResultIcon(true); // false if task failed

                        databaseReference.child("placemenPaper_user_answer").child(playerId).child("quantitativeAbility").child("quiz1").removeValue();

                        getQuizId();
                        Intent intent = new Intent(getApplicationContext(), PlacementQuizQuestionsActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }, 1000);
            }
        });


    }

    private void getQuizId(){
        databaseReference.child("placementPaper").child("quantitativeAbility").child("quiz1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                    q_id = dataSnapshot1.getKey();
                    getQuizQuestions(q_id);

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void getQuizQuestions(String q_id) {


        databaseReference.child("placementPaper").child("quantitativeAbility").child("quiz1").child(q_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                    questionid = dataSnapshot.getKey();

                    BookmarkData bookmarkData = new BookmarkData(false, position);
                    databaseReference.child("placemenPaper_user_answer").child(playerId).child("quantitativeAbility").child("quiz1").child(questionid).setValue(bookmarkData);

                    position++;
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
