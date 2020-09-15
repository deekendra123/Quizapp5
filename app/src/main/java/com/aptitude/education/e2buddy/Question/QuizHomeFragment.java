package com.aptitude.education.e2buddy.Question;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;

import com.aptitude.education.e2buddy.MainActivity;
import com.aptitude.education.e2buddy.Pushdatatofirebase.CopyDbActivity;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aptitude.education.e2buddy.Intro.IntroActivity;
import com.aptitude.education.e2buddy.R;
import com.aptitude.education.e2buddy.School_Quiz.StudentProfileActivity;
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


public class QuizHomeFragment extends Fragment implements TabLayout.OnTabSelectedListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private TabLayout tabLayout;

    //This is our viewPager
    private ViewPager viewPager;
    ImageView userIcon1;
    TextView username, tvscore;
    Transformation transformation;
    DatabaseReference databaseReference;
    String student_name,eid,imageUrl,student_email;
    FirebaseAuth auth;
    public QuizHomeFragment() {
        // Required empty public constructor
    }

     // TODO: Rename and change types and number of parameters
    public static QuizHomeFragment newInstance(String param1, String param2) {
        QuizHomeFragment fragment = new QuizHomeFragment();
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

        View view = inflater.inflate(R.layout.fragment_quiz_home, container, false);

        userIcon1 = view.findViewById(R.id.userimg);
        username = view.findViewById(R.id.user);
        tvscore = view.findViewById(R.id.score);
        tabLayout = view.findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Daily Diet"));
        tabLayout.addTab(tabLayout.newTab().setText("Topics"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = view.findViewById(R.id.pager);
       // tabLayout.setupWithViewPager(viewPager);

        View root = tabLayout.getChildAt(0);
        if (root instanceof LinearLayout) {
            ((LinearLayout) root).setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
            GradientDrawable drawable = new GradientDrawable();
            drawable.setColor(getResources().getColor(R.color.white));
            drawable.setSize(1, 1);
            ((LinearLayout) root).setDividerPadding(10);
            ((LinearLayout) root).setDividerDrawable(drawable);
        }

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            auth = FirebaseAuth.getInstance();
            final FirebaseUser user = auth.getCurrentUser();
            eid = user.getUid();

        }
        else{
            Intent intent = new Intent(getActivity(), IntroActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
        transformation = new RoundedTransformationBuilder()
                .borderColor(getResources().getColor(R.color.white))
                .borderWidthDp(0)
                .cornerRadiusDp(50)
                .oval(false)
                .build();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        getUserData();
        getTotalQuizAndScore();

        QuizHomeFragmentPagerAdapter adapter = new QuizHomeFragmentPagerAdapter(getActivity().getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(this);

        userIcon1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), StudentProfileActivity.class);
           //Intent intent = new Intent(getActivity(), CopyDbActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void getUserData(){
        databaseReference.child("user_info").child(eid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {

                    student_name = dataSnapshot.child("student_name").getValue().toString();
                    student_email = dataSnapshot.child("email").getValue().toString();
                    imageUrl = dataSnapshot.child("image_Url").getValue(String.class);

                    Picasso.get()
                            .load(imageUrl)
                            .placeholder(R.drawable.userimg)
                            .fit()
                            .transform(transformation)
                            .into(userIcon1);


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

    private void getTotalQuizAndScore() {
        PlayerTotalScore playerTotalScore = new PlayerTotalScore(databaseReference, eid, tvscore);
        playerTotalScore.getPlayerScore();
    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {

        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onResume() {
        super.onResume();

            PlayerTotalScore playerTotalScore = new PlayerTotalScore(databaseReference, eid, tvscore);
            playerTotalScore.getPlayerScore();
    }
}
