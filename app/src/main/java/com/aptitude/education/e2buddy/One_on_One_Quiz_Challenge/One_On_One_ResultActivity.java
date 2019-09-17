package com.aptitude.education.e2buddy.One_on_One_Quiz_Challenge;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.aptitude.education.e2buddy.Intro.CheckInternet;
import com.aptitude.education.e2buddy.Question.HomeFragment;
import com.aptitude.education.e2buddy.Question.HomeNevActivity;
import com.aptitude.education.e2buddy.Question.StartQuizActivity;
import com.aptitude.education.e2buddy.R;
import com.aptitude.education.e2buddy.ViewData.Questionids;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
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

public class One_On_One_ResultActivity extends AppCompatActivity {

    TextView  user1score, user2score, user1name,user2name,tvloosewin,tv1,tv2;
    Button viewresult,bthome;
    SharedPreferences sharedPreferences;
    String quizid,quizname,userid,quizdate,username;
    SharedPreferences sharedPref;
    String rec_name, rec_id, not_id;
    DatabaseReference databaseReference;
    DatabaseReference databaseReference1;
    DatabaseReference databaseReference2;
    DatabaseReference databaseReference3;
    DatabaseReference databaseReference4;
    SharedPreferences preferences;
    String sender_score, receiver_score,q_id;
    int sender_count = 0;
    int sender_count1 = 0;
    int sender_count2 = 0;
    int rec_count = 0;
    int rec_count1 = 0;
    String questionid, question, option1, option2,option3,option4, answer,description;

    String question_id,userans;
    int timeleft;

    List<Questionids> list;
    ImageView imgReceiver, imgSender;
    Transformation transformation;
    Animation fromtop;

    long notification_hr,notification_min,notification_sec;
    String sender_name,notification_id,receiver_user_id,sender_id;
    SharedPreferences pref;
    SharedPreferences.Editor editor1;
    AdView mAdView;
    InterstitialAd mInterstitialAd;
    AdRequest adRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one__on__one__result);

        user1score = findViewById(R.id.tvuser1score);
        user2score = findViewById(R.id.tvuser2score);
        user1name = findViewById(R.id.tvuser1name);
        user2name = findViewById(R.id.tvuser2name);

        imgReceiver = findViewById(R.id.user2);
        imgSender = findViewById(R.id.user1);
        tvloosewin = findViewById(R.id.textView3);

        viewresult = findViewById(R.id.button3);
        bthome = findViewById(R.id.button4);
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        mAdView = (AdView) findViewById(R.id.adView);
        adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mInterstitialAd = new InterstitialAd(One_On_One_ResultActivity.this);

        mInterstitialAd.setAdUnitId(getString(R.string.Interstitial_ad));

        mInterstitialAd.loadAd(adRequest);

        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }
            }
        });

        CheckInternet checkInternet = new CheckInternet(getApplicationContext());
        checkInternet.checkConnection();


        Typeface type = (Typeface) Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
        user1name.setTypeface(type);
        user2name.setTypeface(type);
        user1score.setTypeface(type);
        user2score.setTypeface(type);
        tvloosewin.setTypeface(type);
        viewresult.setTypeface(type);
        bthome.setTypeface(type);
        tv1.setTypeface(type);
        tv2.setTypeface(type);

        fromtop = AnimationUtils.loadAnimation(this, R.anim.fromtop);
        tvloosewin.setAnimation(fromtop);

        list = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference1 = FirebaseDatabase.getInstance().getReference();
        databaseReference2 = FirebaseDatabase.getInstance().getReference();
        databaseReference3 = FirebaseDatabase.getInstance().getReference();
        databaseReference4 = FirebaseDatabase.getInstance().getReference();

        sharedPreferences = getSharedPreferences("quizpref", MODE_PRIVATE);
        quizid = sharedPreferences.getString("quizid", "");
        quizname = sharedPreferences.getString("quizname", "");
        userid = sharedPreferences.getString("userid", "");
        quizdate = sharedPreferences.getString("quizdate","");
        username = sharedPreferences.getString("username","");

        transformation = new RoundedTransformationBuilder()
                .borderColor(getResources().getColor(R.color.white))
                .borderWidthDp(1)
                .cornerRadiusDp(50)
                .oval(false)
                .build();


        sharedPref = getSharedPreferences("receiver_info", MODE_PRIVATE);

        not_id = sharedPref.getString("notification_ids","");
        rec_id = sharedPref.getString("receiver_id","");
        rec_name = sharedPref.getString("receiver_name","");

        getReceiverImage();
        getSenderImage();

        getReceiverName(rec_id);
        getSenderAnswer(not_id,userid);
        getReceiverAnswer(not_id,rec_id);

        user1name.setText(""+username);


        viewresult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ViewAnswerActivity.class);
                startActivity(intent);
            }
        });

        bthome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HomeNevActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

    }

    private void getReceiverImage(){
        DatabaseReference databaseReference;
        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("user_info").child(rec_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String imageUrl = dataSnapshot.child("image_Url").getValue(String.class);

                Picasso.with(getApplicationContext())
                        .load(imageUrl)
                        .placeholder(R.drawable.userimg)
                        .fit()
                        .transform(transformation)
                        .into(imgReceiver);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getSenderImage(){
        DatabaseReference databaseReference;
        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("user_info").child(userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String imageUrl = dataSnapshot.child("image_Url").getValue(String.class);

                Picasso.with(getApplicationContext())
                        .load(imageUrl)
                        .placeholder(R.mipmap.ic_launcher)
                        .fit()
                        .transform(transformation)
                        .into(imgSender);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void getReceiverName(final String rec_id){

        databaseReference.child("user_info").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                rec_name = dataSnapshot.child(rec_id).child("student_name").getValue().toString();

                user2name.setText(""+rec_name);


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
                            timeleft = Integer.parseInt(data.child("timeleft").getValue().toString());

                            getQuizQuestionsforSender(question_id, userans, timeleft);
                        }catch (Exception e){

                        }


                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void getReceiverAnswer(String not_id, String rec_id){

        databaseReference4.child("1V1-user_answer").child(not_id).child(rec_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if (dataSnapshot.exists()){
                    for (DataSnapshot data : dataSnapshot.getChildren()){

                        try {
                            question_id = data.getKey();
                            userans = data.child("useranswer").getValue().toString();
                            timeleft = Integer.parseInt(data.child("timeleft").getValue().toString());

                            getQuizQuestionsforReceiver(question_id, userans, timeleft);
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

    private void getQuizQuestionsforReceiver(String question_id, final String user_answer, final int timeleft){

        databaseReference2.child("questions").child(question_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                questionid = dataSnapshot.getKey();
                question = dataSnapshot.child("Question").getValue(String.class);
                option1 = dataSnapshot.child("Option1").getValue(String.class);
                option2 = dataSnapshot.child("Option2").getValue(String.class);
                option3 = dataSnapshot.child("Option3").getValue(String.class);
                option4 = dataSnapshot.child("Option4").getValue(String.class);
                answer = dataSnapshot.child("Answer").getValue(String.class);
                description = dataSnapshot.child("Description").getValue(String.class);

                if (answer.equals(user_answer)) {
                    rec_count = rec_count + timeleft * 3;
                    rec_count1 = rec_count1 + 1;

                }

                //        Toast.makeText(getApplicationContext(),""+rec_count1+ "\n"+sender_count1,Toast.LENGTH_SHORT).show();
//                tvreceiver.setText("receiver score : " + rec_count + "\n" + "Question Answered : " + rec_count1+"/10");


                user2score.setText(rec_count1+"/10");



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void getQuizQuestionsforSender(String question_id, final String user_answer, final int timeleft){

        databaseReference2.child("questions").child(question_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                questionid = dataSnapshot.getKey();
                question = dataSnapshot.child("Question").getValue(String.class);
                option1 = dataSnapshot.child("Option1").getValue(String.class);
                option2 = dataSnapshot.child("Option2").getValue(String.class);
                option3 = dataSnapshot.child("Option3").getValue(String.class);
                option4 = dataSnapshot.child("Option4").getValue(String.class);
                answer = dataSnapshot.child("Answer").getValue(String.class);
                description = dataSnapshot.child("Description").getValue(String.class);

                if (answer.equals(user_answer)){
                    sender_count = sender_count+timeleft*3;
                    sender_count1 = sender_count1+1;
                }


                if (sender_count1>rec_count1){
                    tvloosewin.setText("YOU WIN");
                }
                else if (sender_count1==rec_count1){
                    tvloosewin.setText("It's a TIE");
                }
                else {
                    tvloosewin.setText("YOU LOSE");
                }



                //  user1score.setText("sender score : "+sender_count + "\n" + "Question Answered : "+ sender_count1+"/10");
                user1score.setText(sender_count1+"/10");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(getApplicationContext(), HomeNevActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();

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

        final LayoutInflater inflater = LayoutInflater.from(One_On_One_ResultActivity.this);
        final View alertLayout = inflater.inflate(R.layout.layout_custom_dialog_notification, null);
        final TextView msg = alertLayout.findViewById(R.id.hj);
        final TextView tvaccept = alertLayout.findViewById(R.id.tvaccept);
        final TextView tvreject = alertLayout.findViewById(R.id.tvrej);
        final ImageView smileFace = alertLayout.findViewById(R.id.smileface);

        zoomout = AnimationUtils.loadAnimation(One_On_One_ResultActivity.this, R.anim.zoomin);
        smileFace.setAnimation(zoomout);

        Typeface type = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/DroidSerif-Regular.ttf");
        msg.setTypeface(type);

        msg.setText(sender_name+" has challenged you.");

        final android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(One_On_One_ResultActivity.this, R.style.CustomDialogTheme);

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
        final LayoutInflater inflater = LayoutInflater.from(One_On_One_ResultActivity.this);
        final View alertLayout = inflater.inflate(R.layout.layout_custom_dialor_start_quiz, null);
        final TextView msg = alertLayout.findViewById(R.id.hj);
        final ImageView smileFace = alertLayout.findViewById(R.id.smileface);
        final  ImageView img1 = alertLayout.findViewById(R.id.dot1);
        final  ImageView img2 = alertLayout.findViewById(R.id.dot2);
        final  ImageView img3 = alertLayout.findViewById(R.id.dot3);

        zoomout = AnimationUtils.loadAnimation(One_On_One_ResultActivity.this, R.anim.zoomin);
        smileFace.setAnimation(zoomout);

        final Animation myFadeInAnimation = AnimationUtils.loadAnimation(One_On_One_ResultActivity.this, R.anim.fade_anim);
        img1.startAnimation(myFadeInAnimation);
        img2.startAnimation(myFadeInAnimation);
        img3.startAnimation(myFadeInAnimation);

        Typeface type = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/DroidSerif-Regular.ttf");
        msg.setTypeface(type);

        msg.setText("Please wait for "+ sender_name+ " to start the game.");

        final android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(One_On_One_ResultActivity.this, R.style.CustomDialogTheme);

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

                        Intent intent = new Intent(One_On_One_ResultActivity.this, LoaderForReceiverActivity.class);
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
