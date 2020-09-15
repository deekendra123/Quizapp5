package com.aptitude.education.e2buddy.Placement_Papers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.aptitude.education.e2buddy.Internship.InternshipDetailsBottemSheet;
import com.aptitude.education.e2buddy.R;
import com.aptitude.education.e2buddy.ViewData.AnswerView;
import com.aptitude.education.e2buddy.ViewData.InternshipData;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.QuizHolder> {

    Context mCtx;
    List<AnswerView> list;

    OnItemClickListener listener;


    public interface OnItemClickListener
    {

        void onItemClick(View itemView, int position);

    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    public AnswerAdapter(Context mCtx, List<AnswerView> list) {
        this.mCtx = mCtx;
        this.list = list;
    }

    @NonNull
    @Override
    public QuizHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mCtx).inflate(R.layout.placement_answer_list_items, parent, false);
        return new QuizHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizHolder holder, int position) {
        AnswerView answerView = list.get(position);

        holder.tvquestion.setText(answerView.getQuestionid());

        if (answerView.getCorrectAnswer().equals(answerView.getUserAnswer())){
            holder.imgNumber.setBackgroundResource(R.drawable.ic_bg3);
        }
        else {
            holder.imgNumber.setBackgroundResource(R.drawable.ic_bg4);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class QuizHolder extends RecyclerView.ViewHolder {

        TextView tvquestion;
        ImageView imgNumber;
        public QuizHolder(View itemView) {
            super(itemView);
            tvquestion = itemView.findViewById(R.id.tvquestion);

            imgNumber = itemView.findViewById(R.id.imgNumber);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (listener!=null){
                        int position = getAdapterPosition();
                        if (position!= RecyclerView.NO_POSITION){
                            listener.onItemClick(itemView,position);
                        }
                    }
                }
            });

        }
    }
}
