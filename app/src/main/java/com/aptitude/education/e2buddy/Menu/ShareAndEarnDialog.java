package com.aptitude.education.e2buddy.Menu;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.aptitude.education.e2buddy.GoogleAds.BannerAd;
import com.aptitude.education.e2buddy.R;
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

public class ShareAndEarnDialog extends DialogFragment {
    private Callback callback;

    TextView tve2buddy,tvUserName,tvReferrals, tvSharecode,tvCode,tvTotalCoinsEarned,tvCoinearned,tvTotalReferrals,tvlevel;
    ImageView userIcon,faq;
    String userid,user_name;
    DatabaseReference databaseReference;
    DatabaseReference reference3;
    int code_counter,counter;
    ProgressDialog progressDialog;
    Transformation transformation;
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

        BannerAd bannerAd = new BannerAd(getActivity(), view);
        bannerAd.loadFragmentBannerAd();


        transformation = new RoundedTransformationBuilder()
                .borderColor(getResources().getColor(R.color.white))
                .borderWidthDp(0)
                .cornerRadiusDp(50)
                .oval(false)
                .build();

        auth = FirebaseAuth.getInstance();
        final FirebaseUser user = auth.getCurrentUser();
        userid = user.getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference();
        reference3 = FirebaseDatabase.getInstance().getReference();


        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Data Loading...");
        progressDialog.show();


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

                try {
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
                }catch (Exception e){
                    e.printStackTrace();
                }

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
                    user_name = student_name;

                    Picasso.get()
                            .load(imageUrl)
                            .placeholder(R.drawable.userimg)
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
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        System.gc();
    }

}
