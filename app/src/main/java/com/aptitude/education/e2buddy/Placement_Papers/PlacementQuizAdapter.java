package com.aptitude.education.e2buddy.Placement_Papers;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aptitude.education.e2buddy.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

public class PlacementQuizAdapter extends RecyclerView.Adapter<PlacementQuizAdapter.QuizHolder> {

    Context mCtx;
    List<String> list;

    public PlacementQuizAdapter(Context mCtx, List<String> list) {
        this.mCtx = mCtx;
        this.list = list;
    }

    @NonNull
    @Override
    public QuizHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mCtx).inflate(R.layout.placement_companies_list_items, parent, false);
        return new QuizHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizHolder holder, int position) {
        String name = list.get(position);
        Log.d("company name", name);
        holder.tvName.setText(name);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                PlacementQuizSheet placementQuizSheet = new PlacementQuizSheet();
                placementQuizSheet.setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
                placementQuizSheet.show(((FragmentActivity)mCtx).getSupportFragmentManager(), "quizapp2");


            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class QuizHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        public QuizHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
        }
    }
}
