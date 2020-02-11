package com.aptitude.education.e2buddy.One_on_One_Quiz_Challenge;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.aptitude.education.e2buddy.Intro.CheckInternet;
import com.aptitude.education.e2buddy.Menu.MenuSheetDialog;
import com.aptitude.education.e2buddy.R;
import com.aptitude.education.e2buddy.ViewData.OnlineStatusData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SendRequestToOpponentFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    RecyclerView recyclerView;
    DatabaseReference databaseReference,databaseReference1;
    String username, imageUrl;
    boolean online_status;
    SendRequestToOpponentAdapter sendRequestToOpponentAdapter;
    private OnFragmentInteractionListener mListener;
    List<OnlineStatusData> list;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String quizid,quizname,userid,quizdate, user_id;
    ProgressDialog progressDialog;
    ImageView img_info;
    EditText searchView1;
    TextView tvchallenge;
    public SendRequestToOpponentFragment() {
        // Required empty public constructor
    }

    public static SendRequestToOpponentFragment newInstance(String param1, String param2) {
        SendRequestToOpponentFragment fragment = new SendRequestToOpponentFragment();
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

        View view = inflater.inflate(R.layout.fragment_send_request_to_opponent, container, false);

        recyclerView = view.findViewById(R.id.onlineusers);
        searchView1 = view.findViewById(R.id.searchView);
        tvchallenge = view.findViewById(R.id.tvchallenge);
        img_info = view.findViewById(R.id.img_info);

        list = new ArrayList<>();

        CheckInternet checkInternet = new CheckInternet(getActivity());
        checkInternet.checkConnection();


        Typeface type = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Regular.ttf");
        tvchallenge.setTypeface(type);
        searchView1.setTypeface(type);

        sharedPreferences = getActivity().getSharedPreferences("quizpref", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        quizid = sharedPreferences.getString("quizid", "");
        quizname = sharedPreferences.getString("quizname", "");
        userid = sharedPreferences.getString("userid", "");
        quizdate = sharedPreferences.getString("quizdate","");

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Data Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);
        progressDialog.setMax(100);
        progressDialog.show();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        sendRequestToOpponentAdapter = new SendRequestToOpponentAdapter(getActivity(),list);

        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));


        getOnlineUser();

        searchView1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                editable.toString();
                filter(editable.toString());
            }
        });

        img_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MenuSheetDialog menuSheetDialog = new MenuSheetDialog();
                menuSheetDialog.setStyle(BottomSheetDialogFragment.STYLE_NORMAL,R.style.CustomBottomSheetDialogTheme);
                menuSheetDialog.show(getActivity().getSupportFragmentManager(), "quizapp2");

            }
        });

        return view;
    }

    private void filter(String text) {
        ArrayList<OnlineStatusData> list1 = new ArrayList<>();

        for (OnlineStatusData onlineStatusData : list){
            if (onlineStatusData.getUser_name().toLowerCase().contains(text.toLowerCase())) {
                list1.add(onlineStatusData);
            }
        }
        sendRequestToOpponentAdapter.filterList(list1);
    }

    private void getOnlineUser(){

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference1 = FirebaseDatabase.getInstance().getReference();
        // databaseReference1.child("online_users").child(userid).removeValue();

        databaseReference.child("online_users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                list.clear();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                    try {

                        sendRequestToOpponentAdapter.notifyItemRangeChanged(0, list.size());
                        user_id = dataSnapshot1.getKey();
                        username = dataSnapshot1.child("user_name").getValue().toString();
                        imageUrl = dataSnapshot1.child("imageUrl").getValue().toString();
                        online_status = Boolean.parseBoolean(dataSnapshot1.child("online_status").getValue().toString());

                        if (online_status==true && !user_id.equals(userid)){
                            list.add(new OnlineStatusData(username, online_status, user_id, imageUrl));
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }

                recyclerView.setAdapter(sendRequestToOpponentAdapter);
                sendRequestToOpponentAdapter.notifyDataSetChanged();

                progressDialog.dismiss();
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
