package com.aptitude.education.e2buddy.Placement_Papers;

import android.content.Context;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.aptitude.education.e2buddy.R;
import com.aptitude.education.e2buddy.ViewData.InsertAnswer;
import com.aptitude.education.e2buddy.ViewData.QuestionView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Matrix on 12-02-2019.
 */

public class AdapterForPlacementQuizQuestion extends PagerAdapter {

    private List<QuestionView> questionViewList;
    Context context;
    LayoutInflater layoutInflater;
    String userid;
    DatabaseReference databaseReference;
    Button btn_unfocus;
    public String[] qnum = {"1/10","2/10","3/10","4/10","5/10","6/10","7/10"};
    boolean clicked=false;


    public AdapterForPlacementQuizQuestion(List<QuestionView> questionViewList, Context context, String userid) {
        this.questionViewList = questionViewList;
        this.context = context;
        this.userid = userid;
    }
    @Override
    public int getCount() {
        return questionViewList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {


        final Button[] btn = new Button[4];
        int[] btn_id = {R.id.rdops1, R.id.rdops2, R.id.rdops3, R.id.rdops4};

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = layoutInflater.inflate(R.layout.que_layout_for_placement, container, false);
        TextView question = view.findViewById(R.id.question);
        final  TextView quenunber = view.findViewById(R.id.questionnumber);
        final ImageView questionBookmark = view.findViewById(R.id.questionBookmark);
        final Button opt1 = view.findViewById(R.id.rdops1);
        final Button opt2 = view.findViewById(R.id.rdops2);
        final  Button opt3 = view.findViewById(R.id.rdops3);
        final Button opt4 = view.findViewById(R.id.rdops4);

        databaseReference = FirebaseDatabase.getInstance().getReference();



        final QuestionView questionView = questionViewList.get(position);


        for (int i=0;i<qnum.length;i++) {

            quenunber.setText("" + qnum[position]);
            question.setText(questionView.getQuestioname());
            opt1.setText(questionView.getOption1());
            opt2.setText(questionView.getOption2());
            opt3.setText(questionView.getOption3());
            opt4.setText(questionView.getOption4());


            questionBookmark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    questionBookmark.setBackgroundResource(R.drawable.ic_bookmark1);

                    databaseReference.child("placemenPaper_user_answer").child(userid).child("quantitativeAbility").child("quiz1").child(questionView.getQuestionid()).child("bookmark").setValue(true);


                }
            });

      //      checkBookmarks(questionView.getQuestionid(), questionBookmark);


        }



        btn_unfocus = btn[0];

        for(int i = 0; i < btn.length; i++){
            btn[i] = view.findViewById(btn_id[i]);
            btn[i].setBackgroundColor(Color.rgb(207, 207, 207));
            btn[i].setBackgroundResource(R.drawable.button);

            btn[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String ans;
                    switch (v.getId()){
                        case R.id.rdops1 :
                            setFocus(btn_unfocus, btn[0]);
                            ans = btn[0].getText().toString();
                            insertAnswer(questionView.getQuestionid(),ans);
                            break;

                        case R.id.rdops2 :
                            setFocus(btn_unfocus, btn[1]);
                            ans = btn[1].getText().toString();
                            insertAnswer(questionView.getQuestionid(),ans);
                            break;

                        case R.id.rdops3 :
                            setFocus(btn_unfocus, btn[2]);
                            ans = btn[2].getText().toString();
                            insertAnswer(questionView.getQuestionid(),ans);
                            break;

                        case R.id.rdops4 :
                            setFocus(btn_unfocus, btn[3]);
                            ans = btn[3].getText().toString();
                            insertAnswer(questionView.getQuestionid(),ans);
                            break;
                    }
                }
            });
        }

        btn_unfocus = btn[0];



        container.addView(view);

        return view;
    }

    private void checkBookmarks(String questionId, ImageView questionBookmark){

        databaseReference.child("placemenPaper_user_answer").child(userid).child("quantitativeAbility").child("quiz1").child(questionId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean bookmark_status = (boolean) dataSnapshot.child("bookmark").getValue();
                if (!bookmark_status){
                    questionBookmark.setBackgroundResource(R.drawable.ic_bookmark1);
                    questionBookmark.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.e("deekestatus", String.valueOf(bookmark_status));

                            questionBookmark.setBackgroundResource(R.drawable.ic_bookmark2);

                            databaseReference.child("placemenPaper_user_answer").child(userid).child("quantitativeAbility").child("quiz1").child(questionId).child("bookmark").setValue(false);


                        }
                    });

                }
                else {
                    questionBookmark.setBackgroundResource(R.drawable.ic_bookmark2);

                    questionBookmark.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.e("deekestatus", String.valueOf(bookmark_status));

                            questionBookmark.setBackgroundResource(R.drawable.ic_bookmark1);

                            databaseReference.child("placemenPaper_user_answer").child(userid).child("quantitativeAbility").child("quiz1").child(questionId).child("bookmark").setValue(true);

                        }
                    });

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void setFocus(TextView btn_unfocus, Button btn_focus){
//        btn_unfocus.setTextColor(Color.rgb(211, 211, 211));
        btn_unfocus.setTextColor(Color.rgb(49, 50, 51));
        btn_unfocus.setBackgroundResource(R.drawable.button);
        btn_focus.setTextColor(Color.rgb(255, 255, 255));
        btn_focus.setBackgroundResource(R.drawable.buttons);
        this.btn_unfocus = btn_focus;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

    }

    private void insertAnswer(String questionid, String answer){

       databaseReference.child("placemenPaper_user_answer").child(userid).child("quantitativeAbility").child("quiz1").child(questionid).child("answer").setValue(answer);


    }



}
