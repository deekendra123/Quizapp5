package com.aptitude.education.e2buddy.Quiz_Category;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.aptitude.education.e2buddy.DisplayAnswer.AnswerAdapter;
import com.aptitude.education.e2buddy.R;
import com.aptitude.education.e2buddy.ViewData.AnswerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class View_Category_Answer_Dialog extends DialogFragment {
    private View_Category_Answer_Dialog.Callback callback;

    RecyclerView recyclerView;
    TextView tvuser,tvanswer,tvCorrectAnswer;
    Button bttry;
    DatabaseReference databaseReference;
    String userid,useranswer,question,corr_ans,q_id, parent_category, child_category;
    int quiz_id;
    AnswerAdapter answerAdapter;
    List<AnswerView> answerViewList;
    ValueEventListener valueEventListener;
    ProgressDialog progressDialog;

    public static View_Category_Answer_Dialog newInstance() {
        return new View_Category_Answer_Dialog();
    }

    public void setCallback(View_Category_Answer_Dialog.Callback callback) {
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

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Data Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);
        progressDialog.setMax(100);
        progressDialog.show();

        Bundle bundle = getArguments();
        parent_category = bundle.getString("parent_category","");
        child_category = bundle.getString("child_category","");
        quiz_id = bundle.getInt("quiz_id");
        userid = bundle.getString("userid","");

        Log.e("data ", parent_category+ child_category+quiz_id+userid);



        answerViewList = new ArrayList<>();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        getTodayQuizScore();

        answerAdapter = new AnswerAdapter(getActivity(), answerViewList);
        recyclerView.setAdapter(answerAdapter);

       databaseReference.child("category_questions").child(parent_category).child(child_category).child(String.valueOf(quiz_id)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    q_id = dataSnapshot1.getKey();
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


                System.gc();
                Intent intent = new Intent(getActivity(), StartCategoryQuizActivity.class);
                intent.putExtra("parent_category",parent_category);
                intent.putExtra("child_category", child_category);
                intent.putExtra("quiz_id", quiz_id);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                getActivity().startActivity(intent);

                CategoryResultActivity.getInstance().finish();
                dismiss();


                
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
               getActivity().onBackPressed();
            }
        });


        return view;
    }

    private void getUserAnswer(final String q_id) {

     databaseReference.child("category_user_answer").child(userid).child(parent_category).child(child_category).child(String.valueOf(quiz_id)).child(q_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                useranswer = dataSnapshot.child("useranswer").getValue(String.class);
               // Log.d("AnswerActivity", "questionid :"+questionId);

                try {

                    if (useranswer==null){
                        useranswer.replace(null, "question was not given");
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
       databaseReference.child("category_user_credit").child(userid).child(parent_category).child(child_category).child(String.valueOf(quiz_id)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                try {
                    String corr_answer = dataSnapshot.child("correct_answers").getValue().toString();
                    tvCorrectAnswer.setText(""+corr_answer+"/10");
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


    private void getQuestion(final String questionId, final String useranswer){

      valueEventListener =  databaseReference.child("category_questions").child(parent_category).child(child_category).child(String.valueOf(quiz_id)).child(questionId)
              .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    question = dataSnapshot.child("Question").getValue(String.class);
                    corr_ans = dataSnapshot.child("Answer").getValue(String.class);
                    answerViewList.add(new AnswerView(
                            questionId,  question, corr_ans, useranswer
                    ));

                    answerAdapter = new AnswerAdapter(getActivity(), answerViewList);
                    recyclerView.setAdapter(answerAdapter);
                    progressDialog.dismiss();

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
