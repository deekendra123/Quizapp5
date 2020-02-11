package com.aptitude.education.e2buddy.Placement_Papers;

import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aptitude.education.e2buddy.R;

import java.util.ArrayList;
import java.util.List;

public class PlacementQuizFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private RecyclerView recyclerViewCompanies;
    private List<String> list;

    public PlacementQuizFragment() {

    }

    // TODO: Rename and change types and number of parameters
    public static PlacementQuizFragment newInstance(String param1, String param2) {
        PlacementQuizFragment fragment = new PlacementQuizFragment();
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

        View view = inflater.inflate(R.layout.fragment_placement_quiz, container, false);
        recyclerViewCompanies = view.findViewById(R.id.recyclerViewCompanies);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewCompanies.setLayoutManager(layoutManager);
        loadCompaniesData();
        return view;
    }

    private void loadCompaniesData() {
        list = new ArrayList<>();
        list.add("Accenture");
        list.add("Infosys");
        list.add("Tech Mahindra");
        list.add("TCS");
        list.add("Cognizent");
        PlacementQuizAdapter placementQuizAdapter = new PlacementQuizAdapter(getActivity(), list);
        recyclerViewCompanies.setAdapter(placementQuizAdapter);
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
