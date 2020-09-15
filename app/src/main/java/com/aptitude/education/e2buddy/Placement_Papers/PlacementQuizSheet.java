package com.aptitude.education.e2buddy.Placement_Papers;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
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

import com.aptitude.education.e2buddy.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
/*
import butterknife.ButterKnife;
import butterknife.Unbinder;*/

/**
 * Created by Matrix on 07-01-2019.
 */

public class PlacementQuizSheet extends BottomSheetDialogFragment {


    String userid;
    LinearLayout layout;
    DatabaseReference databaseReference;
    FirebaseAuth auth;
    RecyclerView recyclerView;
    List<String> list;
    PlacementPaperAdapter answerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.placement_quiz_bottem_sheet_dialog, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager); // set LayoutM

        list = new ArrayList<>();
        answerAdapter = new PlacementPaperAdapter(getActivity(), list);


        auth = FirebaseAuth.getInstance();
        final FirebaseUser user = auth.getCurrentUser();
        userid = user.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        list.add("Beginner 1");
        list.add("Beginner 2");
        list.add("Intermediate 1");
        list.add("Intermediate 2");
        list.add("Advance 1");
        list.add("Advance 2");

        recyclerView.setAdapter(answerAdapter);

        return view;
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
