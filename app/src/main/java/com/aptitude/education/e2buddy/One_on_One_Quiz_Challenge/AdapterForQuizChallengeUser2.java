package com.aptitude.education.e2buddy.One_on_One_Quiz_Challenge;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.aptitude.education.e2buddy.R;
import com.aptitude.education.e2buddy.ViewData.InsertAnswer;
import com.aptitude.education.e2buddy.ViewData.QuestionView1;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Matrix on 04-03-2019.
 */

public class AdapterForQuizChallengeUser2 extends PagerAdapter {
    private List<QuestionView1> questionViewList;
    Context context;
    String notification_id;
    String receiver_user_id;
    LayoutInflater layoutInflater;
    SharedPreferences sharedPreferences;
    String quizid, quizname, finalanswer, userid,quizdate;
    DatabaseReference databaseReference;
    DatabaseReference dataref;
    DatabaseReference reference;
    SharedPreferences sharedPreferences1;
    SharedPreferences.Editor editor1;
    Button btn_unfocus;

    public String[] qnum = {"1/10","2/10","3/10","4/10","5/10","6/10","7/10","8/10","9/10","10/10","10/10"};

    public AdapterForQuizChallengeUser2(List<QuestionView1> questionViewList, Context context, String notification_id) {
        this.questionViewList = questionViewList;
        this.context = context;
        this.notification_id = notification_id;

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

        final   String ans;


        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = layoutInflater.inflate(R.layout.que_layout_for_challenge, container, false);

        //  TextView questionid  = view.findViewById(R.id.questionid);
        TextView question = view.findViewById(R.id.question);
        //final RadioGroup radioGroup = view.findViewById(R.id.radiogrforquestion);
        final TextView selectedans = view.findViewById(R.id.selectedans);
        final  TextView quenunber = view.findViewById(R.id.questionnumber);

        final Button opt1 = view.findViewById(R.id.rdops1);
        final Button opt2 = view.findViewById(R.id.rdops2);
        final  Button opt3 = view.findViewById(R.id.rdops3);
        final Button opt4 = view.findViewById(R.id.rdops4);


        sharedPreferences = context.getSharedPreferences("quizpref", Context.MODE_PRIVATE);
        quizid = sharedPreferences.getString("quizid", "");
        quizname = sharedPreferences.getString("quizname", "");
        userid = sharedPreferences.getString("userid", "");
        quizdate = sharedPreferences.getString("quizdate","");


        sharedPreferences1 = context.getSharedPreferences("notificatios", Context.MODE_PRIVATE);

        receiver_user_id = sharedPreferences1.getString("receiver_id", "");

        databaseReference = FirebaseDatabase.getInstance().getReference("1V1-user_answer");

        dataref = FirebaseDatabase.getInstance().getReference();
        reference = FirebaseDatabase.getInstance().getReference();

        final QuestionView1 questionView = questionViewList.get(position);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy::HH:mm:ss");
        final String date = sdf.format(new Date());

        //questionid.setText("Question id : " +questionView.getQuestionid());
       quenunber.setText("Question : " +qnum[position]);

        question.setText(questionView.getQuestioname());
        opt1.setText(questionView.getOption1());
        opt2.setText(questionView.getOption2());
        opt3.setText(questionView.getOption3());
        opt4.setText(questionView.getOption4());

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
                            //         Toast.makeText(context, "answ " + ans, Toast.LENGTH_SHORT ).show();

                            insertAnswer(questionView.getQuestionid(),ans);

                            selectedans.setText("Your Selected Answer : "+ ans);

                            break;

                        case R.id.rdops2 :
                            setFocus(btn_unfocus, btn[1]);
                            ans = btn[1].getText().toString();
//                            Toast.makeText(context, "answ " + ans, Toast.LENGTH_SHORT ).show();
                            insertAnswer(questionView.getQuestionid(),ans);

                            selectedans.setText("Your Selected Answer : "+ ans);

                            break;

                        case R.id.rdops3 :
                            setFocus(btn_unfocus, btn[2]);
                            ans = btn[2].getText().toString();
                            //                          Toast.makeText(context, "answ " + ans, Toast.LENGTH_SHORT ).show();
                            insertAnswer(questionView.getQuestionid(),ans);

                            selectedans.setText("Your Selected Answer : "+ ans);

                            break;

                        case R.id.rdops4 :
                            setFocus(btn_unfocus, btn[3]);
                            ans = btn[3].getText().toString();
                            //                        Toast.makeText(context, "answ " + ans, Toast.LENGTH_SHORT ).show();
                            insertAnswer(questionView.getQuestionid(),ans);

                            selectedans.setText("Your Selected Answer : "+ ans);

                            break;
                    }
                }
            });
        }

        btn_unfocus = btn[0];
        container.addView(view);
        return view;
    }

    private void setFocus(Button btn_unfocus, Button btn_focus){
        btn_unfocus.setTextColor(Color.rgb(49, 50, 51));
        // btn_unfocus.setBackgroundColor(Color.rgb(207, 207, 207));
        btn_unfocus.setBackgroundResource(R.drawable.button);
        btn_focus.setTextColor(Color.rgb(255, 255, 255));
        // btn_focus.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
        btn_focus.setBackgroundResource(R.drawable.buttons);
        this.btn_unfocus = btn_focus;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

//        container.removeView((RelativeLayout) object);
    }

    private void insertAnswer(String questionid, String answer){


        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy::HH:mm:ss");
        final String date = sdf.format(new Date());

        InsertAnswer answers = new InsertAnswer(answer, date);

        databaseReference.child(notification_id).child(userid).child(questionid).setValue(answers);



    }





}
