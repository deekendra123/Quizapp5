package com.aptitude.education.e2buddy.Quiz_Tournament;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.aptitude.education.e2buddy.Intro.SessionManager;
import com.aptitude.education.e2buddy.Question.HomeNevActivity;
import com.aptitude.education.e2buddy.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class ResultActivity extends AppCompatActivity {

    DatabaseReference databaseReference;
    SessionManager sessionManager;
    String playerId, playerName, playerEmail, playerImageUrl, daily_quiz_id, tournament_quiz_id;
    Button bthome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result3);
        bthome = findViewById(R.id.bthome);

        sessionManager = new SessionManager(ResultActivity.this);

        HashMap<String, String> user = sessionManager.getData();

        playerId = user.get(SessionManager.KEY_PLAYER_ID);
        playerName = user.get(SessionManager.KEY_PLAYER_NAME);
        playerEmail = user.get(SessionManager.KEY_PLAYER_EMAIL);
        playerImageUrl = user.get(SessionManager.KEY_PLAYER_IMAGE_URL);
        daily_quiz_id = user.get(SessionManager.KEY_DAILY_QUIZ_ID);
        tournament_quiz_id = user.get(SessionManager.KEY_TOURNAMENT_QUIZ_ID);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        bthome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HomeNevActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        System.gc();
        Intent intent = new Intent(getApplicationContext(), HomeNevActivity.class);
        startActivity(intent);
        finish();


    }



}
