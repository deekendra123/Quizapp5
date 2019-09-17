package com.aptitude.education.e2buddy.Question;

import android.annotation.SuppressLint;

import android.app.FragmentManager;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aptitude.education.e2buddy.App_Sharing.FAQ_Dialog;
import com.aptitude.education.e2buddy.App_Sharing.Merchandise_Dialog;
import com.aptitude.education.e2buddy.App_Sharing.RewardFragment;
import com.aptitude.education.e2buddy.DisplayAnswer.LeaderBoardForQuizFragment;
import com.aptitude.education.e2buddy.One_on_One_Quiz_Challenge.ChallengeQuizRequestFragment;
import com.aptitude.education.e2buddy.One_on_One_Quiz_Challenge.LoaderForReceiverActivity;
import com.aptitude.education.e2buddy.One_on_One_Quiz_Challenge.SendRequestToOpponentFragment;
import com.aptitude.education.e2buddy.R;
import com.aptitude.education.e2buddy.ViewData.OnlineStatusData;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import android.app.DialogFragment;
public class HomeNevActivity extends AppCompatActivity {

    private final static String TAG_FRAGMENT = "TAG_FRAGMENT";
    DatabaseReference reference4;
    String student_name, eid, imageUrl;
    FirebaseAuth auth;
    long notification_hr,notification_min,notification_sec;
    String sender_name,notification_id,receiver_user_id,sender_id;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    InterstitialAd mInterstitialAd;
    Fragment currentFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_nev);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        BottomNavigationViewHelper.removeShiftMode(navigation);

        navigation.getMenu().getItem(2).setChecked(true);

        navigation.setItemIconTintList(null);

        mInterstitialAd = new InterstitialAd(HomeNevActivity.this);

        mInterstitialAd.setAdUnitId(getString(R.string.Interstitial_ad));

        AdRequest adRequest = new AdRequest.Builder().build();

        mInterstitialAd.loadAd(adRequest);


        auth = FirebaseAuth.getInstance();
        final FirebaseUser user = auth.getCurrentUser();
        eid = user.getUid();

        navigation.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                        currentFragment = getSupportFragmentManager().findFragmentById(R.id.frame_container);


                        switch (item.getItemId()) {

                            case R.id.navigation_home:

                                if (!(currentFragment instanceof HomeFragment)) {
                                    System.gc();
                                    HomeFragment fragment = new HomeFragment();
                                    loadFragment(fragment);
                                }

                                break;
                            case R.id.navigation_dashboard:


                                if (!(currentFragment instanceof PreviousQuizFragment)) {
                                    System.gc();
                                    PreviousQuizFragment previousQuizFragment = new PreviousQuizFragment();
                                    loadFragment(previousQuizFragment);
                                }

                                break;

                            case R.id.navigation_leaderboard:

                                if (!(currentFragment instanceof LeaderBoardForQuizFragment)) {
                                    System.gc();
                                    LeaderBoardForQuizFragment  leaderBoardForQuizFragment = new LeaderBoardForQuizFragment();
                                    loadFragment(leaderBoardForQuizFragment);

                                }
                                break;


                            case R.id.navigation_rewards:

                                if (!(currentFragment instanceof RewardFragment)) {
                                    System.gc();
                                    RewardFragment rewardFragment = new RewardFragment();
                                    loadFragment(rewardFragment);
                                }
                                break;

                           /* case  R.id.navigation_challenge:

                                if (!(currentFragment instanceof SendRequestToOpponentFragment)) {
                                    SendRequestToOpponentFragment sendRequestToOpponentFragment = new SendRequestToOpponentFragment();
                                    loadFragment(sendRequestToOpponentFragment);
                                }
                                break;
*/
                        }
                        return false;
                    }
                });

        getUserName();
        getDynamicLink();

        loadFragment(new HomeFragment());
    }



    private boolean loadFragment(android.support.v4.app.Fragment fragment) {

        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_container, fragment);
        fragmentTransaction.commit();
        return true;

    }


    @Override
    public void onBackPressed() {

        mInterstitialAd.show();

        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
        else {
            new AlertDialog.Builder(HomeNevActivity.this)
                    .setTitle("Really Exit?")
                    .setMessage("Are you sure you want to exit?")
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            HomeNevActivity.super.onBackPressed();
                        }
                    }).create().show();
        }


        mInterstitialAd.setAdListener(new AdListener() {

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                HomeNevActivity.super.onBackPressed();
                // Code to be executed when the interstitial ad is closed.
            }
        });

    }

    public static class BottomNavigationViewHelper {

        @SuppressLint("RestrictedApi")
        public static void removeShiftMode(BottomNavigationView view) {
            //this will remove shift mode for bottom navigation view
            BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
            try {
                Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
                shiftingMode.setAccessible(true);
                shiftingMode.setBoolean(menuView, false);
                shiftingMode.setAccessible(false);
                for (int i = 0; i < menuView.getChildCount(); i++) {
                    BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                    item.setShiftingMode(false);
                    // set once again checked value, so view will be updated
                    item.setChecked(item.getItemData().isChecked());
                }

            } catch (NoSuchFieldException e) {
                Log.e("ERROR NO SUCH FIELD", "Unable to get shift mode field");
            } catch (IllegalAccessException e) {
                Log.e("ERROR ILLEGAL ALG", "Unable to change value of shift mode");
            }
        }
    }

    private void getUserName(){

        reference4 = FirebaseDatabase.getInstance().getReference();

        reference4.child("user_info").child(eid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                    student_name = dataSnapshot.child("student_name").getValue().toString();
                    imageUrl = dataSnapshot.child("image_Url").getValue(String.class);

                    fullConnectionExample(student_name,imageUrl);

                }catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void fullConnectionExample(final String student_name, final String imageUrl) {



        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myConnectionsRef = database.getReference("online_users");

        final DatabaseReference connectedRef = database.getReference(".info/connected");
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();


        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);

                ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                final boolean isconnected = networkInfo!=null && networkInfo.isConnectedOrConnecting();


                if (connected) {

                    myConnectionsRef.child(eid).setValue(new OnlineStatusData(student_name, Boolean.TRUE, imageUrl));

                    reference.child("online_users").child(eid).onDisconnect().removeValue();

                }


            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("deeke", "Listener was cancelled at .info/connected");
            }
        });
    }

    private void getDynamicLink(){

        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("referral_code");
        final DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference reference3 = FirebaseDatabase.getInstance().getReference();

        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {

                            deepLink = pendingDynamicLinkData.getLink();

                            Log.e("deeke"," detect link "+deepLink);

                            String link = deepLink.toString();
                            final String code = link.substring(27);

                            Log.e("deeke"," refer code "+code);


                            reference1.orderByChild("referral_code").equalTo(code).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                                        final String user_id =dataSnapshot1.getKey();

                                        Log.e("deeke"," refer user "+user_id);

                                        reference3.child("referral_code").child(eid).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {

                                                if (dataSnapshot.hasChild("refered_by")){
                                                    //  reference2.child("referral_code").child(eid).child("refered_by").setValue("no");
                                                }
                                                else {
                                                    reference2.child("referral_code").child(eid).child("refered_by").setValue(user_id);
                                                    reference.child("referrals").child(user_id).child(eid).setValue("1");
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });

                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });


                        }
                        else {

                            reference3.child("referral_code").child(eid).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    if (dataSnapshot.hasChild("refered_by")){
                                        //  reference2.child("referral_code").child(eid).child("refered_by").setValue("no");
                                    }
                                    else {
                                        reference2.child("referral_code").child(eid).child("refered_by").setValue("no");
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }

                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("deeke", "getDynamicLink:onFailure", e);
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

        final LayoutInflater inflater = LayoutInflater.from(HomeNevActivity.this);
        final View alertLayout = inflater.inflate(R.layout.layout_custom_dialog_notification, null);
        final TextView msg = alertLayout.findViewById(R.id.hj);
        final TextView tvaccept = alertLayout.findViewById(R.id.tvaccept);
        final TextView tvreject = alertLayout.findViewById(R.id.tvrej);
        final ImageView smileFace = alertLayout.findViewById(R.id.smileface);

        zoomout = AnimationUtils.loadAnimation(HomeNevActivity.this, R.anim.zoomin);
        smileFace.setAnimation(zoomout);

        Typeface type = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/DroidSerif-Regular.ttf");
        msg.setTypeface(type);

        msg.setText(sender_name+" has challenged you.");

        final android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(HomeNevActivity.this, R.style.CustomDialogTheme);

        alert.setView(alertLayout);
        alert.setCancelable(false);

        final android.app.AlertDialog dialog = alert.create();

        tvaccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

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
        final LayoutInflater inflater = LayoutInflater.from(HomeNevActivity.this);
        final View alertLayout = inflater.inflate(R.layout.layout_custom_dialor_start_quiz, null);
        final TextView msg = alertLayout.findViewById(R.id.hj);
        final ImageView smileFace = alertLayout.findViewById(R.id.smileface);
        final  ImageView img1 = alertLayout.findViewById(R.id.dot1);
        final  ImageView img2 = alertLayout.findViewById(R.id.dot2);
        final  ImageView img3 = alertLayout.findViewById(R.id.dot3);

        zoomout = AnimationUtils.loadAnimation(HomeNevActivity.this, R.anim.zoomin);
        smileFace.setAnimation(zoomout);

        final Animation myFadeInAnimation = AnimationUtils.loadAnimation(HomeNevActivity.this, R.anim.fade_anim);
        img1.startAnimation(myFadeInAnimation);
        img2.startAnimation(myFadeInAnimation);
        img3.startAnimation(myFadeInAnimation);

        Typeface type = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/DroidSerif-Regular.ttf");
        msg.setTypeface(type);

        msg.setText("Please wait for "+ sender_name+ " to start the game.");

        final android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(HomeNevActivity.this, R.style.CustomDialogTheme);

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
                        editor = pref.edit();
                        editor.putString("s_id", sender_id);
                        editor.putString("not_id",notification_id);
                        editor.putString("sender_name",sender_name);
                        editor.commit();

                        Intent intent = new Intent(HomeNevActivity.this, LoaderForReceiverActivity.class);
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
