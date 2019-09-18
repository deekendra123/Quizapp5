package com.aptitude.education.e2buddy.Question;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.aptitude.education.e2buddy.R;
import com.aptitude.education.e2buddy.ViewData.InsertAnswer;
import com.aptitude.education.e2buddy.ViewData.QuestionView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.Quizholder> {

    Context mCtx;
    List<QuestionView> list;
    String value,userid,quizdate;
    DatabaseReference databaseReference;
    String ans;
    Button btn_unfocus;
    public String[] qnum = {"1/10","2/10","3/10","4/10","5/10","6/10","7/10","8/10","9/10","10/10"};
    final Button[] btn = new Button[4];

    public QuizAdapter(Context mCtx, List<QuestionView> list, String value, String userid, String quizdate) {
        this.mCtx = mCtx;
        this.list = list;
        this.value = value;
        this.userid = userid;
        this.quizdate = quizdate;
    }


    @NonNull
    @Override
    public Quizholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.que_layout,parent,false);
        return new Quizholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Quizholder holder, int position) {


        databaseReference = FirebaseDatabase.getInstance().getReference("daily_user_answer");

        final QuestionView questionView = list.get(position);



        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy::HH:mm:ss");
        final String date = sdf.format(new Date());

        //questionid.setText("Question id : " +questionView.getQuestionid());



        holder.quenunber.setText("Question : " + qnum[position]);
        holder.question.setText(questionView.getQuestioname());
        holder.opt1.setText(questionView.getOption1());
        holder.opt2.setText(questionView.getOption2());
        holder.opt3.setText(questionView.getOption3());
        holder.opt4.setText(questionView.getOption4());

        btn_unfocus = btn[0];

        for (int i = 0; i < btn.length; i++) {

            if(i == 0)
            {
                btn[i]=holder.bt1;
            }else if(i == 1)
            {
                btn[i]=holder.bt2;
            }else if(i == 2)
            {
                btn[i]= holder.bt3;
            }else if(i == 3)
            {
                btn[1]=holder.bt4;
            }
            btn[i].setBackgroundColor(Color.rgb(207, 207, 207));
            btn[i].setBackgroundResource(R.drawable.button);

            btn[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String ans;
                    switch (v.getId()) {
                        case R.id.rdops1:
                            setFocus(btn_unfocus, btn[0]);
                            ans = btn[0].getText().toString();
                            insertAnswer(questionView.getQuestionid(), ans);
                            break;

                        case R.id.rdops2:
                            setFocus(btn_unfocus, btn[1]);
                            ans = btn[1].getText().toString();
                            insertAnswer(questionView.getQuestionid(), ans);
                            break;

                        case R.id.rdops3:
                            setFocus(btn_unfocus, btn[2]);
                            ans = btn[2].getText().toString();
                            insertAnswer(questionView.getQuestionid(), ans);
                            break;

                        case R.id.rdops4:
                            setFocus(btn_unfocus, btn[3]);
                            ans = btn[3].getText().toString();
                            insertAnswer(questionView.getQuestionid(), ans);
                            break;
                    }
                }
            });
        }

        btn_unfocus = btn[0];
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Quizholder extends RecyclerView.ViewHolder {



        TextView question,selectedans,quenunber;
        Button opt1,opt2,opt3,opt4;
        Button bt1,bt2,bt3,bt4;

        public Quizholder(View view) {
            super(view);

            bt1=view.findViewById(R.id.rdops1);
            bt2=view.findViewById(R.id.rdops2);
            bt3=view.findViewById(R.id.rdops3);
            bt4=view.findViewById(R.id.rdops4);

             question = view.findViewById(R.id.question);
            selectedans = view.findViewById(R.id.selectedans);
            quenunber = view.findViewById(R.id.questionnumber);

            opt1 = view.findViewById(R.id.rdops1);
            opt2 = view.findViewById(R.id.rdops2);
            opt3 = view.findViewById(R.id.rdops3);
            opt4 = view.findViewById(R.id.rdops4);
        }
    }

    private void insertAnswer(String questionid, String answer){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy::HH:mm:ss");
        final String date = sdf.format(new Date());

        InsertAnswer answers = new InsertAnswer(answer, date);
        databaseReference.child(userid).child(quizdate).child(value).child(questionid).setValue(answers);


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



}
