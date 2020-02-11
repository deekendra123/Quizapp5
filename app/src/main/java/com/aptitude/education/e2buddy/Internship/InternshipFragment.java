package com.aptitude.education.e2buddy.Internship;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aptitude.education.e2buddy.Intro.SessionManager;
import com.aptitude.education.e2buddy.Placement_Papers.PlacementPaperAdapter;
import com.aptitude.education.e2buddy.R;
import com.aptitude.education.e2buddy.ViewData.HistoryView;
import com.aptitude.education.e2buddy.ViewData.InternshipData;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class InternshipFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private RecyclerView recyclerInternship;
    DatabaseReference databaseReference;
    String playerId, playerName, playerEmail, playerImageUrl;

    SessionManager sessionManager;
    ProgressDialog progressDialog;
    RelativeLayout relativeLayout;
    ImageView imgLogo;
    TextView tvInternshipName,textMsg, tvDateTo, tvDateFrom;
    LinearLayout linearLayout,linearCongo;


    public InternshipFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static InternshipFragment newInstance(String param1, String param2) {
        InternshipFragment fragment = new InternshipFragment();
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
        View view = inflater.inflate(R.layout.fragment_internship, container, false);

        recyclerInternship = view.findViewById(R.id.recyclerInternship);
        relativeLayout = view.findViewById(R.id.relativeLayout);
        tvInternshipName = view.findViewById(R.id.tvInternshipName);
        imgLogo = view.findViewById(R.id.imgLogo);
        linearLayout = view.findViewById(R.id.layout14);
        linearCongo = view.findViewById(R.id.linearCongo);
        textMsg = view.findViewById(R.id.textMsg);
        tvDateTo = view.findViewById(R.id.tvDateTo);
        tvDateFrom = view.findViewById(R.id.tvDateFrom);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false);
        recyclerInternship.setLayoutManager(layoutManager);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        sessionManager = new SessionManager(getActivity());

        HashMap<String, String> user = sessionManager.getData();

        playerId = user.get(SessionManager.KEY_PLAYER_ID);
        playerName = user.get(SessionManager.KEY_PLAYER_NAME);
        playerEmail = user.get(SessionManager.KEY_PLAYER_EMAIL);
        playerImageUrl = user.get(SessionManager.KEY_PLAYER_IMAGE_URL);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Data Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);
        progressDialog.setMax(100);
        progressDialog.show();



        try {
            loadInternshipData();

        }catch (Exception e){
            e.printStackTrace();
        }


        return view;
    }

    private void loadInternshipData(){

        databaseReference.child("internshipRequest").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(playerId)){


                    Query lastQuery = databaseReference.child("internshipRequest").child(playerId).orderByKey().limitToLast(1);

                    lastQuery.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {


                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {


                                String key = dataSnapshot1.getKey();
                                String status = dataSnapshot1.child("status").getValue(String.class);
                                String course = dataSnapshot1.child("course").getValue().toString();
                                String dateFrom = dataSnapshot1.child("dateFrom").getValue().toString();
                                String dateTo = dataSnapshot1.child("dateTo").getValue().toString();

                                if (status.equals("pending")) {

                                    relativeLayout.setVisibility(View.VISIBLE);
                                    recyclerInternship.setVisibility(View.INVISIBLE);
                                    linearLayout.setVisibility(View.VISIBLE);
                                    linearCongo.setVisibility(View.INVISIBLE);
                                    loadPendingStatus(key);

                                } else if (status.equals("approved")) {

                                    relativeLayout.setVisibility(View.INVISIBLE);
                                    recyclerInternship.setVisibility(View.INVISIBLE);
                                    linearLayout.setVisibility(View.INVISIBLE);
                                    linearCongo.setVisibility(View.VISIBLE);
                                    textMsg.setText("You have been shortlisted for the "+ course+ " Internship.");
                                    tvDateFrom.setText(""+dateFrom);
                                    tvDateTo.setText(""+dateTo);
                                    progressDialog.dismiss();

                                    } else if (status.equals("rejected")) {

                                    relativeLayout.setVisibility(View.INVISIBLE);
                                    recyclerInternship.setVisibility(View.VISIBLE);
                                    linearLayout.setVisibility(View.VISIBLE);
                                    linearCongo.setVisibility(View.INVISIBLE);
                                    loadInternshipRejectedStatus();

                                }

                                }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                else {

                    Log.e("msg", "not exist");
                    loadInternshipRejectedStatus();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }

    private void loadPendingStatus(String id){
        databaseReference.child("internshipRequest").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                    String name = dataSnapshot.child(playerId).child(id).child("course").getValue().toString();
                    String url = dataSnapshot.child(playerId).child(id).child("image").getValue().toString();
                    Log.e("msg", "exist"+ "  "+ name);

                    Picasso.get().load(url).into(imgLogo);
                    tvInternshipName.setText(name+ " Internship");


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


    private void loadInternshipRejectedStatus() {
        List<InternshipData> list = new ArrayList<>();

        InternshipAdapter internshipAdapter = new InternshipAdapter(getActivity(), list);


        databaseReference.child("internshipDetails").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    String card_id = dataSnapshot1.getKey();
                    String name = dataSnapshot1.child("name").getValue().toString();
                    String logo = dataSnapshot1.child("logo").getValue().toString();
                    int remainingSlots = Integer.parseInt(dataSnapshot1.child("remainingSlots").getValue().toString());
                    int totalSlots = Integer.parseInt(dataSnapshot1.child("totalSlots").getValue().toString());
                    String imageUrl = dataSnapshot1.child("image").getValue().toString();
                    list.add(new InternshipData(card_id,logo,name, imageUrl ,remainingSlots,totalSlots));


                }

                recyclerInternship.setAdapter(internshipAdapter);
                progressDialog.dismiss();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        internshipAdapter.setOnItemClickListener(new InternshipAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                InternshipData internshipData = list.get(position);
                InternshipDetailsBottemSheet internshipDetailsBottemSheet = new InternshipDetailsBottemSheet();
                internshipDetailsBottemSheet.setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
                Bundle bundle = new Bundle();
                bundle.putString("imageUrl", internshipData.getImageUrl());
                internshipDetailsBottemSheet.setArguments(bundle);
                internshipDetailsBottemSheet.show(getActivity().getSupportFragmentManager(), "quizapp2");


            }
        });

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
