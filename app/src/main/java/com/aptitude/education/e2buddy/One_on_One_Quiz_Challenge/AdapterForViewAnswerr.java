package com.aptitude.education.e2buddy.One_on_One_Quiz_Challenge;

import android.content.Context;
import android.graphics.Typeface;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.aptitude.education.e2buddy.R;
import com.aptitude.education.e2buddy.ViewData.AnswerViewForOneOnOne;

import java.util.List;

public class AdapterForViewAnswerr extends RecyclerView.Adapter<AdapterForViewAnswerr.AnswerHolder> {
    Context mCtx;
   List<AnswerViewForOneOnOne> list;


    private final static int FADE_DURATION = 1000;

    public AdapterForViewAnswerr(Context mCtx, List<AnswerViewForOneOnOne> list) {
        this.mCtx = mCtx;
        this.list = list;
    }


    @NonNull
    @Override
    public AnswerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.answer_list_item, parent, false);
        return new AnswerHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull AnswerHolder holder, int position) {
        AnswerViewForOneOnOne answerView = list.get(position);
        holder.textView.setText(answerView.getQuestion());


        holder.corr_ans.setText("Correct Answer : " +answerView.getCorrectAnswer());
        holder.user_ans.setText( "Your Answer : "+answerView.getUserAnswer());


        holder.img1.setBackgroundResource(R.drawable.ic_right1);

        if (answerView.getCorrectAnswer().equals(answerView.getUserAnswer())){
            holder.img2.setBackgroundResource(R.drawable.ic_right1);
        }

        else {
            holder.img2.setBackgroundResource(R.drawable.ic_delete_cross);

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
        ImageView img1, img2;

        public AnswerHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.answer);
            user_ans = itemView.findViewById(R.id.user_ans);
            corr_ans = itemView.findViewById(R.id.corr_ans);
            cardView = itemView.findViewById(R.id.card);

            img1 = itemView.findViewById(R.id.img3);
            img2 = itemView.findViewById(R.id.img4);

            Typeface type = Typeface.createFromAsset(mCtx.getAssets(), "fonts/Roboto-Regular.ttf");
            textView.setTypeface(type);
            user_ans.setTypeface(type);
            corr_ans.setTypeface(type);


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
