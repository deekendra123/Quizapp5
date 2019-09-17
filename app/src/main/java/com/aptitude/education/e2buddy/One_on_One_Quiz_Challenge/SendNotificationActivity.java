package com.aptitude.education.e2buddy.One_on_One_Quiz_Challenge;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aptitude.education.e2buddy.Intro.CheckInternet;
import com.aptitude.education.e2buddy.R;
import com.aptitude.education.e2buddy.ViewData.NotificationData;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SendNotificationActivity extends AppCompatActivity {

    String user_id;
    EditText msg;
    Button send;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String quizid,quizname,userid,quizdate,name;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_notification);
        msg = findViewById(R.id.editText);
        send = findViewById(R.id.button2);
        user_id = getIntent().getStringExtra("user_id");
        name = getIntent().getStringExtra("user_nam");

        CheckInternet checkInternet = new CheckInternet(getApplicationContext());
        checkInternet.checkConnection();


        sharedPreferences = getApplicationContext().getSharedPreferences("quizpref", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        quizid = sharedPreferences.getString("quizid", "");
        quizname = sharedPreferences.getString("quizname", "");
        userid = sharedPreferences.getString("userid", "");
        quizdate = sharedPreferences.getString("quizdate","");

        Toast.makeText(getApplicationContext(), " "+ user_id,Toast.LENGTH_SHORT).show();

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendNotification();
            }
        });
    }
    private void sendNotification(){
        databaseReference = FirebaseDatabase.getInstance().getReference("user_notifications");
        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference();
        String message = msg.getText().toString();

        String id = databaseReference.push().getKey();

        NotificationData notificationData = new NotificationData(message, userid);

        databaseReference.child(user_id).child("notifications").child(id).setValue(notificationData);
       databaseReference1.child("user_notifications").child(user_id).child("name").setValue(name);


    }
}
