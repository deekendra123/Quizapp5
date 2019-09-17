package com.aptitude.education.e2buddy.Quiz_for_Group;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aptitude.education.e2buddy.R;
import com.aptitude.education.e2buddy.ViewData.GroupResultData;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class AdapterForGroupResult extends RecyclerView.Adapter<AdapterForGroupResult.GroupResultHolder> {

    Context mCtx;
    List<GroupResultData> list;
    DatabaseReference reference6;

    public AdapterForGroupResult(Context mCtx, List<GroupResultData> list) {
        this.mCtx = mCtx;
        this.list = list;
    }


    @NonNull
    @Override
    public GroupResultHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mCtx).inflate(R.layout.group_result_item_layout, parent, false);
        return new GroupResultHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupResultHolder holder, int position) {

        GroupResultData groupResultData = list.get(position);
        holder.player_name.setText(""+ groupResultData.getPlayer_name());
        holder.player_score.setText("score "+groupResultData.getScore());
        holder.player_question_answered.setText(""+groupResultData.getQuestion_answered()+"/10");
        reference6 = FirebaseDatabase.getInstance().getReference();

        if (position==0){

            holder.player_rank.setText("1");

        }

        if (position==1){

            holder.player_rank.setText("2");

        }
        if (position==2){

            holder.player_rank.setText("3");

        }
        if (position==3){

            holder.player_rank.setText("4");

        }
        if (position==4){

            holder.player_rank.setText("5");

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class GroupResultHolder extends RecyclerView.ViewHolder {

        TextView player_name;
        TextView player_score;
        TextView player_question_answered;
        TextView player_rank;
        public GroupResultHolder(View itemView) {
            super(itemView);

            player_name = itemView.findViewById(R.id.tvplayername);
            player_score = itemView.findViewById(R.id.tvplayerscore);
            player_question_answered = itemView.findViewById(R.id.tvquestionanswered);
            player_rank = itemView.findViewById(R.id.tvrank);
        }
    }
}
