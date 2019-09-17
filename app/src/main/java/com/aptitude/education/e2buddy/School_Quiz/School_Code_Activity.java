package com.aptitude.education.e2buddy.School_Quiz;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aptitude.education.e2buddy.Question.HomeNevActivity;
import com.aptitude.education.e2buddy.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class School_Code_Activity extends AppCompatActivity {

    EditText schoolCode;
    ImageView submit;
    Animation fromtop;
    TextView skip, text,error;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    ImageView icon;
    FirebaseAuth auth;
    String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school__code_);

        schoolCode = findViewById(R.id.editText11);
        submit = findViewById(R.id.btsubmit);
        skip = findViewById(R.id.buttonSkip);
        icon = findViewById(R.id.logo);
        text = findViewById(R.id.t1);
        error = findViewById(R.id.error);

        Typeface type = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
        text.setTypeface(type);
        schoolCode.setTypeface(type);
        skip.setTypeface(type);
        //tvCode.setTypeface(type);

        fromtop = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fromtop);
        icon.setAnimation(fromtop);

        auth = FirebaseAuth.getInstance();
        final FirebaseUser user = auth.getCurrentUser();
        userid = user.getUid();

        Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade);
                    text.startAnimation(animation);
                    sleep(2000);
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }

            }
        };
        thread.start();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (schoolCode.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Enter Your School Code", Toast.LENGTH_SHORT).show();
                }
                else {
                    databaseReference = FirebaseDatabase.getInstance().getReference();

                    databaseReference.child("school_codes").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                                String school_code = dataSnapshot1.getKey();

                              //  Toast.makeText(getApplicationContext(), "School Code  "+ school_code, Toast.LENGTH_SHORT).show();

                                if (schoolCode.getText().toString().equalsIgnoreCase(school_code)){

                                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

                                    databaseReference.child("user_info").child(userid).child("school_code").setValue(school_code);

                                    System.gc();
                                    Intent intent = new Intent(getApplicationContext(), Student_Details_Activity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();

                                }
                                else {
                                    error.setText("Invalid School Code");
                          //          Toast.makeText(getApplicationContext(), "Invalid School Code", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                System.gc();
                Intent intent = new Intent(getApplicationContext(), HomeNevActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode==KeyEvent.KEYCODE_BACK)
            Toast.makeText(getApplicationContext(), "Enter school code or Skip",
                    Toast.LENGTH_LONG).show();

        return false;
    }

}
