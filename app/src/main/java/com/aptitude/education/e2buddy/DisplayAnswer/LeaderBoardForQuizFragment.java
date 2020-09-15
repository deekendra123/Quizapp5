package com.aptitude.education.e2buddy.DisplayAnswer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;


import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.AppCompatButton;



import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aptitude.education.e2buddy.GoogleAds.BannerAd;
import com.aptitude.education.e2buddy.Question.StartQuizActivity;
import com.aptitude.education.e2buddy.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class LeaderBoardForQuizFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    RecyclerView recyclerView;
    LeaderBoardAdapter leaderBoardAdapter;
    List<LeaderBoardData> list;
    String  userid,quizdate,userids,quizids, username, yourscore ,playername,value;
    int scores;
    TextView score,tvscore,textViewRank,leaderdata,coin, tvrank,tvoverallrank;
    Transformation transformation;
    List<String> stringList;
    ImageView userIcon;
    ProgressDialog progressDialog;
    DatabaseReference databaseReference;
    ValueEventListener valueEventListener;
    FirebaseAuth auth;
    SharedPreferences sharedPreferences;
    AppCompatButton btPlayQuiz;
    LinearLayout linearLayout;

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
        final View view = inflater.inflate(R.layout.fragment_leader_board_for_quiz, container, false);
        recyclerView = view.findViewById(R.id.leaderboard);
        tvscore = view.findViewById(R.id.qscore);
        leaderdata = view.findViewById(R.id.tvdata);
        coin = view.findViewById(R.id.coinsds);
        tvrank = view.findViewById(R.id.r);
        textViewRank = view.findViewById(R.id.img);
        score = view.findViewById(R.id.textView13);
        userIcon = view.findViewById(R.id.e2buddy);
        tvoverallrank = view.findViewById(R.id.tvoverallrank);
        btPlayQuiz = view.findViewById(R.id.btPlayQuiz);
        linearLayout = view.findViewById(R.id.layout);

        tvoverallrank.setText(R.string.globla_rank);

        getActivity().getFragmentManager().popBackStack();

        auth = FirebaseAuth.getInstance();
        final FirebaseUser user = auth.getCurrentUser();
        userid = user.getUid();

        sharedPreferences = getActivity().getSharedPreferences("e2buddy", Context.MODE_PRIVATE);
        value = sharedPreferences.getString("curent_date","");

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        quizdate = sdf.format(new Date());

        BannerAd bannerAd = new BannerAd(getActivity(), view);
        bannerAd.loadFragmentBannerAd();

        databaseReference = FirebaseDatabase.getInstance().getReference();
        progressDialog = new ProgressDialog(getActivity());
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

        getPlayerImage();

        list = new ArrayList<>();
        stringList = new ArrayList<>();
        leaderBoardAdapter = new LeaderBoardAdapter(getActivity(), list, userid,textViewRank);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                databaseReference.child("daily_user_credit").child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(quizdate)){
                            databaseReference.child("daily_user_credit").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                                        userids = data.getKey();
                                        getQuiz(userids);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                        else {

                            progressDialog.dismiss();
                            linearLayout.setVisibility(View.VISIBLE);
                            btPlayQuiz.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getActivity(), StartQuizActivity.class);
                                    intent.putExtra("quiz_date", quizdate);
                                    getActivity().startActivity(intent);
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        };
        new Thread(runnable).start();





        Query lastQuery = databaseReference.child("daily_user_credit").child(userid).child(quizdate).orderByKey().limitToLast(1);

        lastQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                    try {
                        yourscore = dataSnapshot1.child("credit_points").getValue().toString();
                        score.setText(""+yourscore);
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

        tvoverallrank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), OverAllLeaderBoardActivity.class);
                startActivity(intent);
            }
        });



        return view;
    }


    private void getQuiz(final String id){


        Query lastQuery = databaseReference.child("daily_user_credit").child(id).child(quizdate).orderByKey().limitToLast(1);

        lastQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                quizids = dataSnapshot.getKey();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    try {

                        String date = dataSnapshot1.getKey();

                        scores = Integer.parseInt(dataSnapshot1.child("credit_points").getValue().toString());

                        getUserName(id, quizids, scores, date);


                    }catch (Exception e){
                        e.printStackTrace();
                    }


                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void getUserName(final String ids, final String qids , final int scr, final String date){

        databaseReference.child("user_info").child(ids).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {

                    username = dataSnapshot.child("student_name").getValue(String.class);
                    String imageUrl = dataSnapshot.child("image_Url").getValue(String.class);


                    list.add(new LeaderBoardData(ids, username, scr, imageUrl, date,qids));

                    Collections.sort(list, Collections.reverseOrder(new Comparator<LeaderBoardData>() {
                        @Override
                        public int compare(LeaderBoardData leaderBoardData, LeaderBoardData t1) {
                            return leaderBoardData.score - t1.score;
                        }
                    }));


                    recyclerView.setAdapter(leaderBoardAdapter);
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

        valueEventListener = databaseReference.child("user_info").child(userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                    String imageUrl = dataSnapshot.child("image_Url").getValue(String.class);
                    String student_name = dataSnapshot.child("student_name").getValue().toString();
                    leaderdata.setText(""+student_name);

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


    @Override
    public void onPause() {
        super.onPause();
        databaseReference.removeEventListener(valueEventListener);
    }

    @Override
    public void onResume() {
        super.onResume();


    }
}
