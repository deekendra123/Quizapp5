package com.aptitude.education.e2buddy.Question;

import android.app.ActionBar;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.aptitude.education.e2buddy.DisplayAnswer.LeaderBoardData;
import com.aptitude.education.e2buddy.Intro.IntroActivity;
import com.aptitude.education.e2buddy.Intro.SessionManager;
import com.aptitude.education.e2buddy.Quiz_Tournament.Quiz_Tournament_Result_Activity;
import com.aptitude.education.e2buddy.Quiz_Tournament.TournamentQuizActivity;
import com.aptitude.education.e2buddy.Quiz_Tournament.Tournament_Participation_Dialog;
import com.aptitude.education.e2buddy.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Transformation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DailyDietFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    RecyclerView quizRecyclerView;
    FirebaseAuth auth;
    FirebaseAuth.AuthStateListener listener;
    ProgressDialog dialog;
    List<LeaderBoardData> list;
    String student_name,eid,date,imageUrl,student_email, tournament_quiz_date, newdate, tournament_date;
    Transformation transformation;
    DatabaseReference databaseReference;
    TextView quizHistory;
    AppCompatButton btParticipate;
    TextView tvTaournament, tvdate,tvQuizStatus, textwin,textpoints, tvpoints,seeTournamentResult, tvRegistered;
    private DailyDietFragment.OnFragmentInteractionListener mListener;

    SessionManager sessionManager;
    private RecyclerView recyclerViewWord;
    ImageView imgArrowRight,imgArrorL;

    String[] tournamentDates = {

            "20-10-2019", "23-10-2019", "27-10-2019",
            "30-10-2019", "03-11-2019", "06-11-2019",
            "10-11-2019", "13-11-2019", "17-11-2019",
            "20-11-2019", "24-11-2019", "27-11-2019",
            "01-12-2019", "04-12-2019", "08-12-2019",
            "11-12-2019", "15-12-2019", "18-12-2019",
            "22-12-2019", "25-12-2019", "29-12-2019",
            "01-01-2020", "05-01-2020", "08-01-2020",
            "12-01-2020", "15-01-2020", "19-01-2020",
            "22-01-2020", "26-01-2020", "29-01-2020",
            "02-02-2020", "05-02-2020", "09-02-2020",
            "12-02-2020", "16-02-2020", "19-02-2020",
            "23-02-2020", "26-02-2020", "01-03-2020",
            "04-03-2020", "08-03-2020", "11-03-2020",
            "15-03-2020", "18-03-2020", "22-03-2020",
            "25-03-2020", "29-03-2020", "01-04-2020",
            "05-04-2020", "08-04-2020", "12-04-2020",
            "15-04-2020", "19-04-2020", "22-04-2020",
            "26-04-2020", "29-04-2020", "03-05-2020",
            "06-05-2020", "10-05-2020", "13-05-2020",
            "17-05-2020", "20-05-2020", "24-05-2020",
            "27-05-2020", "31-05-2020"
    };
    DailyQuizes dailyQuizes;

    public DailyDietFragment() {
        // Required empty public constructor
    }
    // TODO: Rename and change types and number of parameters
    public static DailyDietFragment newInstance(String param1, String param2) {
        DailyDietFragment fragment = new DailyDietFragment();
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
        View view = inflater.inflate(R.layout.fragment_daily_diet, container, false);
        tvTaournament = view.findViewById(R.id.tvTaournament);
        btParticipate = view.findViewById(R.id.btparticipate);
        tvdate = view.findViewById(R.id.tvTournamentDate);
        tvQuizStatus = view.findViewById(R.id.tvQuizStatus);
        textwin = view.findViewById(R.id.textwin);
        quizRecyclerView = view.findViewById(R.id.prerecycler);
        textpoints = view.findViewById(R.id.textpoints);
        tvpoints = view.findViewById(R.id.points);
          seeTournamentResult = view.findViewById(R.id.seeTournamentResult);
        tvRegistered = view.findViewById(R.id.tvRegistered);
        quizHistory = view.findViewById(R.id.quizhistory);
        recyclerViewWord = view.findViewById(R.id.recyclerViewWord);
         imgArrowRight = view.findViewById(R.id.imgArrorR);
        imgArrorL = view.findViewById(R.id.imgArrorL);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        recyclerViewWord.setLayoutManager(layoutManager);

        sessionManager = new SessionManager(getActivity());

        databaseReference = FirebaseDatabase.getInstance().getReference();

        dialog = new ProgressDialog(getActivity(),R.style.full_screen_dialog){
            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.full_screen_progress_dialog);
                getWindow().setLayout(WindowManager.LayoutParams.FILL_PARENT,
                        ActionBar.LayoutParams.FILL_PARENT);
            }
        };

        dialog.setCancelable(false);
        dialog.show();

        getActivity().getFragmentManager().popBackStack();

        transformation = new RoundedTransformationBuilder()
                .borderColor(getResources().getColor(R.color.white))
                .borderWidthDp(0)
                .cornerRadiusDp(50)
                .oval(false)
                .build();

        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        quizRecyclerView.setLayoutManager(layoutManager1);
        dailyQuizes = new DailyQuizes(getActivity(), quizRecyclerView);
        dailyQuizes.getQuiz();
        list = new ArrayList<>();

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

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM");
        date = sdf.format(new Date());

        SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
        tournament_date = sdf1.format(new Date());

        try {
            tournamentQuiz();
            checkTournamentRegisteredStudents();
            checkTournamentResultDates();
            getRegisteredStudent();
            getWordoftheDay1();

        } catch (Exception e){
            e.printStackTrace();
        }

        databaseReference.child("user_info").child(eid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {

                    student_name = dataSnapshot.child("student_name").getValue().toString();
                    student_email = dataSnapshot.child("email").getValue().toString();
                    imageUrl = dataSnapshot.child("image_Url").getValue(String.class);
                    sessionManager.createStudentData(eid,student_name,student_email,imageUrl,date,tournament_quiz_date);
                    dialog.dismiss();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        listener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Intent intent = new Intent(getActivity(), IntroActivity.class);
                    startActivity(intent);
                }
            }
        };


        quizHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), QuizHistoryActivity.class);
                startActivity(intent);

            }
        });

        recyclerViewWord.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                imgArrorL.setVisibility(View.VISIBLE);
                imgArrowRight.setVisibility(View.VISIBLE);
                return false;
            }
        });


        imgArrowRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imgArrorL.setVisibility(View.VISIBLE);
                v.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.ripple));

                recyclerViewWord.smoothScrollToPosition(layoutManager.findLastVisibleItemPosition() + 1);

                if (layoutManager.findLastVisibleItemPosition()==3){
                    imgArrowRight.setVisibility(View.INVISIBLE);
                }

            }
        });


            imgArrorL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imgArrowRight.setVisibility(View.VISIBLE);

                    v.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.ripple));

                    if (layoutManager.findFirstVisibleItemPosition() > 1) {
                        recyclerViewWord.smoothScrollToPosition(layoutManager.findFirstVisibleItemPosition() - 1);
                    } else {
                        imgArrorL.setVisibility(View.INVISIBLE);
                        recyclerViewWord.smoothScrollToPosition(0);
                    }

                }
            });



       return view;

    }


    private void getWordoftheDay1(){

        WordoftheDay wordoftheDay = new WordoftheDay(getActivity(), recyclerViewWord);
        wordoftheDay.getWordoftheDay();
    }
    private void getRegisteredStudent() {
        databaseReference.child("tournament_user_numbers").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    int registeredUsers = Integer.parseInt(dataSnapshot.child("total_users").getValue().toString());
                    tvRegistered.setText(registeredUsers+" Players");
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void tournamentQuiz() {
        for (int i =0; i<tournamentDates.length;i++){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

            try {
                if (simpleDateFormat.parse(tournamentDates[i]).after(simpleDateFormat.parse(tournament_date))){
                    tournament_quiz_date = tournamentDates[i];
                    break;
                }else  if (simpleDateFormat.parse(tournament_date).equals(simpleDateFormat.parse(tournamentDates[i]))){
                    tournament_quiz_date = tournamentDates[i];
                    break;
                }

            }catch (Exception e){
                e.printStackTrace();
            }

        }

        newdate = tournament_quiz_date.replaceAll("-01-2020", "-Jan")
                .replaceAll("-02-2020", "-Feb")
                .replaceAll("-03-2020", "-Mar")
                .replaceAll("-04-2020", "-Apr")
                .replaceAll("-05-2020", "-May")
                .replaceAll("-06-2020", "-Jun")
                .replaceAll("-07-2020", "-Jul")
                .replaceAll("-08-2020", "-Aug")
                .replaceAll("-09-2020", "-Sep")
                .replaceAll("-10-2020", "-Oct")
                .replaceAll("-11-2020", "-Nov")
                .replaceAll("-12-2020", "-Dec");

    }

    private void checkTournamentResultDates(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Calendar c = Calendar.getInstance();
        try {

            if (!sdf.parse(tournament_date).equals(sdf.parse(tournament_quiz_date))){
                c.setTime(sdf.parse(tournament_quiz_date));
                seeTournamentResult.setVisibility(View.VISIBLE);
                seeTournamentResult.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getActivity(), Quiz_Tournament_Result_Activity.class);
                        startActivity(i);
                    }
                });

            }



        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    private void checkTournamentRegisteredStudents(){

        databaseReference.child("tournament_register_students").child(eid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {

                            if (dataSnapshot.hasChild(tournament_quiz_date)){
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    String tournamentDate = dataSnapshot1.getKey();

                                    tvdate.setVisibility(View.GONE);
                                    btParticipate.setVisibility(View.GONE);
                                    tvQuizStatus.setVisibility(View.VISIBLE);

                                    tvQuizStatus.setText("Live at 12:00 AM on " +newdate);


                                    if (tournament_quiz_date.equals(tournament_date)) {

                                        databaseReference.child("tournament_register_students").child(eid).child(tournament_quiz_date).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {


                                                boolean playStatus = (boolean) dataSnapshot.child("play_status").getValue();

                                                if (playStatus == false){
                                                    btParticipate.setVisibility(View.GONE);
                                                    tvdate.setVisibility(View.VISIBLE);
                                                    tvQuizStatus.setVisibility(View.VISIBLE);
                                                    tvdate.setText(newdate);
                                                    tvQuizStatus.setText("Tournament is Live.  Play Now.");

                                                    tvQuizStatus.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {

                                                            Intent intent = new Intent(getActivity(), TournamentQuizActivity.class);
                                                            startActivity(intent);
                                                        }
                                                    });
                                                }
                                                else {
                                                    tvQuizStatus.setVisibility(View.VISIBLE);
                                                    tvdate.setVisibility(View.VISIBLE);
                                                    tvdate.setText(newdate);
                                                    tvQuizStatus.setText("Result will be declared Tomorrow");
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });




                                    }
                                }
                            }
                            else {
                                btParticipate.setVisibility(View.VISIBLE);
                                tvTaournament.setText("Open Tournament");
                                tvdate.setText(newdate);
                                btParticipate.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        FragmentManager fm = getActivity().getFragmentManager();
                                        DialogFragment dialog = Tournament_Participation_Dialog.newInstance();
                                        dialog.show(fm,"tag");

                                    }
                                });
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }



    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(listener);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

}
