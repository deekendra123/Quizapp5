package com.aptitude.education.e2buddy.Question;

import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.aptitude.education.e2buddy.ViewData.InsertScoreData;
import com.aptitude.education.e2buddy.ViewData.InsertTotalScoreData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PlayerTotalScore {
    DatabaseReference databaseReference;
    String userId;
    TextView tvPoints;
    public int daily_quiz_total_score;
    public int tournament_point_spent;
    public int tournament_point_earn;
    public int final_score;
    public int category_total_score;

    public PlayerTotalScore(DatabaseReference databaseReference, String userId, TextView tvPoints) {
        this.databaseReference = databaseReference;
        this.userId = userId;
        this.tvPoints = tvPoints;
    }


    public void getPlayerScore() {

        final DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("final_Points").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {

                    if (dataSnapshot.hasChild(userId)){

                        databaseReference.child("final_Points").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                try {

                                    if (dataSnapshot.hasChild("category_total_score")){
                                        daily_quiz_total_score = Integer.parseInt(dataSnapshot.child("daily_quiz_total_score").getValue().toString());
                                        tournament_point_spent = Integer.parseInt(dataSnapshot.child("tournament_point_spent").getValue().toString());
                                        tournament_point_earn = Integer.parseInt(dataSnapshot.child("tournament_point_earn").getValue().toString());
                                        category_total_score = Integer.parseInt(dataSnapshot.child("category_total_score").getValue().toString());
                                        final_score = daily_quiz_total_score-tournament_point_spent+tournament_point_earn+category_total_score;
                                        tvPoints.setText(""+final_score);
                                        databaseReference1.child("final_Points").child(userId).child("final_score").setValue(final_score);

                                    }
                                    else {
                                        daily_quiz_total_score = Integer.parseInt(dataSnapshot.child("daily_quiz_total_score").getValue().toString());
                                        tournament_point_spent = Integer.parseInt(dataSnapshot.child("tournament_point_spent").getValue().toString());
                                        tournament_point_earn = Integer.parseInt(dataSnapshot.child("tournament_point_earn").getValue().toString());
                                        final_score = daily_quiz_total_score-tournament_point_spent+tournament_point_earn;
                                        tvPoints.setText(""+final_score);
                                        databaseReference1.child("final_Points").child(userId).child("final_score").setValue(final_score);

                                    }

                                }catch (Exception e){
                                    e.printStackTrace();
                                }


                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                    }
                    else {

                        databaseReference1.child("daily_user_total_score").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                if (dataSnapshot.hasChild(userId)) {

                                    daily_quiz_total_score = Integer.parseInt(dataSnapshot.child(userId).child("total_score").getValue().toString());
                                    final_score = daily_quiz_total_score;
                                    tvPoints.setText(""+ final_score);
                                    Log.e("deeke", "jhon");

                                    InsertScoreData insertTotalScoreData = new InsertScoreData(final_score,0,final_score,0,0);
                                    databaseReference1.child("final_Points").child(userId).setValue(insertTotalScoreData);

                                }
                                else {
                                    InsertScoreData insertTotalScoreData = new InsertScoreData(0,0,0,0,0);
                                    databaseReference1.child("final_Points").child(userId).setValue(insertTotalScoreData);
                                    tvPoints.setText("" + 0);

                                    Log.e("deeke", "ram");

                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
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
}