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

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.aptitude.education.e2buddy.Internship.InternshipDetailsBottemSheet;
import com.aptitude.education.e2buddy.R;
import com.aptitude.education.e2buddy.ViewData.InternshipData;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.QuizHolder> {

    Context mCtx;
    List<String> list;

    public AnswerAdapter(Context mCtx, List<String> list) {
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
        String name = list.get(position);
      //  holder.tvquestion.setText(name);


        if (position==0){
            holder.imgNumber.setBackgroundResource(R.drawable.ic_one);
        }
        else if (position ==1){
            holder.imgNumber.setBackgroundResource(R.drawable.ic_two);

        }

        else if (position ==2){
            holder.imgNumber.setBackgroundResource(R.drawable.ic_three);

        }
        else if (position ==3){
            holder.imgNumber.setBackgroundResource(R.drawable.ic_four);

        }
        else if (position ==4){
            holder.imgNumber.setBackgroundResource(R.drawable.ic_five);

        }
        else if (position ==5){
            holder.imgNumber.setBackgroundResource(R.drawable.ic_six);

        }
        else if (position ==6){
            holder.imgNumber.setBackgroundResource(R.drawable.ic_seven);

        }
        else if (position ==7){
            holder.imgNumber.setBackgroundResource(R.drawable.ic_eight);

        }
        else if (position ==8){
            holder.imgNumber.setBackgroundResource(R.drawable.ic_nine);

        }
        else if (position ==9){
            holder.imgNumber.setBackgroundResource(R.drawable.ic_ten);

        }

        holder.itemView.setClickable(false);
        holder.imgNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                v.startAnimation(AnimationUtils.loadAnimation(mCtx, R.anim.ripple));


            /*    PlacementPaperAnswerDialog bottomSheet = new PlacementPaperAnswerDialog();

                Bundle bundle = new Bundle();
                bundle.putString("questionid",name);
                bottomSheet.setArguments(bundle);
                bottomSheet.show(((FragmentActivity)mCtx).getSupportFragmentManager(), "quizapp2");
*/
            }
        });
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
        }
    }
}
