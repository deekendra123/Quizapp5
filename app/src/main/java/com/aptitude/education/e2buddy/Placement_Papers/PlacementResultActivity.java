package com.aptitude.education.e2buddy.Placement_Papers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.aptitude.education.e2buddy.DisplayAnswer.View_Answer_Dialog;
import com.aptitude.education.e2buddy.Intro.SessionManager;
import com.aptitude.education.e2buddy.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class PlacementResultActivity extends AppCompatActivity {

    TextView tvName, tvAnswerGiven, tvTimeTaken;
    Button btViewAnswer;
    ImageView imgHome;
    DatabaseReference databaseReference;
    SessionManager sessionManager;
    String playerId, playerName, playerEmail, playerImageUrl, q_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placement_result);
        btViewAnswer = findViewById(R.id.btViewAnswer);
        tvName = findViewById(R.id.tvName);
        imgHome = findViewById(R.id.imgHome);
        tvAnswerGiven = findViewById(R.id.tvAnswerGiven);
        tvTimeTaken = findViewById(R.id.tvTimeTaken);

        sessionManager = new SessionManager(this );

        HashMap<String, String> user = sessionManager.getData();

        playerId = user.get(SessionManager.KEY_PLAYER_ID);
        playerName = user.get(SessionManager.KEY_PLAYER_NAME);
        playerEmail = user.get(SessionManager.KEY_PLAYER_EMAIL);
        playerImageUrl = user.get(SessionManager.KEY_PLAYER_IMAGE_URL);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        tvName.setText("Accenture Placement Paper");
        getCorrectAnswer();

        btViewAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fm = getSupportFragmentManager();
                DialogFragment dialog = View_PlacementPaper_Answer_Dialog.newInstance();
                dialog.show(fm,"tag");

//                AnswerSheet answerSheet = new AnswerSheet();
//                answerSheet.setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
//                answerSheet.show(getSupportFragmentManager(), "quizapp2");



            }
        });

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    private void getCorrectAnswer(){
        databaseReference.child("placemenPaper_user_answer").child(playerId).child("quantitativeAbility").child("quiz1").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    String corr_ans = dataSnapshot.child("correct_answer").getValue().toString();
                    String time_left = dataSnapshot.child("timeleft").getValue().toString();

                    Log.e("deekeresult", corr_ans + "   "+ time_left);

                    tvAnswerGiven.setText(corr_ans+"/10");
                    tvTimeTaken.setText(""+time_left);

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
