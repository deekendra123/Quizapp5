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
import com.aptitude.education.e2buddy.Intro.Quizapp;
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

    String  eid;
    FirebaseAuth auth;

    InterstitialAd mInterstitialAd;
    Fragment currentFragment = null;
    DatabaseReference reference;
    ValueEventListener valueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_nev);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        BottomNavigationViewHelper.removeShiftMode(navigation);


        navigation.getMenu().getItem(2).setChecked(true);

        navigation.setItemIconTintList(null);

         reference = FirebaseDatabase.getInstance().getReference();


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
                                    HomeFragment fragment = new HomeFragment();
                                    loadFragment(fragment);
                                }

                                break;
                            case R.id.navigation_dashboard:


                                if (!(currentFragment instanceof PreviousQuizFragment)) {

                                    PreviousQuizFragment previousQuizFragment = new PreviousQuizFragment();
                                    loadFragment(previousQuizFragment);
                                }

                                break;

                            case R.id.navigation_leaderboard:

                                if (!(currentFragment instanceof LeaderBoardForQuizFragment)) {
                                    LeaderBoardForQuizFragment  leaderBoardForQuizFragment = new LeaderBoardForQuizFragment();
                                    loadFragment(leaderBoardForQuizFragment);

                                }
                                break;


                            case R.id.navigation_rewards:

                                if (!(currentFragment instanceof RewardFragment)) {
                                    RewardFragment rewardFragment = new RewardFragment();
                                    loadFragment(rewardFragment);
                                }
                                break;


                        }
                        return false;
                    }
                });

    //    getUserName();
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


    @Override
    protected void onPause() {
        super.onPause();
        reference.removeEventListener(valueEventListener);

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



    private void getDynamicLink(){

         reference = FirebaseDatabase.getInstance().getReference("referral_code");

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


                          valueEventListener =  reference.orderByChild("referral_code").equalTo(code).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                                        final String user_id =dataSnapshot1.getKey();

                                        Log.e("deeke"," refer user "+user_id);

                                        reference.child("referral_code").child(eid).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {

                                                if (dataSnapshot.hasChild("refered_by")){
                                                    //  reference2.child("referral_code").child(eid).child("refered_by").setValue("no");
                                                }
                                                else {
                                                    reference.child("referral_code").child(eid).child("refered_by").setValue(user_id);
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

                            reference.child("referral_code").child(eid).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    if (dataSnapshot.hasChild("refered_by")){
                                        //  reference2.child("referral_code").child(eid).child("refered_by").setValue("no");
                                    }
                                    else {
                                        reference.child("referral_code").child(eid).child("refered_by").setValue("no");
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


}
