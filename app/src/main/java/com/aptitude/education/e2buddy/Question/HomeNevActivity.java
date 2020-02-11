package com.aptitude.education.e2buddy.Question;

import android.annotation.SuppressLint;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.aptitude.education.e2buddy.Internship.InternshipFragment;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.aptitude.education.e2buddy.Menu.MenuSheetDialog;
import com.aptitude.education.e2buddy.Menu.RewardFragment;
import com.aptitude.education.e2buddy.DisplayAnswer.LeaderBoardForQuizFragment;
import com.aptitude.education.e2buddy.Intro.IntroActivity;
import com.aptitude.education.e2buddy.Placement_Papers.PlacementQuizFragment;
import com.aptitude.education.e2buddy.R;
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

public class HomeNevActivity extends AppCompatActivity {

    String eid;
    FirebaseAuth auth;
    Fragment currentFragment = null;
    DatabaseReference reference;

    private InterstitialAd interstitialAd;
    private boolean doubleBackToExitPressedOnce = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_nev);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        BottomNavigationViewHelper.removeShiftMode(navigation);

       navigation.getMenu().getItem(2).setChecked(true);

        navigation.setItemIconTintList(null);

        reference = FirebaseDatabase.getInstance().getReference();


        interstitialAd= new InterstitialAd(this);
        interstitialAd.setAdUnitId(getString(R.string.Interstitial_ad));
        AdRequest adRequest = new AdRequest.Builder().build();
        interstitialAd.loadAd(adRequest);


        auth = FirebaseAuth.getInstance();
        final FirebaseUser user = auth.getCurrentUser();

        if(user != null){
            eid = user.getUid();
        }
        else {
            Intent intent = new Intent(HomeNevActivity.this, IntroActivity.class);
            startActivity(intent);
        }

        navigation.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                        currentFragment = getSupportFragmentManager().findFragmentById(R.id.frame_container);


                        switch (item.getItemId()) {


                            case R.id.navigation_home:

                                if (!(currentFragment instanceof QuizHomeFragment)) {
                                    QuizHomeFragment fragment = new QuizHomeFragment();
                                    loadFragment(fragment);
                                }

                                break;


                            case R.id.navigation_rewards:

                                if (!(currentFragment instanceof RewardFragment)) {
                                    RewardFragment rewardFragment = new RewardFragment();
                                    loadFragment(rewardFragment);
                                }
                                break;

                            case R.id.navigation_leaderboard:

                                if (!(currentFragment instanceof LeaderBoardForQuizFragment)) {
                                    LeaderBoardForQuizFragment leaderBoardForQuizFragment = new LeaderBoardForQuizFragment();
                                    loadFragment(leaderBoardForQuizFragment);

                                }
                                break;


                            case R.id.navigation_placement_paper:

                                if (!(currentFragment instanceof PlacementQuizFragment)) {
                                    PlacementQuizFragment leaderBoardForQuizFragment = new PlacementQuizFragment();
                                    loadFragment(leaderBoardForQuizFragment);

                                }
                                break;

                         /*   case R.id.internships:

                                if (!(currentFragment instanceof PlacementQuizFragment)) {
                                    InternshipFragment internshipFragment = new InternshipFragment();
                                    loadFragment(internshipFragment);

                                }
                                break;*/



                            case R.id.navigation_menu:

                                    MenuSheetDialog menuSheetDialog = new MenuSheetDialog();
                                    menuSheetDialog.setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
                                    menuSheetDialog.show(getSupportFragmentManager(), "quizapp2");

                                break;


                        }
                        return false;
                    }
                });

        //    getUserName();
        getDynamicLink();

        loadFragment(new QuizHomeFragment());
    }


    private boolean loadFragment(Fragment fragment) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_container, fragment);
        fragmentTransaction.commit();
        return true;

    }

    @Override
    public void onBackPressed() {

       this.doubleBackToExitPressedOnce = true;

        if (doubleBackToExitPressedOnce) {

            if (interstitialAd.isLoaded()) {
                interstitialAd.show();
                interstitialAd.setAdListener(new AdListener() {
                    @Override
                    public void onAdClosed() {
                        super.onAdClosed();
                        finish();
                    }
                });
            }else{

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

            return;
        }






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
                    item.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
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


    private void getDynamicLink() {

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

                            Log.e("deeke", " detect link " + deepLink);

                            String link = deepLink.toString();
                            final String code = link.substring(27);

                            Log.e("deeke", " refer code " + code);


                            reference.orderByChild("referral_code").equalTo(code).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                                        final String user_id = dataSnapshot1.getKey();

                                        Log.e("deeke", " refer user " + user_id);

                                        reference.child("referral_code").child(eid).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {

                                                if (dataSnapshot.hasChild("refered_by")) {
                                                    //  reference2.child("referral_code").child(eid).child("refered_by").setValue("no");
                                                } else {
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


                        } else {

                            reference.child("referral_code").child(eid).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    if (dataSnapshot.hasChild("refered_by")) {

                                    } else {
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
