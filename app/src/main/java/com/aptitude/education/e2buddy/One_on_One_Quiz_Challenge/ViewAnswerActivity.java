package com.aptitude.education.e2buddy.One_on_One_Quiz_Challenge;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.aptitude.education.e2buddy.Intro.CheckInternet;
import com.aptitude.education.e2buddy.R;
import com.aptitude.education.e2buddy.ViewData.AnswerViewForOneOnOne;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ViewAnswerActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    SharedPreferences sharedPreferences;
    String quizid,quizname,userid,quizdate,username;
    DatabaseReference databaseReference3;
    DatabaseReference databaseReference2;
    SharedPreferences sharedPref;
    String questionid, question, option1, option2,option3,option4, answer,description;
    String rec_id, not_id;
    String question_id,userans;
    List<AnswerViewForOneOnOne> list;
    AdapterForViewAnswerr adapterForViewAnswerr;
    ProgressDialog progressDialog;
    ImageView userIcon;
    TextView tvusername,tvanswer,tvCorrectAnswer;
    int count = 0;
    Transformation transformation;

    long notification_hr,notification_min,notification_sec;
    String sender_name,notification_id,receiver_user_id,sender_id;
    SharedPreferences pref;
    SharedPreferences.Editor editor1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_answer);
        recyclerView = findViewById(R.id.answerrecy);
        userIcon = findViewById(R.id.userimage);
        tvusername = findViewById(R.id.user);
        tvanswer = findViewById(R.id.tvans);
        tvCorrectAnswer = findViewById(R.id.tvcorrectans);



        CheckInternet checkInternet = new CheckInternet(getApplicationContext());
        checkInternet.checkConnection();


        Typeface type = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
        tvusername.setTypeface(type);
        tvanswer.setTypeface(type);
        tvCorrectAnswer.setTypeface(type);

        sharedPreferences = getSharedPreferences("quizpref", MODE_PRIVATE);
        quizid = sharedPreferences.getString("quizid", "");
        quizname = sharedPreferences.getString("quizname", "");
        userid = sharedPreferences.getString("userid", "");
        quizdate = sharedPreferences.getString("quizdate","");
        username = sharedPreferences.getString("username","");

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Data Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);
        progressDialog.setMax(100);
        progressDialog.show();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        list = new ArrayList<>();

        transformation = new RoundedTransformationBuilder()
                .borderColor(getResources().getColor(R.color.white))
                .borderWidthDp(1)
                .cornerRadiusDp(50)
                .oval(false)
                .build();

        tvusername.setText(""+username);


        sharedPref = getSharedPreferences("receiver_info", MODE_PRIVATE);

        not_id = sharedPref.getString("notification_ids","");
        rec_id = sharedPref.getString("receiver_id","");

        databaseReference3 = FirebaseDatabase.getInstance().getReference();
        databaseReference2 = FirebaseDatabase.getInstance().getReference();
        getPlayerImage();

        try {


            getSenderAnswer(not_id,userid);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        // Toast.makeText(getApplicationContext(), "deekes  "+ not_id,Toast.LENGTH_SHORT ).show();
    }

    private void getPlayerImage(){
        DatabaseReference databaseReference;
        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("user_info").child(userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String imageUrl = dataSnapshot.child("image_Url").getValue(String.class);


                Picasso.get()
                        .load(imageUrl)
                        .placeholder(R.drawable.userimg)
                        .fit()
                        .transform(transformation)
                        .into(userIcon);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getSenderAnswer(String not_id, String sender_id){

        databaseReference3.child("1V1-user_answer").child(not_id).child(sender_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if (dataSnapshot.exists()){
                    for (DataSnapshot data : dataSnapshot.getChildren()){

                        try {

                            question_id = data.getKey();
                            userans = data.child("useranswer").getValue().toString();

                            getQuizQuestionsforSender(question_id, userans);

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getQuizQuestionsforSender(final String question_id, final String user_answer){

        databaseReference2.child("questions").child(question_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // questionid = dataSnapshot.getKey();
                question = dataSnapshot.child("Question").getValue(String.class);
                option1 = dataSnapshot.child("Option1").getValue(String.class);
                option2 = dataSnapshot.child("Option2").getValue(String.class);
                option3 = dataSnapshot.child("Option3").getValue(String.class);
                option4 = dataSnapshot.child("Option4").getValue(String.class);
                answer = dataSnapshot.child("Answer").getValue(String.class);
                description = dataSnapshot.child("Description").getValue(String.class);

                list.add(new AnswerViewForOneOnOne(
                        question_id,  question, answer, user_answer
                ));
                if (user_answer.equals(answer)){
                    count = count+1;
                }
                tvCorrectAnswer.setText(count+"/10");

                adapterForViewAnswerr = new AdapterForViewAnswerr(getApplicationContext(), list);
                recyclerView.setAdapter(adapterForViewAnswerr);

                progressDialog.dismiss();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver((mMessageReceiver),
                new IntentFilter("NotificationData"));
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);

    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            sender_name = intent.getExtras().getString("sender_name");
            receiver_user_id = intent.getExtras().getString("receiver_user_id");
            notification_id = intent.getExtras().getString("notification_id");
            sender_id = intent.getExtras().getString("sender_id");

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

        }
    };


    private void showAlertDialog(long notification_hr, long notification_min, long notification_sec){
        Animation zoomout;
        final DatabaseReference databaseReference1;
        DatabaseReference reference;

        databaseReference1 = FirebaseDatabase.getInstance().getReference("user_notifications");
        reference = FirebaseDatabase.getInstance().getReference();

        final LayoutInflater inflater = LayoutInflater.from(ViewAnswerActivity.this);
        final View alertLayout = inflater.inflate(R.layout.layout_custom_dialog_notification, null);
        final TextView msg = alertLayout.findViewById(R.id.hj);
        final TextView tvaccept = alertLayout.findViewById(R.id.tvaccept);
        final TextView tvreject = alertLayout.findViewById(R.id.tvrej);
        final ImageView smileFace = alertLayout.findViewById(R.id.smileface);

        zoomout = AnimationUtils.loadAnimation(ViewAnswerActivity.this, R.anim.zoomin);
        smileFace.setAnimation(zoomout);


        msg.setText(sender_name+" has challenged you.");

        final android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(ViewAnswerActivity.this, R.style.CustomDialogTheme);

        alert.setView(alertLayout);
        alert.setCancelable(false);

        final android.app.AlertDialog dialog = alert.create();

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

                databaseReference1.child(receiver_user_id).child(notification_id).child("status").setValue("no");

                dialog.dismiss();

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
            }
        }, x);

    /*    long delayInMillis1 = 15000;
        Timer timer1 = new Timer();
        timer1.schedule(new TimerTask() {
            @Override
            public void run() {
                dialog.dismiss();

            }
        }, delayInMillis1);*/

        reference.child("user_notifications").child(receiver_user_id).child(notification_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                    String cancel_status = dataSnapshot.child("cancel_status").getValue().toString();
                    // Toast.makeText(getApplicationContext(), ""+cancel_status,Toast.LENGTH_SHORT).show();

                    if (cancel_status.equals("yes")) {
                        dialog.dismiss();
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


        dialog.show();

    }


    private void alertDialog(){
        Animation zoomout;
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        final LayoutInflater inflater = LayoutInflater.from(ViewAnswerActivity.this);
        final View alertLayout = inflater.inflate(R.layout.layout_custom_dialor_start_quiz, null);
        final TextView msg = alertLayout.findViewById(R.id.hj);
        final ImageView smileFace = alertLayout.findViewById(R.id.smileface);
        final  ImageView img1 = alertLayout.findViewById(R.id.dot1);
        final  ImageView img2 = alertLayout.findViewById(R.id.dot2);
        final  ImageView img3 = alertLayout.findViewById(R.id.dot3);

        zoomout = AnimationUtils.loadAnimation(ViewAnswerActivity.this, R.anim.zoomin);
        smileFace.setAnimation(zoomout);

        final Animation myFadeInAnimation = AnimationUtils.loadAnimation(ViewAnswerActivity.this, R.anim.fade_anim);
        img1.startAnimation(myFadeInAnimation);
        img2.startAnimation(myFadeInAnimation);
        img3.startAnimation(myFadeInAnimation);

        msg.setText("Please wait for "+ sender_name+ " to start the game.");

        final android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(ViewAnswerActivity.this, R.style.CustomDialogTheme);

        alert.setView(alertLayout);
        alert.setCancelable(false);

        final android.app.AlertDialog dialog = alert.create();


        databaseReference.child("user_notifications").child(receiver_user_id).child(notification_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                    boolean playstatus = Boolean.parseBoolean(dataSnapshot.child("play_status").getValue().toString());

                    if (playstatus==true){
                        dialog.dismiss();

                        pref = getSharedPreferences("sender_info",MODE_PRIVATE);
                        editor1 = pref.edit();
                        editor1.putString("s_id", sender_id);
                        editor1.putString("not_id",notification_id);
                        editor1.putString("sender_name",sender_name);
                        editor1.commit();

                        Intent intent = new Intent(ViewAnswerActivity.this, LoaderForReceiverActivity.class);
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
