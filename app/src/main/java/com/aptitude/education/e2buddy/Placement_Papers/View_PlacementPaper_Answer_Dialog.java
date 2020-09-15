package com.aptitude.education.e2buddy.Placement_Papers;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.style.QuoteSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.airbnb.lottie.L;
import com.aptitude.education.e2buddy.DisplayAnswer.ResultActivity;
import com.aptitude.education.e2buddy.Intro.SessionManager;
import com.aptitude.education.e2buddy.Question.StartQuizActivity;
import com.aptitude.education.e2buddy.R;
import com.aptitude.education.e2buddy.ViewData.AnswerView;
import com.aptitude.education.e2buddy.ViewData.BookmarkData;
import com.aptitude.education.e2buddy.ViewData.QuestionView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class View_PlacementPaper_Answer_Dialog extends DialogFragment {
    private View_PlacementPaper_Answer_Dialog.Callback callback;

    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    private String userid,useranswer,question,corr_ans,q_id;
    private com.aptitude.education.e2buddy.Placement_Papers.AnswerAdapter answerAdapter;
    private List<AnswerView> answerViewList;
    private ValueEventListener valueEventListener;
    private ProgressDialog progressDialog;
    private TextView tvQuestion,tvCorrectAnswer,tvUserAnswer, tvQuestionNumber;
    private  String answer,description;
    private ImageView imgBack;


    SessionManager sessionManager;

    public static View_PlacementPaper_Answer_Dialog newInstance() {
        return new View_PlacementPaper_Answer_Dialog();
    }

    public void setCallback(View_PlacementPaper_Answer_Dialog.Callback callback) {
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
        View view = inflater.inflate(R.layout.placementpaper_view_answer_dialog, container, false);

        ImageButton closeDialog = view.findViewById(R.id.fullscreen_dialog_close);
        recyclerView = view.findViewById(R.id.answerrecy);
        tvQuestion = view.findViewById(R.id.tvQuestion);
        tvCorrectAnswer = view.findViewById(R.id.tvCorrectAnswer);
        tvUserAnswer = view.findViewById(R.id.tvUserAnswer);
        tvQuestionNumber = view.findViewById(R.id.tvQuestionNumber);
        imgBack = view.findViewById(R.id.imgBack);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Data Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);
        progressDialog.setMax(100);
        progressDialog.show();

        sessionManager = new SessionManager(getActivity());

        HashMap<String, String> user = sessionManager.getData();

        userid = user.get(SessionManager.KEY_PLAYER_ID);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        answerViewList = new ArrayList<>();
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(5, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager); // set LayoutM
        databaseReference = FirebaseDatabase.getInstance().getReference();

        answerAdapter = new com.aptitude.education.e2buddy.Placement_Papers.AnswerAdapter(getActivity(), answerViewList);


        try {
            getQuestionIds();

        }catch (Exception e){
            e.printStackTrace();
        }

        closeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });


        loadScreen();


        return view;
    }

    private void loadScreen(){

       answerAdapter.setOnItemClickListener(new AnswerAdapter.OnItemClickListener() {
           @Override
           public void onItemClick(View itemView, int position) {
               AnswerView answerView = answerViewList.get(position);

               tvQuestionNumber.setText("Question "+answerView.getQuestionid());
               tvQuestion.setText(answerView.getQuestion());
               tvCorrectAnswer.setText(answerView.getCorrectAnswer());
               tvUserAnswer.setText(answerView.getUserAnswer());

               if (answerView.getUserAnswer().equals(answerView.getCorrectAnswer())){
                   imgBack.setBackgroundResource(R.drawable.correct);
               }
               else {
                   imgBack.setBackgroundResource(R.drawable.incorrect);

               }

           }
       });
    }

    private void getQuestionIds(){

        databaseReference.child("placementPaper").child("quantitativeAbility").child("quiz1").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                    q_id = dataSnapshot1.getKey();

                    getQuizQuestions(q_id);

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void getQuizQuestions(String q_id) {


        databaseReference.child("placementPaper").child("quantitativeAbility").child("quiz1").child(q_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                    question = dataSnapshot.child("question").getValue(String.class);
                    answer = dataSnapshot.child("answer").getValue(String.class);
                    description = dataSnapshot.child("description").getValue(String.class);

                    getUserAnswers(q_id, question, answer, description);

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void getUserAnswers(String questionid, String question, String corr_ans, String description){

        databaseReference.child("placemenPaper_user_answer").child(userid).child("quantitativeAbility").child("quiz1").child(questionid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String userAnswer = (String) dataSnapshot.child("answer").getValue();

                answerViewList.add(new AnswerView(questionid, question, corr_ans, userAnswer, description));
                recyclerView.setAdapter(answerAdapter);
                progressDialog.dismiss();

                if (questionid.equals("1")){
                    tvQuestionNumber.setText("Question "+questionid);
                    tvQuestion.setText(question);
                    tvCorrectAnswer.setText(corr_ans);
                    tvUserAnswer.setText(userAnswer);


                    if (userAnswer.equals(corr_ans)){
                        imgBack.setBackgroundResource(R.drawable.correct);
                    }
                    else {
                        imgBack.setBackgroundResource(R.drawable.incorrect);
                    }
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
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }
}
