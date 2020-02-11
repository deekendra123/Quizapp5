package com.aptitude.education.e2buddy.Question;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aptitude.education.e2buddy.R;
import com.aptitude.education.e2buddy.ViewData.PreviousQuizView;
import com.aptitude.education.e2buddy.ViewData.WordData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;


public class WordoftheDayAdapter extends RecyclerView.Adapter<WordoftheDayAdapter.QuizHolder> {

    Context mCtx;
    List<WordData> list;

    String systemdate;
    FirebaseAuth auth;
    OnItemClickListener listener;

    public WordoftheDayAdapter(Context mCtx, List<WordData> list, String systemdate) {
        this.mCtx = mCtx;
        this.list = list;
        this.systemdate = systemdate;
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

        View view = LayoutInflater.from(mCtx).inflate(R.layout.word_list_item, parent, false);
        return new QuizHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizHolder holder, int position) {
        WordData wordData = list.get(position);

        final String day = wordData.getDate();
        final String word = wordData.getWord();
        final String meaning = wordData.getMeaning();
        final String usage = wordData.getWord_usage();


        String newdate = wordData.getDate().replaceAll("-01", "-Jan")
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


        holder.tvDay.setText(""+newdate);
        holder.tvWord.setText(""+word);
        holder.tvMeaning.setText(""+meaning);
        holder.tvUsage.setText(""+usage);

    }

    @Override
    public int getItemCount() {

        return list.size() ;

    }

    public class QuizHolder extends RecyclerView.ViewHolder {

        TextView tvDay, tvWord, tvUsage, tvMeaning;
        public QuizHolder(final View view) {
            super(view);
            tvDay = view.findViewById(R.id.tvday);
            tvWord = view.findViewById(R.id.tvword);
            tvUsage = view.findViewById(R.id.tvusage);
            tvMeaning = view.findViewById(R.id.tvMeaning);

        }
    }
}
