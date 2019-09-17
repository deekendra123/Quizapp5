package com.aptitude.education.e2buddy.One_on_One_Quiz_Challenge;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aptitude.education.e2buddy.Intro.CheckInternet;
import com.aptitude.education.e2buddy.Question.HomeNevActivity;
import com.aptitude.education.e2buddy.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class OneonOneStartQuiz extends AppCompatActivity {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    DatabaseReference databaseRef,databaseRef1;
    DatabaseReference databaseReference1;
    long notification_hr,notification_min,notification_sec;
    SharedPreferences sharedPreferences;
    String quizid,quizname, userid,quizdate,sender_name,notification_id,receiver_user_id,sender_id, username;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oneon_one_start_quiz);

        CheckInternet checkInternet = new CheckInternet(getApplicationContext());
        checkInternet.checkConnection();

        databaseReference1 = FirebaseDatabase.getInstance().getReference("user_notifications");
        databaseRef = FirebaseDatabase.getInstance().getReference();
        databaseRef1 = FirebaseDatabase.getInstance().getReference();


        sharedPreferences = getSharedPreferences("quizpref", MODE_PRIVATE);
        quizid = sharedPreferences.getString("quizid", "");
        userid = sharedPreferences.getString("userid", "");
        quizdate = sharedPreferences.getString("quizdate","");
        username = sharedPreferences.getString("username","");

        sender_name = getIntent().getStringExtra("sender_name");
        notification_id = getIntent().getStringExtra("notification_id");
        receiver_user_id = getIntent().getStringExtra("receiver_user_id");
        sender_id = getIntent().getStringExtra("sender_id");


        if (sender_name!=null){

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

            databaseReference.child("user_notifications").child(receiver_user_id).child(notification_id)
                    .child("sender_name").child(sender_name).child(sender_id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    try {
                        notification_hr = Long.parseLong(dataSnapshot.child("hour").getValue().toString());
                        notification_min = Long.parseLong(dataSnapshot.child("minut").getValue().toString());
                        notification_sec = Long.parseLong(dataSnapshot.child("second").getValue().toString());
                        showAlertDialog(notification_hr,notification_min,notification_sec);

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

        /*Toast.makeText(getApplicationContext(), "sender id : " + sender_id + "\n" +"sender_name : "+ sender_name
                + "\n"+ "receiver_user_id : " + receiver_user_id + "\n" +
                "notification_id : "+ notification_id ,Toast.LENGTH_SHORT).show();
*/
        databaseReference = FirebaseDatabase.getInstance().getReference();


    }

    private void showAlertDialog(long notification_hr, long notification_min, long notification_sec){
        Animation zoomout;
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        final LayoutInflater inflater = LayoutInflater.from(OneonOneStartQuiz.this);
        final View alertLayout = inflater.inflate(R.layout.layout_custom_dialog_notification, null);
        final TextView msg = alertLayout.findViewById(R.id.hj);
        final TextView tvaccept = alertLayout.findViewById(R.id.tvaccept);
        final TextView tvreject = alertLayout.findViewById(R.id.tvrej);
        final ImageView smileFace = alertLayout.findViewById(R.id.smileface);

        zoomout = AnimationUtils.loadAnimation(OneonOneStartQuiz.this, R.anim.zoomin);
        smileFace.setAnimation(zoomout);

        Typeface type = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/Roboto-Regular.ttf");
        msg.setTypeface(type);

        msg.setText(sender_name+" has challenged you.");

        final android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(OneonOneStartQuiz.this, R.style.CustomDialogTheme);

        alert.setView(alertLayout);
        alert.setCancelable(false);

        final android.app.AlertDialog dialog = alert.create();

        reference.child("user_notifications").child(receiver_user_id).child(notification_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                    String cancel_status = dataSnapshot.child("cancel_status").getValue().toString();
                    //Toast.makeText(getApplicationContext(), ""+cancel_status,Toast.LENGTH_SHORT).show();

                    if (cancel_status.equals("yes")) {
                       dialog.cancel();
                       Toast.makeText(getApplicationContext(),sender_name +" has canceled the game",Toast.LENGTH_SHORT).show();
                       Intent intent = new Intent(getApplicationContext(), HomeNevActivity.class);
                       startActivity(intent);
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


        tvaccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                // showAlert();
                databaseReference1.child(receiver_user_id).child(notification_id).child("status").setValue("yes");
                alertDialog();

            }
        });

        tvreject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                databaseReference1.child(receiver_user_id).child(notification_id).child("status").setValue("no");
                Intent intent = new Intent(getApplicationContext(), HomeNevActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat hour = new SimpleDateFormat("HH");
        SimpleDateFormat minut = new SimpleDateFormat("mm");
        SimpleDateFormat second = new SimpleDateFormat("ss");

        long hour1 = Long.parseLong(hour.format(calendar.getTime()));
        long minut1 = Long.parseLong(minut.format(calendar.getTime()));
        long second1 = Long.parseLong(second.format(calendar.getTime()));

        long sender_time = notification_hr*60*60+notification_min*60+notification_sec;
        long rec_time = hour1*60*60+minut1*60+second1;

        long delayInMillis = 15000-(rec_time*1000-sender_time*1000);
        long x = Math.abs(delayInMillis);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                dialog.dismiss();
               /* Intent intent = new Intent(OneonOneStartQuiz.this, HomeNevActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);*/
            }
        }, x);

        try {
            dialog.show();
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    private void alertDialog(){
        Animation zoomout;
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        final LayoutInflater inflater = LayoutInflater.from(OneonOneStartQuiz.this);
        final View alertLayout = inflater.inflate(R.layout.layout_custom_dialor_start_quiz, null);
        final TextView msg = alertLayout.findViewById(R.id.hj);
        final ImageView smileFace = alertLayout.findViewById(R.id.smileface);
        final  ImageView img1 = alertLayout.findViewById(R.id.dot1);
        final  ImageView img2 = alertLayout.findViewById(R.id.dot2);
        final  ImageView img3 = alertLayout.findViewById(R.id.dot3);

        zoomout = AnimationUtils.loadAnimation(OneonOneStartQuiz.this, R.anim.zoomin);
        smileFace.setAnimation(zoomout);

        final Animation myFadeInAnimation = AnimationUtils.loadAnimation(OneonOneStartQuiz.this, R.anim.fade_anim);
        img1.startAnimation(myFadeInAnimation);
        img2.startAnimation(myFadeInAnimation);
        img3.startAnimation(myFadeInAnimation);

        Typeface type = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/DroidSerif-Regular.ttf");
        msg.setTypeface(type);

        msg.setText("Please wait for "+ sender_name+ " to start the game.");

        final android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(OneonOneStartQuiz.this, R.style.CustomDialogTheme);

        alert.setView(alertLayout);
        alert.setCancelable(false);

        final android.app.AlertDialog dialog = alert.create();


        databaseReference.child("user_notifications").child(userid).child(notification_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                    boolean playstatus = Boolean.parseBoolean(dataSnapshot.child("play_status").getValue().toString());

                    if (playstatus==true){
                        dialog.dismiss();

                        pref = getSharedPreferences("sender_info",MODE_PRIVATE);
                        editor = pref.edit();
                        editor.putString("s_id", sender_id);
                        editor.putString("not_id",notification_id);
                        editor.putString("sender_name",sender_name);
                        editor.commit();

                        Intent intent = new Intent(OneonOneStartQuiz.this, LoaderForReceiverActivity.class);
                        startActivity(intent);

                    }
                }catch (Exception e){

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        dialog.show();

    }


}
