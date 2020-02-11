package com.aptitude.education.e2buddy.Quiz_Tournament;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.aptitude.education.e2buddy.Intro.SessionManager;
import com.aptitude.education.e2buddy.Question.HomeNevActivity;
import com.aptitude.education.e2buddy.Question.PlayerTotalScore;
import com.aptitude.education.e2buddy.R;
import com.aptitude.education.e2buddy.ViewData.TournamentRegisterStudentData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Tournament_Participation_Dialog extends DialogFragment {

    private Tournament_Participation_Dialog.Callback callback;
    Button btJoin;
    TextView tvPoins;
    DatabaseReference databaseReference;
    SessionManager sessionManager;
    PlayerTotalScore playerTotalScore;
    String playerId, playerName, playerEmail, playerImageUrl, daily_quiz_id, tournament_quiz_id;

    public static Tournament_Participation_Dialog newInstance() {
        return new Tournament_Participation_Dialog();
    }

    public void setCallback(Tournament_Participation_Dialog.Callback callback) {
        this.callback = callback;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(android.app.DialogFragment.STYLE_NORMAL, R.style.FullscreenDialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tournament_participation_dialog, container, false);
        ImageButton closeDialog = view.findViewById(R.id.fullscreen_dialog_close);
        tvPoins = view.findViewById(R.id.tvscore);
        btJoin = view.findViewById(R.id.btenter);

        sessionManager = new SessionManager(getActivity());

        HashMap<String, String> user = sessionManager.getData();

        playerId = user.get(SessionManager.KEY_PLAYER_ID);
        playerName = user.get(SessionManager.KEY_PLAYER_NAME);
        playerEmail = user.get(SessionManager.KEY_PLAYER_EMAIL);
        playerImageUrl = user.get(SessionManager.KEY_PLAYER_IMAGE_URL);
        daily_quiz_id = user.get(SessionManager.KEY_DAILY_QUIZ_ID);
        tournament_quiz_id = user.get(SessionManager.KEY_TOURNAMENT_QUIZ_ID);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        playerTotalScore = new PlayerTotalScore(databaseReference, playerId, tvPoins);
        playerTotalScore.getPlayerScore();

        closeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
              //  getActivity().finish();

            }
        });

        btJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playerTotalScore.final_score>=200){

                    insertTournamentRegisterStudent();
                    insertTournamentDateJoiners();
                    insertFinalPoints();

                    Intent intent = new Intent(getActivity(), HomeNevActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    getActivity().startActivity(intent);
                    getActivity().finish();
                }
                else {
                    showAlertDialog();
                }
            }
        });


        return view;
    }



    private void insertTournamentRegisterStudent() {
        databaseReference = FirebaseDatabase.getInstance().getReference("tournament_register_students");
        TournamentRegisterStudentData tournamentRegisterStudentData = new TournamentRegisterStudentData(200, false);
        databaseReference.child(playerId).child(tournament_quiz_id).setValue(tournamentRegisterStudentData);
    }
    private void insertTournamentDateJoiners() {
        databaseReference = FirebaseDatabase.getInstance().getReference("tournament_date_joiners");
        databaseReference.child(tournament_quiz_id).child(playerId).child("point_spent").setValue(200);

    }
    private void insertFinalPoints() {

        final DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference();

        databaseReference1.child("final_Points").child(playerId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                try {
                    if (dataSnapshot.hasChild("tournament_point_spent")){

                        int tournament_point = Integer.parseInt(dataSnapshot.child("tournament_point_spent").getValue().toString());
                        databaseReference1.child("final_Points").child(playerId).child("tournament_point_spent").setValue(tournament_point+200);
                    }
                    else {
                        databaseReference1.child("final_Points").child(playerId).child("tournament_point_spent").setValue(200);
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

    public interface Callback {

        void onActionClick(String name);

    }



    public void showAlertDialog() {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View alertLayout = inflater.inflate(R.layout.custom_dailog_tournament, null);
        final TextView btearn = alertLayout.findViewById(R.id.btearn);

        final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity(), R.style.CustomDialogTheme);

        alert.setView(alertLayout);


        final AlertDialog dialog = alert.create();

        btearn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), HomeNevActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        dialog.show();
    }
}
