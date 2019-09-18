package com.aptitude.education.e2buddy.DisplayAnswer;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.aptitude.education.e2buddy.AdMob.Banner_Ad;
import com.aptitude.education.e2buddy.Intro.CheckInternet;
import com.aptitude.education.e2buddy.Intro.Quizapp;
import com.aptitude.education.e2buddy.Question.MenuSheetDialog;
import com.aptitude.education.e2buddy.R;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LeaderBoardForQuizFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    RecyclerView recyclerView;
    LeaderAdapter leaderBoardAdapter;
    List<LeaderBoardData> list;
    String userid, username, yourscore,userids,totalscore,student_name;
    int userscore;
    TextView tvscore, leaderdata,coin, score,tvrank,imageView;
    ProgressDialog progressDialog;
    Transformation transformation;
    ArrayList<String> uids;
    long totalquiz;
    ImageView userIcon,img_info;
    List<String> rank;
    FirebaseAuth auth;
    DatabaseReference databaseReference;
    ValueEventListener valueEventListener;

    public LeaderBoardForQuizFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static LeaderBoardForQuizFragment newInstance(String param1, String param2) {
        LeaderBoardForQuizFragment fragment = new LeaderBoardForQuizFragment();
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
        View view = inflater.inflate(R.layout.fragment_leader_board_for_quiz, container, false);

        recyclerView = view.findViewById(R.id.leaderboard);
        tvscore = view.findViewById(R.id.qscore);
        leaderdata = view.findViewById(R.id.tvdata);
        coin = view.findViewById(R.id.coinsds);
        tvrank = view.findViewById(R.id.r);
        imageView = view.findViewById(R.id.img);
        score = view.findViewById(R.id.textView13);
        img_info = view.findViewById(R.id.img_info);
        userIcon = view.findViewById(R.id.e2buddy);

        Quizapp.getRefWatcher(getActivity()).watch(this);

        auth = FirebaseAuth.getInstance();
        final FirebaseUser user = auth.getCurrentUser();
        userid = user.getUid();

        getActivity().getFragmentManager().popBackStack();


        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Data Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);
        progressDialog.setMax(100);
        progressDialog.show();

        transformation = new RoundedTransformationBuilder()
                .borderColor(getResources().getColor(R.color.white))
                .borderWidthDp(0)
                .cornerRadiusDp(50)
                .oval(false)
                .build();


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        databaseReference = FirebaseDatabase.getInstance().getReference();

        getPlayerName();
        getPlayerImage();
        list = new ArrayList<>();
        rank = new ArrayList<>();
        uids = new ArrayList<>();
        leaderBoardAdapter = new LeaderAdapter(getActivity(), list,userid, imageView);

        databaseReference.child("daily_user_total_score").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                //     list.clear();

                for (DataSnapshot data : dataSnapshot.getChildren()){

                    try {
                        userids = data.getKey();
                        userscore = Integer.parseInt(data.child("total_score").getValue().toString());
                        // Toast.makeText(getActivity(), ""+ userscore,Toast.LENGTH_SHORT).show();
                        getUserName(userids,userscore);

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        valueEventListener = databaseReference.child("daily_user_total_score").child(userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                    yourscore = dataSnapshot.child("total_score").getValue().toString();

                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        leaderBoardAdapter.setOnItemClickListener(new LeaderBoardAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                LeaderBoardData leaderBoardData = list.get(position);
                String user = leaderBoardData.getUserid();
                final int scoresss = list.get(position).getScore();
                showAlertDialog(itemView, user, position);

            }
        });


        showAlertDialogForUser();

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
    private void getUserName(final String id, final int score){

        databaseReference.child("user_info").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                username = dataSnapshot.child("student_name").getValue(String.class);
                String imageUrl = dataSnapshot.child("image_Url").getValue(String.class);


                list.add(new LeaderBoardData(id, username, score,imageUrl));


                Collections.sort(list, Collections.reverseOrder(new Comparator<LeaderBoardData>() {
                    @Override
                    public int compare(LeaderBoardData leaderBoardData, LeaderBoardData t1) {

                        return leaderBoardData.score - t1.score;
                    }
                }));



                recyclerView.setAdapter(leaderBoardAdapter);
                progressDialog.dismiss();

                //getRank(score);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }





    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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

    public void showAlertDialog(View view, String uid, int position) {

        final ProgressDialog progressDialog;
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.layout_custom_dialog, null);
        final TextView tvtotalquiz = alertLayout.findViewById(R.id.totalquiz);
        final TextView score = alertLayout.findViewById(R.id.scores);
        final TextView userrank = alertLayout.findViewById(R.id.userrank);
        final TextView goodjob = alertLayout.findViewById(R.id.goodjob);
        final TextView textView = alertLayout.findViewById(R.id.hj);

        Typeface type = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Regular.ttf");
        tvtotalquiz.setTypeface(type);
        score.setTypeface(type);
        userrank.setTypeface(type);
        goodjob.setTypeface(type);
        textView.setTypeface(type);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Data Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);
        progressDialog.setMax(100);
        progressDialog.show();

        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity(), R.style.CustomDialogTheme);

        alert.setView(alertLayout);

        valueEventListener = databaseReference.child("daily_user_total_score").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                totalscore = dataSnapshot.child("total_score").getValue().toString();

                score.setText("Total Score : " + totalscore);

                progressDialog.dismiss();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

       valueEventListener = databaseReference.child("daily_user_credit").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                totalquiz = dataSnapshot.getChildrenCount();

                tvtotalquiz.setText("Total Quiz : "+ totalquiz);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        if (position==0){

            userrank.setText("1");
            goodjob.setText("Excellent Job!!");

        }
        if (position==1){

            userrank.setText("2");
            goodjob.setText("Excellent Job!!");

        } if (position==2){

            userrank.setText("3");
            goodjob.setText("Excellent Job!!");

        } if (position==3){

            userrank.setText("4");
            goodjob.setText("Good Job!!");


        } if (position==4){

            userrank.setText("5");
            goodjob.setText("Good Job!!");

        } if (position==5){

            userrank.setText("6");
            goodjob.setText("Good Job!!");

        } if (position==6){

            userrank.setText("7");
            goodjob.setText("Good Job!!");

        } if (position==7){

            userrank.setText("8");
            goodjob.setText("Good Job!!");

        } if (position ==8){
            userrank.setText("9");
            goodjob.setText("Good Job!!");

        } if (position ==9){

            userrank.setText("10");
            goodjob.setText("Good Job!!");
        }

        alert.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog dialog = alert.create();
        dialog.show();
    }


    public void showAlertDialogForUser() {

        databaseReference.child("daily_user_total_score").child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {

                    String score1 = dataSnapshot.child("total_score").getValue().toString();
                    score.setText("" + score1);

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

    private void getPlayerName(){
       valueEventListener = databaseReference.child("user_info").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                    student_name = dataSnapshot.child(userid).child("student_name").getValue().toString();


                    leaderdata.setText(""+student_name);


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


    private void getPlayerImage(){

       valueEventListener = databaseReference.child("user_info").child(userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String imageUrl = dataSnapshot.child("image_Url").getValue(String.class);
                Picasso.with(getActivity())
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

    @Override
    public void onPause() {
        super.onPause();
        databaseReference.removeEventListener(valueEventListener);
    }
}
