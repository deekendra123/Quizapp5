package com.aptitude.education.e2buddy.Placement_Papers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.aptitude.education.e2buddy.DisplayAnswer.AnswerAdapter;
import com.aptitude.education.e2buddy.DisplayAnswer.ResultActivity;
import com.aptitude.education.e2buddy.Intro.SessionManager;
import com.aptitude.education.e2buddy.Question.StartQuizActivity;
import com.aptitude.education.e2buddy.R;
import com.aptitude.education.e2buddy.ViewData.AnswerView;
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

    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    String userid,useranswer,question,corr_ans,q_id;
    com.aptitude.education.e2buddy.Placement_Papers.AnswerAdapter answerAdapter;
    List<String> answerViewList;
    ValueEventListener valueEventListener;
    ProgressDialog progressDialog;
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
        ImageButton homeButton = view.findViewById(R.id.fullscreen_dialog_home);
        recyclerView = view.findViewById(R.id.answerrecy);

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

        answerViewList = new ArrayList<>();
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(5, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager); // set LayoutM
        databaseReference = FirebaseDatabase.getInstance().getReference();

        answerAdapter = new com.aptitude.education.e2buddy.Placement_Papers.AnswerAdapter(getActivity(), answerViewList);

        answerViewList.add("1");
        answerViewList.add("2");
        answerViewList.add("3");
        answerViewList.add("4");
        answerViewList.add("5");
        answerViewList.add("6");
        answerViewList.add("7");
        answerViewList.add("8");
        answerViewList.add("9");
        answerViewList.add("10");

        recyclerView.setAdapter(answerAdapter);
        progressDialog.dismiss();





        closeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });


        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    }






    public interface Callback {

        void onActionClick(String name);

    }

}
