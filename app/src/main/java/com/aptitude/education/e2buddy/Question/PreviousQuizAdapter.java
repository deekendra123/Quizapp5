package com.aptitude.education.e2buddy.Question;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.aptitude.education.e2buddy.AdMob.AdManager;
import com.aptitude.education.e2buddy.R;
import com.aptitude.education.e2buddy.ViewData.PreviousQuizView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Matrix on 28-01-2019.
 */

public class PreviousQuizAdapter extends RecyclerView.Adapter<PreviousQuizAdapter.QuizHolder> {

    Context mCtx;
    List<PreviousQuizView> previousQuizViewList;

    String newdate;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    OnItemClickListener listener;
    SharedPreferences sharedPreferences1;
    String userid,student_name;



    public PreviousQuizAdapter(Context mCtx, List<PreviousQuizView> previousQuizViewList) {
        this.mCtx = mCtx;
        this.previousQuizViewList = previousQuizViewList;
    }


    public interface OnItemClickListener
    {
        void onItemClick(View itemView, int position);

    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }



    @NonNull
    @Override
    public QuizHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mCtx).inflate(R.layout.pre_quiz_item, parent, false);
        return new QuizHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizHolder holder, int position) {
        PreviousQuizView previousQuizView = previousQuizViewList.get(position);

        sharedPreferences = mCtx.getSharedPreferences("quizpref", MODE_PRIVATE);
        userid = sharedPreferences.getString("userid", "");


        newdate = previousQuizView.getQuizdate().replaceAll("-01", "-Jan")
                .replaceAll("-02", "-Feb")
                .replaceAll("-03", "-Mar")
                .replaceAll("-04", "-Apr")
                .replaceAll("-05", "-May")
                .replaceAll("-06", "-Jun")
                .replaceAll("-07", "-Jul")
                .replaceAll("-08", "-Aug")
                .replaceAll("-09", "-Sep")
                .replaceAll("-10", "-Oct")
                .replaceAll("-11", "-Nov")
                .replaceAll("-12", "-Dec");


        holder.qdate.setText(newdate+"-2019");
        //    holder.qid.setText("Quiz "+ previousQuizView.getQuizid());

        final String id = previousQuizViewList.get(position).getQuizid();
        final String date = previousQuizViewList.get(position).getQuizdate();

        sharedPreferences = mCtx.getSharedPreferences("quizpref", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        holder.itemView.setClickable(false);

        final AlphaAnimation buttonClick = new AlphaAnimation(1F, 1F);

        holder.playquiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

                databaseReference.child("daily_user_credit").child(userid).child(date).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {
                            long count = dataSnapshot.getChildrenCount();
                            //  Toast.makeText(mCtx, ""+ count,Toast.LENGTH_SHORT).show();
                            if (count<3){

                                previousQuizViewList.clear();
                                Intent intent = new Intent(mCtx, StartQuizActivity.class);
                                intent.putExtra("quiz_date", date);
                                mCtx.startActivity(intent);

                            }else {
                                Toast.makeText(mCtx, "You have already attemped 3 times", Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                view.startAnimation(buttonClick);

            }
        });


    }

    @Override
    public int getItemCount() {


        return previousQuizViewList.size() ;

    }

    public class QuizHolder extends RecyclerView.ViewHolder {

        TextView qid, qdate, textView;
        Button playquiz;
        public QuizHolder(final View itemView) {
            super(itemView);

            //   qid = itemView.findViewById(R.id.qid);
            qdate = itemView.findViewById(R.id.qdate);
            textView = itemView.findViewById(R.id.qdates);
            playquiz = itemView.findViewById(R.id.playquiz);

            Typeface type = Typeface.createFromAsset(mCtx.getAssets(), "fonts/Roboto-Regular.ttf");
            //qid.setTypeface(type);

            qdate.setTypeface(type);
            playquiz.setTypeface(type);
            textView.setTypeface(type);

        }
    }
}
