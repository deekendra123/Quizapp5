package com.aptitude.education.e2buddy.Placement_Papers;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.aptitude.education.e2buddy.Intro.CheckInternet;
import com.aptitude.education.e2buddy.Intro.SessionManager;
import com.aptitude.education.e2buddy.R;
import com.aptitude.education.e2buddy.ViewData.BookmarkData;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/*
import butterknife.ButterKnife;
import butterknife.Unbinder;*/

/**
 * Created by Matrix on 07-01-2019.
 */

public class BookmarkBottomDialog extends BottomSheetDialogFragment {


    LinearLayout layout;
    DatabaseReference databaseReference;
    FirebaseAuth auth;
    RecyclerView recyclerViewBookmarks;
    private SessionManager sessionManager;
    List<BookmarkData> list;
    String playerId, playerName;
    private BookmarksAdapter bookmarksAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bookmark_bottem_sheet_dialog, container, false);

        recyclerViewBookmarks = view.findViewById(R.id.recyclerViewBookmarks);

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL);
        recyclerViewBookmarks.setLayoutManager(staggeredGridLayoutManager);

        sessionManager = new SessionManager(getActivity());
        HashMap<String, String> user = sessionManager.getData();
        playerId = user.get(SessionManager.KEY_PLAYER_ID);
        playerName = user.get(SessionManager.KEY_PLAYER_NAME);

        list = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        bookmarksAdapter = new BookmarksAdapter(getActivity(), list);

        CheckInternet checkInternet = new CheckInternet(getActivity());
        checkInternet.checkConnection();

        getBookmarks();

        return view;
    }

    private void getBookmarks(){
        databaseReference.child("placemenPaper_user_answer").child(playerId).child("quantitativeAbility").child("quiz1").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    String question_id = dataSnapshot1.getKey();
                    boolean bookmark_status = (boolean) dataSnapshot1.child("bookmark").getValue();
                    int position = Integer.parseInt(dataSnapshot1.child("position").getValue().toString());
                    String answer = dataSnapshot1.child("answer").getValue().toString();
                    Log.e("bookmark_status", question_id + "  "+ bookmark_status);
                    list.add(new BookmarkData(question_id, bookmark_status, position, answer));
                }
                recyclerViewBookmarks.setAdapter(bookmarksAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        bookmarksAdapter.setOnItemClickListener(new BookmarksAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {

                BookmarkData leaderBoardData = list.get(position);
                SharedPreferences sp = getActivity().getSharedPreferences("bookmark", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("questionid",leaderBoardData.getQuestion_id());
                editor.putInt("position", leaderBoardData.getPosition());
                editor.commit();
                dismiss();

                ((PlacementQuizQuestionsActivity) getActivity()).onResume();


            }
        });

    }
    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {

        View rootView = getActivity().getLayoutInflater().inflate(R.layout.bottem_sheet_dialog,null,false);
       // Unbinder unbinder = ButterKnife.bind(this, rootView);
        dialog.setContentView(rootView);
        FrameLayout bottomSheet = dialog.getWindow().findViewById(com.google.android.material.R.id.design_bottom_sheet);
        bottomSheet.setBackgroundResource(R.drawable.bglinear);
        super.setupDialog(dialog, style);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        System.gc();
    }





}
