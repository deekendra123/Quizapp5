package com.aptitude.education.e2buddy.One_on_One_Quiz_Challenge;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import com.aptitude.education.e2buddy.Intro.CheckInternet;
import com.aptitude.education.e2buddy.R;
import com.aptitude.education.e2buddy.ViewData.OnlineStatusData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SendQuizRequestActivity extends AppCompatActivity {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    RecyclerView recyclerView;
    DatabaseReference databaseReference,databaseReference1;
    String username, useremail;
    boolean online_status;
    SendRequestToOpponentAdapter sendRequestToOpponentAdapter;
    private SendRequestToOpponentFragment.OnFragmentInteractionListener mListener;
    List<OnlineStatusData> list;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String quizid,quizname,userid,quizdate, user_id;
    ProgressDialog progressDialog;
    Toolbar toolbar;
    EditText searchView;
    Button btsearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_quiz_request);

        recyclerView = findViewById(R.id.onlineusers);
        searchView = findViewById(R.id.searchView);
     //
        //   btsearch = findViewById(R.id.button4);
        list = new ArrayList<>();

        CheckInternet checkInternet = new CheckInternet(getApplicationContext());
        checkInternet.checkConnection();


        sharedPreferences = getSharedPreferences("quizpref", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        quizid = sharedPreferences.getString("quizid", "");
        quizname = sharedPreferences.getString("quizname", "");
        userid = sharedPreferences.getString("userid", "");
        quizdate = sharedPreferences.getString("quizdate","");

        progressDialog = new ProgressDialog(SendQuizRequestActivity.this);
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Data Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);
        progressDialog.setMax(100);
        progressDialog.show();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        sendRequestToOpponentAdapter = new SendRequestToOpponentAdapter(getApplicationContext(),list);

        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));


        getOnlineUser();

        searchView.addTextChangedListener(new TextWatcher() {
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


}

