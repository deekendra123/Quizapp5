package com.aptitude.education.e2buddy.Question;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aptitude.education.e2buddy.Intro.CheckInternet;
import com.aptitude.education.e2buddy.R;
import com.aptitude.education.e2buddy.ViewData.PreQuizIdData;
import com.aptitude.education.e2buddy.ViewData.PreviousQuizView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class PreviousQuizFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    ProgressDialog progressDialog;

    TextView textView;
    RecyclerView recyclerView ;
    DatabaseReference databaseReference;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String quizid, quizname, userid,quizdate;

    DatabaseReference reference;
    AdView mAdView;
    String uname;

    List<PreviousQuizView> previousQuizViewList;
    List<PreQuizIdData> list;
    PreviousQuizAdapter previousQuizAdapter;
    String  qdate;
    ImageView userIcon;

    String  user, student_name;
    DatabaseReference reference4;
    Transformation transformation;
    FirebaseAuth auth;

    String p1,p2,p3,p4,p5,p6,p7,p8,p9,p10,p11, systemdate;

    Date pd1,pd2,pd3,pd4,pd5,pd6,pd7,pd8,pd9,pd10,pd11;
    TextView e2;
    ImageView img_info;


    public PreviousQuizFragment() {
        // Required empty public constructor
    }

 public static PreviousQuizFragment newInstance(String param1, String param2) {
        PreviousQuizFragment fragment = new PreviousQuizFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_previous_quiz, container, false);

        recyclerView = view.findViewById(R.id.prerecycler);
        userIcon = view.findViewById(R.id.userimage);
        img_info = view.findViewById(R.id.img_info);

        mAdView = (AdView) view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        getActivity().getFragmentManager().popBackStack();


        e2 = view.findViewById(R.id.e2);

        CheckInternet checkInternet = new CheckInternet(getActivity());
        checkInternet.checkConnection();


        auth = FirebaseAuth.getInstance();
        final FirebaseUser user = auth.getCurrentUser();
        userid = user.getUid();

        transformation = new RoundedTransformationBuilder()
                .borderColor(getResources().getColor(R.color.white))
                .borderWidthDp(0)
                .cornerRadiusDp(50)
                .oval(false)
                .build();


        //e2.setText(""+uname);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Data Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);
        progressDialog.setMax(100);
        progressDialog.show();

        Typeface type = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Regular.ttf");
        e2.setTypeface(type);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);


        reference = FirebaseDatabase.getInstance().getReference();
        reference4 = FirebaseDatabase.getInstance().getReference();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM");
        systemdate = sdf.format(new Date());

        getPlayerName();
        getPlayerImage();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM");
        Date myDate = null;
        try {
            myDate = dateFormat.parse(systemdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar c1 = Calendar.getInstance();
        c1.setTime(myDate);
        c1.add(Calendar.DATE, -1);

        Calendar c2 = Calendar.getInstance();
        c2.setTime(myDate);
        c2.add(Calendar.DATE, -2);

        Calendar c3 = Calendar.getInstance();
        c3.setTime(myDate);
        c3.add(Calendar.DATE, -3);

        Calendar c4 = Calendar.getInstance();
        c4.setTime(myDate);
        c4.add(Calendar.DATE, -4);

        Calendar c5 = Calendar.getInstance();
        c5.setTime(myDate);
        c5.add(Calendar.DATE, -5);

        Calendar c6 = Calendar.getInstance();
        c6.setTime(myDate);
        c6.add(Calendar.DATE, -6);
        Calendar c7 = Calendar.getInstance();
        c7.setTime(myDate);
        c7.add(Calendar.DATE, -7);
        Calendar c8 = Calendar.getInstance();
        c8.setTime(myDate);
        c8.add(Calendar.DATE, -8);
        Calendar c9 = Calendar.getInstance();
        c9.setTime(myDate);
        c9.add(Calendar.DATE, -9);
        Calendar c10 = Calendar.getInstance();
        c10.setTime(myDate);
        c10.add(Calendar.DATE, -10);


        pd1 = c1.getTime();
        p1 = dateFormat.format(pd1);
        pd2 = c2.getTime();
        p2 = dateFormat.format(pd2);

        pd3 = c3.getTime();
        p3 = dateFormat.format(pd3);

        pd4 = c4.getTime();
        p4 = dateFormat.format(pd4);

        pd5 = c5.getTime();
        p5 = dateFormat.format(pd5);

        pd6 = c6.getTime();
        p6 = dateFormat.format(pd6);

        pd7 = c7.getTime();
        p7 = dateFormat.format(pd7);

        pd8 = c8.getTime();
        p8 = dateFormat.format(pd8);

        pd9 = c9.getTime();
        p9 = dateFormat.format(pd9);

        pd10 = c10.getTime();
        p10 = dateFormat.format(pd10);

        sharedPreferences = getActivity().getSharedPreferences("quizpref", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        databaseReference = FirebaseDatabase.getInstance().getReference();

        previousQuizViewList = new ArrayList<>();

        previousQuizAdapter = new PreviousQuizAdapter(getActivity(), previousQuizViewList);

        databaseReference.child("daily_Question").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot data : dataSnapshot.getChildren()){
                    qdate = data.getKey();

                    if (p1.equals(qdate)
                            || p2.equals(qdate)
                            || p3.equals(qdate)
                            ||p4.equals(qdate)
                            ||p5.equals(qdate)
                            ||p6.equals(qdate)
                            ||p7.equals(qdate)
                            ||p8.equals(qdate)
                            ||p9.equals(qdate)
                            ||p10.equals(qdate)) {



                            previousQuizViewList.add(new PreviousQuizView(qdate));


                    }
                }

                recyclerView.setAdapter(previousQuizAdapter);
                progressDialog.dismiss();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        img_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MenuSheetDialog menuSheetDialog = new MenuSheetDialog();
                menuSheetDialog.setStyle(BottomSheetDialogFragment.STYLE_NORMAL,R.style.CustomBottomSheetDialogTheme);
                menuSheetDialog.show(getActivity().getSupportFragmentManager(), "quizapp2");

            }
        });

        return view;
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    private void getPlayerName(){

        reference4.child("user_info").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                    student_name = dataSnapshot.child(userid).child("student_name").getValue().toString();

                    e2.setText(""+student_name);


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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    private void getPlayerImage(){
        DatabaseReference databaseReference;
        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("user_info").child(userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                    String imageUrl = dataSnapshot.child("image_Url").getValue(String.class);
                    String student_name = dataSnapshot.child("student_name").getValue(String.class);

                    Picasso.with(getActivity())
                            .load(imageUrl)
                            .placeholder(R.mipmap.ic_launcher)
                            .fit()
                            .transform(transformation)
                            .into(userIcon);

                    e2.setText(""+student_name);
                }catch (Exception e){
                    e.printStackTrace();
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



}
