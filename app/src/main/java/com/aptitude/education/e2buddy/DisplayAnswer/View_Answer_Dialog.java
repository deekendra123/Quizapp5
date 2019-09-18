package com.aptitude.education.e2buddy.DisplayAnswer;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aptitude.education.e2buddy.Intro.CheckInternet;
import com.aptitude.education.e2buddy.Intro.Quizapp;
import com.aptitude.education.e2buddy.Question.HomeNevActivity;
import com.aptitude.education.e2buddy.Question.StartQuizActivity;
import com.aptitude.education.e2buddy.R;
import com.aptitude.education.e2buddy.ViewData.AnswerView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class View_Answer_Dialog extends DialogFragment {
    private View_Answer_Dialog.Callback callback;

    RecyclerView recyclerView;
    TextView tvuser,tvanswer,tvCorrectAnswer;
    Button bttry;
    DatabaseReference databaseReference;
    String userid,useranswer,question,corr_ans, questionId,quizdate;
    String TAG = getClass().getSimpleName();
    AnswerAdapter answerAdapter;
    List<AnswerView> answerViewList;
    String q_id,value;
    ValueEventListener valueEventListener;
    ProgressDialog progressDialog;

    public static View_Answer_Dialog newInstance() {
        return new View_Answer_Dialog();
    }

    public void setCallback(View_Answer_Dialog.Callback callback) {
        this.callback = callback;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(android.app.DialogFragment.STYLE_NORMAL, R.style.FullscreenDialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_answer_dialog, container, false);

        ImageButton closeDialog = view.findViewById(R.id.fullscreen_dialog_close);
        ImageButton homeButton = view.findViewById(R.id.fullscreen_dialog_home);
        tvuser = view.findViewById(R.id.tvuser);

        recyclerView = view.findViewById(R.id.answerrecy);
        bttry = view.findViewById(R.id.bttryagain);

        tvCorrectAnswer = view.findViewById(R.id.tvcorrectans);
        tvanswer = view.findViewById(R.id.tvans);

        Quizapp.getRefWatcher(getActivity()).watch(this);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Data Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);
        progressDialog.setMax(100);
        progressDialog.show();


        Runnable progressRunnable = new Runnable() {

            @Override
            public void run() {
                progressDialog.dismiss();

            }
        };

        Handler pdCanceller = new Handler();
        pdCanceller.postDelayed(progressRunnable, 3500);

        Bundle bundle = getArguments();
        quizdate = bundle.getString("quiz_date","");
        value = bundle.getString("curent_date","");
        userid = bundle.getString("userid","");

        answerViewList = new ArrayList<>();


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        databaseReference = FirebaseDatabase.getInstance().getReference();



        getTodayQuizScore();

        for (int i=0;i<answerViewList.size();i++){
            AnswerView answerView = answerViewList.get(i);

            Toast.makeText(getActivity(), ""+ answerView.getCorrectAnswer(),Toast.LENGTH_SHORT).show();

        }

        answerAdapter = new AnswerAdapter(getActivity(), answerViewList);
        recyclerView.setAdapter(answerAdapter);

       valueEventListener = databaseReference.child("daily_Question").child(quizdate).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                    q_id = dataSnapshot1.getKey();

                    //Toast.makeText(getApplicationContext(), ""+q_id,Toast.LENGTH_SHORT).show();

                    getUserAnswer(q_id);

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        bttry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                databaseReference.child("daily_user_credit").child(userid).child(quizdate).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {
                            long count = dataSnapshot.getChildrenCount();
                            //  Toast.makeText(getApplicationContext(), ""+ count,Toast.LENGTH_SHORT).show();
                            if (count<3){


                                System.gc();
                                Intent intent = new Intent(getActivity(), StartQuizActivity.class);
                                intent.putExtra("quiz_date", quizdate);
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

        closeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), HomeNevActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });


        return view;
    }

    private void getUserAnswer(final String q_id) {

       valueEventListener = databaseReference.child("daily_user_answer").child(userid).child(quizdate).child(value).child(q_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                useranswer = dataSnapshot.child("useranswer").getValue(String.class);
                Log.d("AnswerActivity", "questionid :"+questionId);

                try {

                    if (useranswer==null){
                        String.valueOf(useranswer.replace(null, "question was not given"));
                    }

                    getQuestion(q_id, useranswer);

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

    private void getTodayQuizScore(){
       valueEventListener = databaseReference.child("daily_user_credit").child(userid).child(quizdate).child(value).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                try {
                    String score = dataSnapshot.child("credit_points").getValue().toString();
                    String corr_answer = dataSnapshot.child("correct_answers").getValue().toString();
                    tvCorrectAnswer.setText(""+corr_answer+"/10");
                    // progressDialog.dismiss();

                }

                catch (Exception e){
                    e.printStackTrace();
                }
                //   progressDialog.dismiss();

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    private void getQuestion(final String questionId, final String useranswer){

      valueEventListener =  databaseReference.child("questions").child(questionId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    Log.d(TAG, "children are: "+dataSnapshot.getKey());

                    Log.d(TAG, "childs: "+dataSnapshot.getKey());
                    question = dataSnapshot.child("Question").getValue(String.class);
                    corr_ans = dataSnapshot.child("Answer").getValue(String.class);

                    answerViewList.add(new AnswerView(
                            questionId,  question, corr_ans, useranswer
                    ));

                    answerAdapter = new AnswerAdapter(getActivity(), answerViewList);
                    recyclerView.setAdapter(answerAdapter);

//                    progressDialog.dismiss();


                }catch (NullPointerException | DatabaseException e){
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

    @Override
    public void onPause() {
        super.onPause();
        databaseReference.removeEventListener(valueEventListener);
    }
}
