package com.aptitude.education.e2buddy.Question;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aptitude.education.e2buddy.App_Sharing.FAQ_Dialog;
import com.aptitude.education.e2buddy.App_Sharing.ShareAndEarnDialog;
import com.aptitude.education.e2buddy.Intro.CheckInternet;
import com.aptitude.education.e2buddy.R;
import com.bumptech.glide.load.engine.Resource;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import javax.mail.Quota;

import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Matrix on 07-01-2019.
 */

public class MenuSheetDialog extends BottomSheetDialogFragment {


    TextView feedback,rateus,shareandearn,faq;
    TextView username, email,privacy;

    String userid,student_name;
    ImageView userIcon;
    LinearLayout layout;
    Transformation transformation;
    FirebaseAuth auth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bottem_sheet_dialog, container, false);

        feedback = view.findViewById(R.id.feed);
        rateus = view.findViewById(R.id.rate);
        username = view.findViewById(R.id.username);
        email = view.findViewById(R.id.email);
        userIcon = view.findViewById(R.id.userimg);
        privacy = view.findViewById(R.id.privacy);
        layout=view.findViewById(R.id.layout1);
        shareandearn = view.findViewById(R.id.earn);
        faq = view.findViewById(R.id.faq);

        Typeface type = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Regular.ttf");
        feedback.setTypeface(type);
        rateus.setTypeface(type);
        username.setTypeface(type);
        email.setTypeface(type);
        privacy.setTypeface(type);
        faq.setTypeface(type);
        shareandearn.setTypeface(type);

        CheckInternet checkInternet = new CheckInternet(getActivity());
        checkInternet.checkConnection();

        //image

        int height= Resources.getSystem().getDisplayMetrics().heightPixels;
        int width= Resources.getSystem().getDisplayMetrics().widthPixels;
        RelativeLayout.LayoutParams params=(RelativeLayout.LayoutParams)userIcon.getLayoutParams();
        RelativeLayout.LayoutParams params2=(RelativeLayout.LayoutParams)layout.getLayoutParams();
        LinearLayout.LayoutParams params3=(LinearLayout.LayoutParams)username.getLayoutParams();
        params2.topMargin=(width*18)/100;
        params.width=(width*36)/100;
        params.height=(width*36)/100;
        params3.topMargin=(width*20)/100;
        username.setLayoutParams(params3);
        userIcon.setLayoutParams(params);
        layout.setLayoutParams(params2);

        auth = FirebaseAuth.getInstance();
        final FirebaseUser user = auth.getCurrentUser();
        userid = user.getUid();

        transformation = new RoundedTransformationBuilder()
                .borderColor(getResources().getColor(R.color.white))
                .borderWidthDp(0)
                .cornerRadiusDp(50)
                .oval(false)
                .build();

        //getPlayerName();
        getPlayerImage();

        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.gc();
                FragmentManager fm = getActivity().getFragmentManager();
                DialogFragment dialog = FeedbackFullScreenDialog.newInstance();
                dialog.show(fm,"tag");

            }
        });
        rateus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.gc();
                Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("https://play.google.com/store/apps/details?id=com.aptitude.education.e2buddy"));
                startActivity(intent);
            }
        });


        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.gc();
                String url = "http://e2softwares.com/privacy-policy";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        shareandearn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.gc();
                FragmentManager fm = getActivity().getFragmentManager();
                DialogFragment dialog = ShareAndEarnDialog.newInstance();
                dialog.show(fm,"tag");

            }
        });

        faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.gc();
                FragmentManager fm = getActivity().getFragmentManager();
                DialogFragment dialog = FAQ_Dialog.newInstance();
                dialog.show(fm,"tag");
            }
        });

        return view;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {

        View rootView = getActivity().getLayoutInflater().inflate(R.layout.bottem_sheet_dialog,null,false);
        Unbinder unbinder = ButterKnife.bind(this, rootView);
        dialog.setContentView(rootView);
        FrameLayout bottomSheet = (FrameLayout) dialog.getWindow().findViewById(android.support.design.R.id.design_bottom_sheet);
        bottomSheet.setBackgroundResource(R.drawable.bglinear);

        super.setupDialog(dialog, style);
    }

    private void getPlayerImage(){
        DatabaseReference databaseReference;
        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("user_info").child(userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {

                    student_name = dataSnapshot.child("student_name").getValue().toString();
                    String imageUrl = dataSnapshot.child("image_Url").getValue(String.class);
                    String number = dataSnapshot.child("phone_no").getValue(String.class);
                    String emailid = dataSnapshot.child("email").getValue(String.class);


                    Picasso.with(getActivity())
                            .load(imageUrl)
                            .placeholder(R.drawable.userimg)
                            .fit()
                            .transform(transformation)
                            .into(userIcon);

                    username.setText(""+student_name);


                    if (number.equals("0")){
                        email.setText(""+emailid);
                    }
                    else {
                        email.setText(""+number);
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
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        System.gc();
    }
}
