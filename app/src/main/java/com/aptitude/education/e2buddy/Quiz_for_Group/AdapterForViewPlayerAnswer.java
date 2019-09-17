package com.aptitude.education.e2buddy.Quiz_for_Group;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import com.aptitude.education.e2buddy.R;
import com.aptitude.education.e2buddy.ViewData.AnswerViewForGroup;

import java.util.List;

public class AdapterForViewPlayerAnswer extends RecyclerView.Adapter<AdapterForViewPlayerAnswer.AnswerHolder> {
    Context mCtx;
   List<AnswerViewForGroup> list;


    private final static int FADE_DURATION = 1000;

    public AdapterForViewPlayerAnswer(Context mCtx, List<AnswerViewForGroup> list) {
        this.mCtx = mCtx;
        this.list = list;
    }


    @NonNull
    @Override
    public AnswerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.answer_list_item_for_group_player, parent, false);
        return new AnswerHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull AnswerHolder holder, int position) {
        AnswerViewForGroup answerView = list.get(position);
        holder.textView.setText(answerView.getQuestion());


        holder.corr_ans.setText("Correct Answer : " +answerView.getCorrectAnswer());
        holder.user_ans.setText( "Your Answer : "+answerView.getUserAnswer());



        if (answerView.getCorrectAnswer().equals(answerView.getUserAnswer())){
            holder.cardView.setBackgroundResource(R.drawable.bgcard);
        }

        else {
            holder.cardView.setBackgroundResource(R.drawable.bgcardred);

        }

        setFadeAnimation(holder.itemView);
        setScaleAnimation(holder.itemView);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class AnswerHolder extends RecyclerView.ViewHolder {
        TextView textView, user_ans, corr_ans;
        CardView cardView;

        public AnswerHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.answer);
            user_ans = itemView.findViewById(R.id.user_ans);
            corr_ans = itemView.findViewById(R.id.corr_ans);
            cardView = itemView.findViewById(R.id.card);

        }
    }
    private void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(FADE_DURATION);
        view.startAnimation(anim);
    }

    private void setScaleAnimation(View view) {
        ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(FADE_DURATION);
        view.startAnimation(anim);
    }

}
