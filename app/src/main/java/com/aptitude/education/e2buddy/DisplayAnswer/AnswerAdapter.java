package com.aptitude.education.e2buddy.DisplayAnswer;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.aptitude.education.e2buddy.R;
import com.aptitude.education.e2buddy.ViewData.AnswerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matrix on 26-12-2018.
 */

public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.AnswerHolder>{

    private Context mCtx;
    private List<AnswerView> answerViewList;

    private final static int FADE_DURATION = 1000;
    public AnswerAdapter(Context mCtx, List<AnswerView> answerViewList) {
        this.mCtx = mCtx;
        this.answerViewList = answerViewList;
    }


    @NonNull
    @Override
    public AnswerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.answer_list_item, parent, false);
        return new AnswerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnswerHolder holder, int position) {

        AnswerView answerView = answerViewList.get(position);
        holder.textView.setText(answerView.getQuestion());


        holder.corr_ans.setText(""+answerView.getCorrectAnswer());
        holder.user_ans.setText( ""+answerView.getUserAnswer());

        holder.img1.setBackgroundResource(R.drawable.ic_right1);

        if (answerView.getCorrectAnswer().equals(answerView.getUserAnswer())){
            holder.img2.setBackgroundResource(R.drawable.ic_right1);
        }

        else {
            holder.img2.setBackgroundResource(R.drawable.ic_delete_cross);

        }


    }

    @Override
    public int getItemCount() {
        return answerViewList.size();
    }

    public class AnswerHolder extends RecyclerView.ViewHolder {

        TextView textView, user_ans, corr_ans,tv1,tv2;
        CardView cardView;
        ImageView img1, img2;
        //ImageView right, wrong;

        public AnswerHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.answer);
            user_ans = itemView.findViewById(R.id.user_ans);
            corr_ans = itemView.findViewById(R.id.corr_ans);
            cardView = itemView.findViewById(R.id.card);
            img1 = itemView.findViewById(R.id.img3);
            img2 = itemView.findViewById(R.id.img4);

            Typeface type = (Typeface) Typeface.createFromAsset(mCtx.getAssets(), "fonts/Roboto-Regular.ttf");
            textView.setTypeface(type);
            user_ans.setTypeface(type);
            corr_ans.setTypeface(type);

        }
    }


}
