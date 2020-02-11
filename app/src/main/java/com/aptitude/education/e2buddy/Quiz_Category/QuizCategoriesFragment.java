package com.aptitude.education.e2buddy.Quiz_Category;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aptitude.education.e2buddy.R;
import com.aptitude.education.e2buddy.ViewData.CategoryData;
import com.aptitude.education.e2buddy.ViewData.CategoryData1;
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
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

public class QuizCategoriesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    int quiz_id;
    private List<CategoryData> categoryDataList2;
    private List<CategoryData1> categoryDataList;
    private RecyclerView recyclerViewCategory,recyclerViewCategory2;
    private static ViewPager mPager;
    private static int currentPage = 0;
    private DatabaseReference databaseReference;
    private ImageView imgCategory1, imgCategory2;
    private CircleIndicator indicator;
    private List<CategoryData> list;

    String[] categorySlider = {"Space", "Technology", "Taglines", "What is it called"};
    private static final String[] category_slider_Image= {
            "https://firebasestorage.googleapis.com/v0/b/e2buddy.appspot.com/o/category_image%2Fspace.jpg?alt=media&token=45724e64-0223-49b9-b095-7de5d07effb3"
            ,"https://firebasestorage.googleapis.com/v0/b/e2buddy.appspot.com/o/category_image%2Ftech.jpg?alt=media&token=636f0b2a-d4d5-40b3-83e6-d0214eddc35b"
            ,"https://firebasestorage.googleapis.com/v0/b/e2buddy.appspot.com/o/category_image%2Ftagline.jpg?alt=media&token=2ef1d709-df25-420b-867c-910336d72df4"
            ,"https://firebasestorage.googleapis.com/v0/b/e2buddy.appspot.com/o/category_image%2Fwhatisit.jpg?alt=media&token=d2b87845-9788-483f-95ce-9858a464735d"
    };

    public QuizCategoriesFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static QuizCategoriesFragment newInstance(String param1, String param2) {
        QuizCategoriesFragment fragment = new QuizCategoriesFragment();
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
        View view = inflater.inflate(R.layout.fragment_quiz_categories, container, false);
        recyclerViewCategory = view.findViewById(R.id.recyclerViewCategory);
        recyclerViewCategory.setHasFixedSize(true);
        recyclerViewCategory2 = view.findViewById(R.id.recyclerViewCategory2);
        recyclerViewCategory2.setHasFixedSize(true);
        imgCategory1 = view.findViewById(R.id.imgCategory1);
        imgCategory2 = view.findViewById(R.id.imgCategory2);
        mPager = view.findViewById(R.id.pager);
        indicator = view.findViewById(R.id.indicator);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        RecyclerView.LayoutManager layoutManager3 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewCategory2.setLayoutManager(layoutManager3);
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewCategory.setLayoutManager(layoutManager2);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getBollywoodCategory();
        getCricketCategory();
        getWorldRecordsCategory();
        getWildLifeCategory();
        loadCategoryImages(view);

    }

    private void getWildLifeCategory() {



        Picasso.get()
                .load("https://firebasestorage.googleapis.com/v0/b/e2buddy.appspot.com/o/category_image%2Fwild.jpeg?alt=media&token=6e006195-787c-46b5-b878-9f92b714fd76").into(imgCategory1);

        imgCategory1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child("category_questions").child("Wildlife").child("Wildlife").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        long count =  dataSnapshot.getChildrenCount();
                        if (count>1){
                            showAlertDialog("Wildlife","Wildlife");
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });


    }

    private void getWorldRecordsCategory() {



        imgCategory2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                databaseReference.child("category_questions").child("World Records").child("World Records").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        long count =  dataSnapshot.getChildrenCount();
                        if (count>1){
                            showAlertDialog("World Records","World Records");
                        }
                        else {
                            databaseReference.child("category_questions").child("World Records").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    String parent_category = dataSnapshot.getKey();

                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                                        String child_category = dataSnapshot1.getKey();

                                        for (DataSnapshot dataSnapshot11 : dataSnapshot1.getChildren()){

                                            quiz_id = Integer.parseInt(dataSnapshot11.getKey());

                                            System.gc();

                                            System.gc();
                                            Intent intent = new Intent(getActivity(), StartCategoryQuizActivity.class);
                                            intent.putExtra("parent_category",parent_category);
                                            intent.putExtra("child_category", child_category);
                                            intent.putExtra("quiz_id", quiz_id);
                                            getActivity().startActivity(intent);
                                        }

                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });




            }
        });
    }

    private void getBollywoodCategory(){

        categoryDataList = new ArrayList<>();

        databaseReference.child("category_questions").child("Bollywood").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String parent_category = dataSnapshot.getKey();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                    String child_category = dataSnapshot1.getKey();

                    for (DataSnapshot dataSnapshot11 : dataSnapshot1.getChildren()){

                        quiz_id = Integer.parseInt(dataSnapshot11.getKey());

                    }
                    categoryDataList.add(new CategoryData1(parent_category,child_category, quiz_id));
                    SubCategoryQuizAdapter1 quizCategoryAdapter = new SubCategoryQuizAdapter1(getActivity(), categoryDataList);
                    recyclerViewCategory.setAdapter(quizCategoryAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getCricketCategory(){

        categoryDataList2 = new ArrayList<>();
        databaseReference.child("category_questions").child("Cricket").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String parent_category = dataSnapshot.getKey();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                    String child_category = dataSnapshot1.getKey();

                    for (DataSnapshot dataSnapshot11 : dataSnapshot1.getChildren()){

                        quiz_id = Integer.parseInt(dataSnapshot11.getKey());

                    }
                    categoryDataList2.add(new CategoryData(parent_category,child_category, quiz_id));
                    SubCategoryQuizAdapter quizCategoryAdapter = new SubCategoryQuizAdapter(getActivity(), categoryDataList2);
                    recyclerViewCategory2.setAdapter(quizCategoryAdapter);
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


    private void loadCategoryImages(View view) {
        list = new ArrayList<>();
        for(int i=0;i<categorySlider.length;i++) {
            final int finalI = i;
            databaseReference.child("category_questions").child(categorySlider[i]).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    categoryDataList.clear();

                    String parent_category = dataSnapshot.getKey();

                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                        String child_category = dataSnapshot1.getKey();

                        for (DataSnapshot dataSnapshot11 : dataSnapshot1.getChildren()){

                            quiz_id = Integer.parseInt(dataSnapshot11.getKey());

                        }
                        if (getActivity()!=null){
                            list.add(new CategoryData(parent_category,child_category, category_slider_Image[finalI], quiz_id));
                            mPager.setAdapter(new CategoryImageAdapter(getActivity(),list));
                            indicator.setViewPager(mPager);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }


        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == categorySlider.length) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 10000, 6000);

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

    public void showAlertDialog(final String parent_category, final String child_category) {

        final List<CategoryData> categoryDataList;
        Transformation transformation;
        View alertLayout = LayoutInflater.from(getActivity()).inflate(R.layout.subcategory_layout_custom_dialog, null);
        final RecyclerView recyclerViewSubCategory = alertLayout.findViewById(R.id.recyclerViewSubCategory);
        final ImageView childCategoryImage =  alertLayout.findViewById(R.id.childCategoryImage);
        final TextView childCategoryName =  alertLayout.findViewById(R.id.childCategoryName);

        categoryDataList = new ArrayList<>();

        transformation = new RoundedTransformationBuilder()
                .borderColor(getActivity().getResources().getColor(R.color.gray))
                .borderWidthDp(0)
                .cornerRadiusDp(50)
                .oval(false)
                .build();


        childCategoryName.setText(""+child_category);



        if (child_category.equals("Wildlife")){

            Picasso.get()
                    .load("https://firebasestorage.googleapis.com/v0/b/e2buddy.appspot.com/o/category_image%2Fwild.jpeg?alt=media&token=6e006195-787c-46b5-b878-9f92b714fd76")
                    .placeholder(R.drawable.userimg)
                    .fit()
                    .transform(transformation)
                    .into(childCategoryImage);
        }


        RecyclerView.LayoutManager layoutManager3 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewSubCategory.setLayoutManager(layoutManager3);


        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity(), R.style.CustomDialogTheme);

        alert.setView(alertLayout);

        final AlertDialog dialog = alert.create();

        databaseReference.child("category_questions").child(parent_category).child(child_category).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    int quiz_id = Integer.parseInt(dataSnapshot1.getKey());
                    Log.e("dk ", String.valueOf(quiz_id));
                    categoryDataList.add(new CategoryData(parent_category, child_category, quiz_id));

                }
                CategoryQuizAdapter categoryQuizAdapter = new CategoryQuizAdapter(getActivity(), categoryDataList,dialog);
                recyclerViewSubCategory.setAdapter(categoryQuizAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        dialog.show();
    }

}
