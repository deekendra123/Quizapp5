package com.aptitude.education.e2buddy.Question;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aptitude.education.e2buddy.Intro.CheckInternet;
import com.aptitude.education.e2buddy.One_on_One_Quiz_Challenge.LoaderForReceiverActivity;
import com.aptitude.education.e2buddy.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import javax.security.auth.callback.Callback;

public class FeedbackFullScreenDialog extends DialogFragment {
    private Callback callback;

    long notification_hr,notification_min,notification_sec;
    String sender_name,notification_id,receiver_user_id,sender_id;
    SharedPreferences pref;
    SharedPreferences.Editor editor1;

    AdView mAdView;
    static FeedbackFullScreenDialog newInstance() {
        return new FeedbackFullScreenDialog();
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullscreenDialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fullscreen_dialog, container, false);
        ImageView btSendFeedback = view.findViewById(R.id.sendfeedback);
        TextView tvname = view.findViewById(R.id.t1);
        TextView tvemail = view.findViewById(R.id.t2);
        TextView tvmsg = view.findViewById(R.id.t3);
        TextView tvE2buddy = view.findViewById(R.id.tve2buddy);
        final EditText etName = view.findViewById(R.id.editText11);
        final EditText etEmail = view.findViewById(R.id.editText12);
        final EditText etMsg = view.findViewById(R.id.editText13);
        ImageButton closeDialog = view.findViewById(R.id.fullscreen_dialog_close);

        mAdView = (AdView) view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        CheckInternet checkInternet = new CheckInternet(getActivity());
        checkInternet.checkConnection();


        Typeface type = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Regular.ttf");
        tvname.setTypeface(type);
        tvemail.setTypeface(type);
        tvmsg.setTypeface(type);
        tvE2buddy.setTypeface(type);
        etEmail.setTypeface(type);
        etName.setTypeface(type);
        etMsg.setTypeface(type);




        closeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.gc();
                dismiss();
            }
        });

        btSendFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String name, email, messgae;
                name = etName.getText().toString();
                email = etEmail.getText().toString();
                messgae = etMsg.getText().toString();


                if (name.isEmpty()) {
                    etName.setError("Name is required");
                    etName.requestFocus();
                    return;
                }
                else if (email.isEmpty()) {
                    etEmail.setError("Email is required");
                    etEmail.requestFocus();
                    return;
                }
                else if (messgae.isEmpty()){
                    etMsg.setError("Message is required");
                    etMsg.requestFocus();
                    return;
                }
                else {

                    ProgressDialog progressDialog;
                    progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setCancelable(true);
                    progressDialog.setMessage("Please Wait...");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setProgress(0);
                    progressDialog.setMax(100);
                    progressDialog.show();

                    Email email1 = new Email("e2buddy.quiz@gmail.com",
                            "E2buddy@123", email, messgae, "e2buddy.quiz@gmail.com", "e2buddy.quiz@gmail.com", name);
                    boolean success = email1.success();

                    if (success == true) {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Your Feedback Submitted Successfully", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });


        return view;
    }
    public interface Callback {

        void onActionClick(String name);

    }

    @Override
    public void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver((mMessageReceiver),
                new IntentFilter("NotificationData"));
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiver);

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

        final LayoutInflater inflater = LayoutInflater.from(getActivity());
        final View alertLayout = inflater.inflate(R.layout.layout_custom_dialog_notification, null);
        final TextView msg = alertLayout.findViewById(R.id.hj);
        final TextView tvaccept = alertLayout.findViewById(R.id.tvaccept);
        final TextView tvreject = alertLayout.findViewById(R.id.tvrej);
        final ImageView smileFace = alertLayout.findViewById(R.id.smileface);

        zoomout = AnimationUtils.loadAnimation(getActivity(), R.anim.zoomin);
        smileFace.setAnimation(zoomout);

        Typeface type = Typeface.createFromAsset(getActivity().getAssets(), "fonts/DroidSerif-Regular.ttf");
        msg.setTypeface(type);

        msg.setText(sender_name+" has challenged you.");

        final android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(getActivity(), R.style.CustomDialogTheme);

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
        final LayoutInflater inflater = LayoutInflater.from(getActivity());
        final View alertLayout = inflater.inflate(R.layout.layout_custom_dialor_start_quiz, null);
        final TextView msg = alertLayout.findViewById(R.id.hj);
        final ImageView smileFace = alertLayout.findViewById(R.id.smileface);
        final  ImageView img1 = alertLayout.findViewById(R.id.dot1);
        final  ImageView img2 = alertLayout.findViewById(R.id.dot2);
        final  ImageView img3 = alertLayout.findViewById(R.id.dot3);

        zoomout = AnimationUtils.loadAnimation(getActivity(), R.anim.zoomin);
        smileFace.setAnimation(zoomout);

        final Animation myFadeInAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_anim);
        img1.startAnimation(myFadeInAnimation);
        img2.startAnimation(myFadeInAnimation);
        img3.startAnimation(myFadeInAnimation);

        Typeface type = Typeface.createFromAsset(getActivity().getAssets(), "fonts/DroidSerif-Regular.ttf");
        msg.setTypeface(type);

        msg.setText("Please wait for "+ sender_name+ " to start the game.");

        final android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(getActivity(), R.style.CustomDialogTheme);

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

                        pref = getActivity().getSharedPreferences("sender_info",Context.MODE_PRIVATE);
                        editor1 = pref.edit();
                        editor1.putString("s_id", sender_id);
                        editor1.putString("not_id",notification_id);
                        editor1.putString("sender_name",sender_name);
                        editor1.commit();

                        Intent intent = new Intent(getActivity(), LoaderForReceiverActivity.class);
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


    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        System.gc();
    }
}
