package com.aptitude.education.e2buddy.School_Quiz;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.aptitude.education.e2buddy.Question.PlayerTotalScore;
import com.aptitude.education.e2buddy.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

public class StudentProfileActivity extends AppCompatActivity {

    TextView tvuserName, tvuserEmail, tvDob, tvScore, tvSchoolName, tvSchoolCode;
    Button editProfile;
    ImageView userIcon;
    FirebaseAuth auth;
    String userid;
    DatabaseReference databaseReference;
    Transformation transformation;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);

        ImageButton closeDialog = findViewById(R.id.fullscreen_dialog_close);
        userIcon = findViewById(R.id.userIcon);
        editProfile = findViewById(R.id.editProfile);
        tvDob = findViewById(R.id.userDoB);
        tvuserEmail = findViewById(R.id.userEmail);
        tvuserName = findViewById(R.id.userName);
        tvScore = findViewById(R.id.tvscore);
        tvSchoolName = findViewById(R.id.userSchoolName);
        tvSchoolCode = findViewById(R.id.school_code);

        progressDialog = new ProgressDialog(StudentProfileActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Data Loading...");
        progressDialog.show();


        auth = FirebaseAuth.getInstance();
        final FirebaseUser user = auth.getCurrentUser();
        userid = user.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        transformation = new RoundedTransformationBuilder()
                .borderColor(getResources().getColor(R.color.white))
                .borderWidthDp(0)
                .cornerRadiusDp(50)
                .oval(false)
                .build();

        getUserProfile(userid);
        getTotalQuizAndScore();
        closeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               onBackPressed();
                finish();
            }
        });


        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.gc();
               Intent intent = new Intent(StudentProfileActivity.this, EditStudentProfileActivity.class);
               startActivity(intent);

            }
        });

    }

    private void getTotalQuizAndScore() {
        PlayerTotalScore playerTotalScore = new PlayerTotalScore(databaseReference, userid, tvScore);
        playerTotalScore.getPlayerScore();
    }


    private void getUserProfile(String userid) {

        databaseReference.child("user_info").child(userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                    String student_name = dataSnapshot.child("student_name").getValue().toString();
                    String imageUrl = dataSnapshot.child("image_Url").getValue(String.class);
                    String userEmail = dataSnapshot.child("email").getValue().toString();

                    tvuserName.setText(""+student_name);
                    tvuserEmail.setText(""+userEmail);

                    Picasso.get()
                            .load(imageUrl)
                            .placeholder(R.mipmap.ic_launcher)
                            .fit()
                            .transform(transformation)
                            .into(userIcon);

                    if (dataSnapshot.hasChild("dob")){
                        String dob = dataSnapshot.child("dob").getValue().toString();
                        if (!dob.equals("")){
                            tvDob.setText(""+dob);

                        }else {
                            tvDob.setVisibility(View.GONE);
                        }

                    }
                    else {
                        tvDob.setVisibility(View.GONE);
                     }


                    if (dataSnapshot.hasChild("school_code")){
                        String school_code= dataSnapshot.child("school_code").getValue().toString();
                        tvSchoolCode.setText(""+school_code);

                        databaseReference.child("school_codes").child(school_code).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                try {
                                    String s_name = dataSnapshot.child("school_name").getValue().toString();
                                    tvSchoolName.setText(""+s_name);
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
                        tvSchoolName.setVisibility(View.GONE);
                        tvSchoolCode.setVisibility(View.GONE);

                    }
                    progressDialog.dismiss();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }

}