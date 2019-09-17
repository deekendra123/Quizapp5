package com.aptitude.education.e2buddy.App_Sharing;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.aptitude.education.e2buddy.One_on_One_Quiz_Challenge.LoaderForReceiverActivity;
import com.aptitude.education.e2buddy.Question.QuizQuestionTimeBasedActivity;
import com.aptitude.education.e2buddy.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.MODE_PRIVATE;

public class ShareAndEarnDialog extends DialogFragment {
    private Callback callback;

    TextView tve2buddy,tvUserName,tvReferrals, tvSharecode,tvCode,tvTotalCoinsEarned,tvCoinearned,tvTotalReferrals,tvlevel;
    ImageView userIcon,faq;
    Transformation transformation;
    SharedPreferences sharedPreferences;
    String userid,user_name;
    DatabaseReference databaseReference;
    DatabaseReference reference3;
    int code_counter,counter;

    long notification_hr,notification_min,notification_sec;
    String sender_name,notification_id,receiver_user_id,sender_id;
    SharedPreferences pref;
    SharedPreferences.Editor editor1;
    ProgressDialog progressDialog;
    AdView mAdView;
    FirebaseAuth auth;

    public static ShareAndEarnDialog newInstance() {
        return new ShareAndEarnDialog();
    }

    public void setCallback(ShareAndEarnDialog.Callback callback) {
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
        View view = inflater.inflate(R.layout.fullscreen_dialog_for_share, container, false);
        ImageButton closeDialog = view.findViewById(R.id.fullscreen_dialog_close);
        tve2buddy = view.findViewById(R.id.tve2buddy);
        userIcon = view.findViewById(R.id.userimg);
        tvUserName = view.findViewById(R.id.tvUsername);
        tvReferrals = view.findViewById(R.id.tvreferrals);
        tvSharecode = view.findViewById(R.id.sharecode);
        tvCode = view.findViewById(R.id.code);
        tvTotalCoinsEarned = view.findViewById(R.id.tvtotalcoinsearned);
        tvCoinearned = view.findViewById(R.id.tvcoinearned);
        tvTotalReferrals =view.findViewById(R.id.tvtotalreferrals);
        tvlevel = view.findViewById(R.id.tvlevel);
        final Button tvShare = view.findViewById(R.id.tvShare);
        faq = view.findViewById(R.id.img_info);
        mAdView = (AdView) view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        Typeface type = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Regular.ttf");
        tve2buddy.setTypeface(type);
        tvUserName.setTypeface(type);
        tvReferrals.setTypeface(type);
        tvCode.setTypeface(type);
        tvShare.setTypeface(type);
        tvTotalCoinsEarned.setTypeface(type);
        tvCoinearned.setTypeface(type);
        tvTotalReferrals.setTypeface(type);
        tvSharecode.setTypeface(type);
        tvlevel.setTypeface(type);

        auth = FirebaseAuth.getInstance();
        final FirebaseUser user = auth.getCurrentUser();
        userid = user.getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference();
        reference3 = FirebaseDatabase.getInstance().getReference();


        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Data Loading...");
        progressDialog.show();

        transformation = new RoundedTransformationBuilder()
                .borderColor(getResources().getColor(R.color.white))
                .borderWidthDp(0)
                .cornerRadiusDp(50)
                .oval(false)
                .build();

        getPlayerImage();

        closeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();

            }
        });

        tvShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getDynamicReferralLink();
            }
        });


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        boolean isFirstRun = preferences.getBoolean("FIRSTRUN", true);
        if (isFirstRun)
        {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("FIRSTRUN", false);

            reference3.child("referral_code").child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.hasChild("referral_code")){

                    }
                    else {
                        insertReferralCode();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


            editor.commit();

        }

        getReferralCode();
        getTotalEarnedCoinsAndAllTimeReferrals();

        faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getFragmentManager();
                DialogFragment dialog = FAQ_Dialog.newInstance();
                dialog.show(fm,"tag");
            }
        });
        return view;
    }


    private void getDynamicReferralLink() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        reference.child("referral_code").child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                final String code = dataSnapshot.child("referral_code").getValue().toString();

                Log.e("deeke","create link");

                DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                        .setLink(Uri.parse("http://www.e2softwares.com/"+code))
                        .setDynamicLinkDomain("e2buddy.page.link")
                        .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                        .buildDynamicLink();

                Log.e("deeke", " Long refer "+ dynamicLink.getUri());

                Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                        .setLongLink(dynamicLink.getUri())
                        //.setLongLink(Uri.parse(sharelinktext))
                        .buildShortDynamicLink()
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<ShortDynamicLink>() {
                            @Override
                            public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                                if (task.isSuccessful()) {


                                    Uri shortLink = task.getResult().getShortLink();
                                    Uri flowchartLink = task.getResult().getPreviewLink();

                                    Log.e("deeke"," short link "+ shortLink);

                                    Intent intent = new Intent();

                                    intent.setAction(Intent.ACTION_SEND);
                                    intent.putExtra(Intent.EXTRA_TEXT,shortLink.toString());
                                    intent.setType("text/plain");
                                    intent.putExtra("code", code);
                                    startActivity(intent);


                                } else {

                                    Log.e("deeke", " error "+task.getException());

                                }
                            }
                        });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }



    public interface Callback {

        void onActionClick(String name);

    }

    private void insertReferralCode(){

        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("referral_code_counter").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                code_counter = Integer.parseInt(dataSnapshot.child("code_counter").getValue().toString());
                counter = code_counter+1;

                String u_name = user_name.substring(0, 3);
                String referral_code = u_name+code_counter;

                reference.child("referral_code_counter").child("code_counter").setValue(counter);
                reference1.child("referral_code").child(userid).child("referral_code").setValue(referral_code);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void getReferralCode(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("referral_code").child(userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                    String referral_code = dataSnapshot.child("referral_code").getValue().toString();
                    tvCode.setText(""+referral_code);

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
    private void getPlayerImage(){
        DatabaseReference databaseReference;
        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("user_info").child(userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                    String student_name = dataSnapshot.child("student_name").getValue().toString();
                    String imageUrl = dataSnapshot.child("image_Url").getValue(String.class);

                    tvUserName.setText(""+student_name);
                    Picasso.with(getActivity())
                            .load(imageUrl)
                            .placeholder(R.mipmap.ic_launcher)
                            .fit()
                            .transform(transformation)
                            .into(userIcon);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getTotalEarnedCoinsAndAllTimeReferrals(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("referrals").child(userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                    long count = dataSnapshot.getChildrenCount();

                    tvCoinearned.setText(""+count*5);

                    tvReferrals.setText(""+count);

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

                        pref = getActivity().getSharedPreferences("sender_info",MODE_PRIVATE);
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
