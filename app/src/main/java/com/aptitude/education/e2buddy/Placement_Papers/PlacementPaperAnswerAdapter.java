package com.aptitude.education.e2buddy.Placement_Papers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.aptitude.education.e2buddy.R;
import com.aptitude.education.e2buddy.ViewData.AnswerView;

import java.util.List;

public class PlacementPaperAnswerAdapter extends RecyclerView.Adapter<PlacementPaperAnswerAdapter.AnswerHolder>{

    private Context mCtx;
    private List<AnswerView> answerViewList;
    public PlacementPaperAnswerAdapter(Context mCtx, List<AnswerView> answerViewList) {
        this.mCtx = mCtx;
        this.answerViewList = answerViewList;
    }


    @NonNull
    @Override
    public AnswerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.placement_paper_answer_list_item, parent, false);
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


        }
    }


}
