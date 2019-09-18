package com.aptitude.education.e2buddy.Question;

import android.app.ActionBar;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aptitude.education.e2buddy.App_Sharing.FAQ_Dialog;
import com.aptitude.education.e2buddy.DisplayAnswer.LeaderAdapter;
import com.aptitude.education.e2buddy.DisplayAnswer.LeaderBoardAdapter;
import com.aptitude.education.e2buddy.DisplayAnswer.LeaderBoardData;
import com.aptitude.education.e2buddy.DisplayAnswer.ResultActivity;
import com.aptitude.education.e2buddy.DisplayAnswer.ViewAnswerActivity;
import com.aptitude.education.e2buddy.Intro.CheckInternet;
import com.aptitude.education.e2buddy.Intro.IntroActivity;
import com.aptitude.education.e2buddy.Intro.Quizapp;
import com.aptitude.education.e2buddy.Intro.UpdateUserImageActivity;
import com.aptitude.education.e2buddy.Pushdatatofirebase.CopyDbActivity;
import com.aptitude.education.e2buddy.R;
import com.aptitude.education.e2buddy.ShowCaseView.MaterialShowcaseSequence;
import com.aptitude.education.e2buddy.ShowCaseView.MaterialShowcaseView;
import com.aptitude.education.e2buddy.ShowCaseView.ShowcaseConfig;
import com.aptitude.education.e2buddy.ViewData.HistoryView;
import com.aptitude.education.e2buddy.ViewData.OnlineStatusData;
import com.firebase.client.Firebase;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment implements View.OnClickListener {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    RecyclerView recyclerView;
    FirebaseAuth auth;
    FirebaseAuth.AuthStateListener listener;
    ProgressDialog dialog;
    Firebase firebase;
    List<HistoryView> historyViewList;
    List<LeaderBoardData> list;
    String quizid,student_name,userids1,username1,eid,date;
    int userscore;
    Transformation transformation;
    DatabaseReference databaseReference;
    UserQuizHistoryAdapter userQuizHistoryAdapter;
    LeaderAdapter leaderAdapter;
    List<LeaderBoardData> leaderBoardDataList;
    TextView totalquiz,tvscore,tvtodayquiz,e2buddy,history,tvrank,tvquiz, tvsc, tvran,username, todayque, tvhistory, tvwin, tvscr, tvcoin;
    private OnFragmentInteractionListener mListener;
    private static final String SHOWCASE_ID = "sequence example";
    LinearLayout layout1, layout2, layout3;
    ImageView userIcon,userIcon1, quizimage, img_faq;
    int quiz_count =0;
    StorageReference storageReference;
    AdView mAdView;

    ValueEventListener valueEventListener;
    String inputPattern = "yyyy-MM-dd";
    String outputPattern = "dd-MM-yyyy";
    SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
    SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
    Date date1 = null;
    String str = null;

    public HomeFragment() {
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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

        View view = inflater.inflate(R.layout.fragment_home1, container, false);
        recyclerView = view.findViewById(R.id.idRecyclerViewHorizontalList);
        username = view.findViewById(R.id.user);
        tvscore = view.findViewById(R.id.score);
        totalquiz = view.findViewById(R.id.quiznum);
        todayque = view.findViewById(R.id.todayque);
        tvtodayquiz = view.findViewById(R.id.todayquea);
        tvsc = view.findViewById(R.id.s);
        tvquiz = view.findViewById(R.id.q);
        tvran = view.findViewById(R.id.r);
        layout1 = view.findViewById(R.id.layout1);
        layout2 = view.findViewById(R.id.layout2);
        layout3 = view.findViewById(R.id.layout3);
        tvrank = view.findViewById(R.id.tvrank);
        history = view.findViewById(R.id.history);
        userIcon = view.findViewById(R.id.e2buddy);
        userIcon1 = view.findViewById(R.id.userimg);
        tvhistory= view.findViewById(R.id.tvhist);
        tvwin = view.findViewById(R.id.textview);
        tvcoin = view.findViewById(R.id.textView1);
        tvscr = view.findViewById(R.id.todayscr);
        quizimage = view.findViewById(R.id.image);
        img_faq = view.findViewById(R.id.img_info);
        mAdView = (AdView) view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        databaseReference = FirebaseDatabase.getInstance().getReference();

        Animation myFadeInAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.play_anim);
        todayque.startAnimation(myFadeInAnimation);

        dialog = new ProgressDialog(getActivity(),R.style.full_screen_dialog){
            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.full_screen_progress_dialog);
                getWindow().setLayout(WindowManager.LayoutParams.FILL_PARENT,
                        ActionBar.LayoutParams.FILL_PARENT);
            }
        };

        dialog.setCancelable(false);
        dialog.show();

        getActivity().getFragmentManager().popBackStack();

        CheckInternet checkInternet = new CheckInternet(getActivity());
        checkInternet.checkConnection();

        transformation = new RoundedTransformationBuilder()
                .borderColor(getResources().getColor(R.color.white))
                .borderWidthDp(0)
                .cornerRadiusDp(50)
                .oval(false)
                .build();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        list = new ArrayList<>();
        leaderBoardDataList = new ArrayList<>();

        Firebase.setAndroidContext(getActivity());


        auth = FirebaseAuth.getInstance();
        final FirebaseUser user = auth.getCurrentUser();
        eid = user.getUid();

        leaderAdapter = new LeaderAdapter(getActivity(),leaderBoardDataList,eid,tvrank);


        storageReference = FirebaseStorage.getInstance().getReference("profile_images/users/"+eid);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM");
        date = sdf.format(new Date());

        presentShowcaseSequence();
        getPlayerImage();
        setTextFont();

        getTotalQuizAndScore();
        getQuizHistory();
        getTotalQuizCount();

        getAllTimeLeaderBoard();

      valueEventListener = databaseReference.child("user_info").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                try {
                    student_name = dataSnapshot.child(eid).child("student_name").getValue().toString();
                    username.setText(student_name);
                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        todayque.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

           databaseReference.child("daily_user_credit").child(eid).child(date).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {
                            long count = dataSnapshot.getChildrenCount();
                            //  Toast.makeText(getApplicationContext(), ""+ count,Toast.LENGTH_SHORT).show();
                            if (count<3){
                                System.gc();
                                historyViewList.clear();
                                todayque.setEnabled(false);
                                Intent intent = new Intent(getActivity(), StartQuizActivity.class);
                                intent.putExtra("quiz_date", date);
                                startActivity(intent);

                            }else {
                                Toast.makeText(getActivity(), "You have already attemped 3 times", Toast.LENGTH_LONG).show();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });




            }
        });

               listener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {

                    Intent intent = new Intent(getActivity(), IntroActivity.class);
                    startActivity(intent);
                }
            }
        };



        tvscore.setOnClickListener(this);
        tvrank.setOnClickListener(this);
        totalquiz.setOnClickListener(this);


       /* userIcon1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), UpdateUserImageActivity.class);
                startActivity(intent);

            }
        });
*/
        /*   userIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CopyDbActivity.class);
                startActivity(intent);
            }
        });*/

        img_faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.gc();
                MenuSheetDialog menuSheetDialog = new MenuSheetDialog();
                menuSheetDialog.setStyle(BottomSheetDialogFragment.STYLE_NORMAL,R.style.CustomBottomSheetDialogTheme);
                Bundle bundle = new Bundle();

                bundle.putString("userid",eid);
                menuSheetDialog.setArguments(bundle);
                menuSheetDialog.show(getActivity().getSupportFragmentManager(), "quizapp2");

            }
        });
        return view;

    }

    private void getTotalQuizAndScore() {

       valueEventListener = databaseReference.child("daily_user_total_score").child(eid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {

                    int userscore = Integer.parseInt(dataSnapshot.child("total_score").getValue().toString());
                    tvscore.setText(""+userscore);

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

    private void getTotalQuizCount(){

       valueEventListener = databaseReference.child("daily_user_credit").child(eid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                    try {
                        String quizid = dataSnapshot1.getKey();
                        quiz_count =quiz_count+1;
                        totalquiz.setText(""+quiz_count);

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

    private void setTextFont() {
        Typeface type = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Regular.ttf");
        tvscore.setTypeface(type);
        username.setTypeface(type);
        totalquiz.setTypeface(type);
        todayque.setTypeface(type);
        tvtodayquiz.setTypeface(type);
        history.setTypeface(type);
        tvrank.setTypeface(type);

        tvran.setTypeface(type);
        tvquiz.setTypeface(type);
        tvsc.setTypeface(type);
        tvhistory.setTypeface(type);
        tvwin.setTypeface(type);
        tvcoin.setTypeface(type);
        tvscr.setTypeface(type);

    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(listener);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.quiznum || v.getId() == R.id.score || v.getId() == R.id.tvrank) {

            presentShowcaseSequence();

        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private void getQuizHistory() {


        valueEventListener = databaseReference.child("daily_user_credit").child(eid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {

                    try {
                        String id = data.getKey();
                        getLastQuiz(id);
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

    private void getLastQuiz(final String q_id){

        Query lastQuery = databaseReference.child("daily_user_credit").child(eid).child(q_id).orderByKey().limitToLast(1);


        historyViewList = new ArrayList<>();
        userQuizHistoryAdapter = new UserQuizHistoryAdapter(getActivity(), historyViewList,eid);

        lastQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    String id = dataSnapshot.getKey();

                    // String quiz = dataSnapshot1.child("quiz_name").getValue().toString();
                    String date = dataSnapshot1.child("date_time").getValue().toString();
                    String coins = dataSnapshot1.child("credit_points").getValue().toString();

                    Collections.sort(historyViewList, byDate);

                    if (date.equals(null)){
                        tvhistory.setText("No Quiz Taken Yet");
                        tvrank.setText("0");
                        totalquiz.setText("0");
                        tvscore.setText("0");

                        // dialog.dismiss();
                    }
                    else {
                        tvhistory.setVisibility(View.INVISIBLE);
                        quizimage.setVisibility(View.INVISIBLE);

                        historyViewList.add(new HistoryView(q_id, date, str, coins));
                        recyclerView.setAdapter(userQuizHistoryAdapter);

                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }



    private void presentShowcaseSequence() {

        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(100);

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(getActivity(), SHOWCASE_ID);

        sequence.setOnItemShownListener(new MaterialShowcaseSequence.OnSequenceItemShownListener() {
            @Override
            public void onShow(MaterialShowcaseView itemView, int position) {
                //   Toast.makeText(itemView.getContext(), "Item #" + position, Toast.LENGTH_SHORT).show();
            }
        });

        sequence.setConfig(config);

        // sequence.addSequenceItem(totalquiz, "Here you can see, How many Quiz has been taken by You.", "GOT IT");

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(getActivity())
                        .setSkipText("Skip")

                        .setTarget(layout1)
                        .setDismissText("Got it")
                        .setDismissOnTouch(true)
                        .setContentText("Here you can see, How many Quiz has been taken by You")
                        .withCircleShape()
                        .build()
        );

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(getActivity())
                        .setSkipText("Skip")
                        .setTarget(layout2)
                        .setDismissText("Got it")
                        .setDismissOnTouch(true)
                        .setContentText("Here you can see your Total Score")
                        .withCircleShape()
                        .build()
        );

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(getActivity())
                        .setSkipText("Skip")
                        .setTarget(layout3)
                        .setDismissText("Got it")
                        .setDismissOnTouch(true)
                        .setContentText("Here you can see your Rank")
                        .withCircleShape()
                        .build()
        );

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(getActivity())
                        .setSkipText("Skip")
                        .setTarget(todayque)
                        .setDismissText("Got it")
                        .setDismissOnTouch(true)
                        .setContentText("From here you can start playing Quiz")
                        .withCircleShape()
                        .build()
        );

        sequence.start();

    }

    private void getPlayerImage(){

        databaseReference.child("user_info").child(eid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                try {

                    String imageUrl = dataSnapshot.child("image_Url").getValue(String.class);

                    Picasso.with(getActivity())
                            .load(imageUrl)
                            .placeholder(R.drawable.userimg)
                            .fit()
                            .transform(transformation)
                            .into(userIcon);

                    Picasso.with(getActivity())
                            .load(imageUrl)
                            .placeholder(R.drawable.userimg)
                            .fit()
                            .transform(transformation)
                            .into(userIcon1);

                    dialog.dismiss();

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


    private void getAllTimeLeaderBoard(){

        valueEventListener =databaseReference.child("daily_user_total_score").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for (DataSnapshot data : dataSnapshot.getChildren()){

                    try {
                        userids1 = data.getKey();
                        getScoreForAllTimeLeaderBoard(userids1);

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


    private void getScoreForAllTimeLeaderBoard(final String userid){


       valueEventListener = databaseReference.child("daily_user_total_score").child(userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                    userscore = Integer.parseInt(dataSnapshot.child("total_score").getValue().toString());
                    // Toast.makeText(getActivity(), ""+ userscore,Toast.LENGTH_SHORT).show();
                    getUserNameForAllTimeLeaderBoard(userid,userscore);

                }catch (Exception e){
                    e.printStackTrace();
                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void getUserNameForAllTimeLeaderBoard(final String userids1, final int userscore) {

       valueEventListener = databaseReference.child("user_info").child(userids1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                username1 = dataSnapshot.child("student_name").getValue(String.class);
                String imageUrl = dataSnapshot.child("image_Url").getValue(String.class);

                //  Toast.makeText(getApplicationContext(),""+ userids1 + "\n"+ username+"\n"+userscore,Toast.LENGTH_SHORT).show();

                leaderBoardDataList.add(new LeaderBoardData(userids1, username1, userscore,imageUrl));

                Collections.sort(leaderBoardDataList, Collections.reverseOrder(new Comparator<LeaderBoardData>() {
                    @Override
                    public int compare(LeaderBoardData leaderBoardData, LeaderBoardData t1) {

                        return leaderBoardData.score - t1.score;
                    }
                }));

                leaderAdapter.inserRank();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    static final Comparator<HistoryView> byDate = new Comparator<HistoryView>() {
        @Override
        public int compare(HistoryView historyView, HistoryView t1) {

            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");

            Date d1 = null;
            Date d2 = null;
            try {
                d1 = sdf.parse(historyView.getId());
                d2 = sdf.parse(t1.getId());
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


            return (d1.getTime() > d2.getTime() ? -1 : 1);
        }


    };

   /* @Override
    public void onPause() {
        super.onPause();
        databaseReference.removeEventListener(valueEventListener);
    }*/
}
