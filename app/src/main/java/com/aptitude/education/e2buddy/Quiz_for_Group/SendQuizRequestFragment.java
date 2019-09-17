package com.aptitude.education.e2buddy.Quiz_for_Group;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.aptitude.education.e2buddy.R;
import com.aptitude.education.e2buddy.ViewData.OnlineStatusData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SendQuizRequestFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    RecyclerView recyclerView;
    DatabaseReference databaseReference,databaseReference1;
    String username, useremail;
    boolean online_status;
    SendQuizRequestAdapter sendRequestToOpponentAdapter;

    List<OnlineStatusData> list;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String quizid,quizname,userid,quizdate, user_id;
    ProgressDialog progressDialog;
    Toolbar toolbar;
    SearchView searchView;
    Button playquiz;


    public SendQuizRequestFragment() {
        // Required empty public constructor
    }

    public static SendQuizRequestFragment newInstance(String param1, String param2) {
        SendQuizRequestFragment fragment = new SendQuizRequestFragment();
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

        View view = inflater.inflate(R.layout.fragment_send_quiz_request, container, false);

        recyclerView = view.findViewById(R.id.onlineusers);
        toolbar = view.findViewById(R.id.toolbar);
        playquiz = view.findViewById(R.id.btplay);
        list = new ArrayList<>();

        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);



        setHasOptionsMenu(true);

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
        sendRequestToOpponentAdapter = new SendQuizRequestAdapter(getActivity(),list, playquiz);

        getOnlineUser();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search, menu);
        final MenuItem menuItem = menu.findItem(R.id.action_filter);

        searchView = (SearchView) menuItem.getActionView();
        ((EditText)searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text)).setHintTextColor(getResources().getColor(R.color.white));

        searchView.setQueryHint("Search your friends");


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if (!searchView.isIconified()){
                    searchView.setIconified(true);
                }
                menuItem.collapseActionView();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                final List<OnlineStatusData> data = filter(list,newText);
                sendRequestToOpponentAdapter.setFilter(data);
                return true;
            }
        });

    }

    private List<OnlineStatusData> filter(List<OnlineStatusData> dataList, String query){

        query = query.toLowerCase();
        final List<OnlineStatusData> onlineStatusDataList = new ArrayList<>();
        for (OnlineStatusData onlineStatusData : dataList){
            final String text = onlineStatusData.getUser_name().toLowerCase();

            if (text.startsWith(query)){
                onlineStatusDataList.add(onlineStatusData);
            }
        }
        return onlineStatusDataList;
    }
    private void getOnlineUser(){

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference1 = FirebaseDatabase.getInstance().getReference();
        databaseReference1.child("online_users").child(userid).removeValue();

        databaseReference.child("online_users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                list.clear();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                    sendRequestToOpponentAdapter.notifyItemRangeChanged(0, list.size());
                    user_id = dataSnapshot1.getKey();
                    username = dataSnapshot1.child("user_name").getValue().toString();
                    useremail = dataSnapshot1.child("user_email").getValue().toString();
                    online_status = Boolean.parseBoolean(dataSnapshot1.child("online_status").getValue().toString());

                    list.add(new OnlineStatusData(username, online_status, user_id));
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
