package com.aptitude.education.e2buddy.Placement_Papers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aptitude.education.e2buddy.R;

import java.util.List;

public class PlacementPaperAdapter extends RecyclerView.Adapter<PlacementPaperAdapter.PaperHolder> {

    Context mCtx;
    List<String> list;

    public PlacementPaperAdapter(Context mCtx, List<String> list) {
        this.mCtx = mCtx;
        this.list = list;
    }

    @NonNull
    @Override
    public PaperHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mCtx).inflate(R.layout.placement_quiz_list_items, parent, false);
        return new PaperHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaperHolder holder, int position) {
        String name = list.get(position);
        holder.tvQuiz.setText(""+name);

        holder.itemView.setClickable(false);

        holder.tvQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // holder.tvQuiz.setTextColor(Color.parseColor("#ffffff"));

                Intent intent = new Intent(mCtx, Quiz_Instruction_Activity.class);
                mCtx.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class PaperHolder extends RecyclerView.ViewHolder {

        TextView tvQuiz;

        public PaperHolder(View itemView) {
            super(itemView);
            tvQuiz = itemView.findViewById(R.id.tvQuiz);
        }
    }
}
