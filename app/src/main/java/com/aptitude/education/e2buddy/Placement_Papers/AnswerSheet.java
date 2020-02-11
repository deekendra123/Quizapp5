package com.aptitude.education.e2buddy.Placement_Papers;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.aptitude.education.e2buddy.Menu.FAQ_Dialog;
import com.aptitude.education.e2buddy.Menu.FeedbackFullScreenDialog;
import com.aptitude.education.e2buddy.Menu.ShareAndEarnDialog;
import com.aptitude.education.e2buddy.R;
import com.aptitude.education.e2buddy.School_Quiz.StudentProfileActivity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
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
import java.util.List;

/*
import butterknife.ButterKnife;
import butterknife.Unbinder;
*/

/**
 * Created by Matrix on 07-01-2019.
 */

public class AnswerSheet extends BottomSheetDialogFragment {


    String userid;
    LinearLayout layout;
    DatabaseReference databaseReference;
    FirebaseAuth auth;
    RecyclerView recyclerView;
    List<String> list;
    AnswerAdapter answerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.placement_answer_bottem_sheet_dialog, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager); // set LayoutM

        list = new ArrayList<>();
        answerAdapter = new AnswerAdapter(getActivity(), list);


        auth = FirebaseAuth.getInstance();
        final FirebaseUser user = auth.getCurrentUser();
        userid = user.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        list.add("6");
        list.add("7");
        list.add("8");
        list.add("9");
        list.add("10");

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
