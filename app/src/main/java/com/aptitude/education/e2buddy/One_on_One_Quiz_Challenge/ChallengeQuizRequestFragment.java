package com.aptitude.education.e2buddy.One_on_One_Quiz_Challenge;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aptitude.education.e2buddy.Intro.CheckInternet;
import com.aptitude.education.e2buddy.R;


public class ChallengeQuizRequestFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    SharedPreferences sharedPreferences;
    String uname;
    TextView tvname;
    CardView cardsingleplaye, cardmultipleplayer;

    private OnFragmentInteractionListener mListener;
    TextView tv1,tv2,tv3,tv4,tv5,tv6,tv7,tv8;
    ImageView bt1,bt2;

    public ChallengeQuizRequestFragment() {
        // Required empty public constructor
    }
    // TODO: Rename and change types and number of parameters
    public static ChallengeQuizRequestFragment newInstance(String param1, String param2) {
        ChallengeQuizRequestFragment fragment = new ChallengeQuizRequestFragment();
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

        View view = inflater.inflate(R.layout.fragment_challenge_quiz_request, container, false);
        tvname = view.findViewById(R.id.e2);
        tv1 = view.findViewById(R.id.tv1);
        tv2 = view.findViewById(R.id.tv2);
        tv3 = view.findViewById(R.id.tv3);
        tv4 = view.findViewById(R.id.tv4);
        tv5 = view.findViewById(R.id.tv5);
        tv6 = view.findViewById(R.id.tv6);
        tv7 = view.findViewById(R.id.bt4);
        tv8 = view.findViewById(R.id.bt3);

        cardmultipleplayer = view.findViewById(R.id.card_view1);
        cardsingleplaye = view.findViewById(R.id.card_view);

        CheckInternet checkInternet = new CheckInternet(getActivity());
        checkInternet.checkConnection();


        sharedPreferences = getActivity().getSharedPreferences("quizpref", Context.MODE_PRIVATE);
        uname = sharedPreferences.getString("username", "");
        tvname.setText(""+uname);


        Typeface type = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Regular.ttf");
        tv1.setTypeface(type);
        tv2.setTypeface(type);
        tv3.setTypeface(type);
        tv4.setTypeface(type);
        tv5.setTypeface(type);
        tv6.setTypeface(type);
        tv7.setTypeface(type);
        tv8.setTypeface(type);
        tvname.setTypeface(type);

        cardsingleplaye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), SendQuizRequestActivity.class);
                startActivity(intent);
            }
        });

        cardmultipleplayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return view;


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


}
