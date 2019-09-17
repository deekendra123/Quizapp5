package com.aptitude.education.e2buddy.Question;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.aptitude.education.e2buddy.DisplayAnswer.ViewAnswerActivity;
import com.aptitude.education.e2buddy.R;
import com.aptitude.education.e2buddy.ViewData.AnswerView;
import com.aptitude.education.e2buddy.ViewData.HistoryView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matrix on 31-12-2018.
 */

public class UserQuizHistoryAdapter extends RecyclerView.Adapter<UserQuizHistoryAdapter.HistoryHolder> {

    Context mCtx;
    String newdate;
    List<HistoryView> historyViewList;
    OnItemClickListener listener;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ArrayList<AnswerView> answerViewList;
    String userId;

    public UserQuizHistoryAdapter(Context mCtx, List<HistoryView> historyViewList, String userId) {
        this.mCtx = mCtx;
        this.historyViewList = historyViewList;
        this.userId = userId;
    }


    public interface OnItemClickListener
    {

        void onItemClick(View itemView, int position);

    }

    public void setOnItemClickListener(QuizAdapter1.OnItemClickListener listener) {
        this.listener = (OnItemClickListener) listener;
    }


    @NonNull
    @Override
    public HistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_list_item, parent, false);
        return new HistoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final HistoryHolder holder, int position) {

        final HistoryView historyView = historyViewList.get(position);


        newdate = historyView.getId().replaceAll("-01", "-Jan")
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

        holder.textView.setText(newdate+"-2019");
        holder.tvcoins.setText(""+historyView.getCoins());


        sharedPreferences = mCtx.getSharedPreferences("quizpref", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        final String id1 = historyViewList.get(position).getQuizname();
        final String score1 = historyViewList.get(position).getDate();

        // Toast.makeText(mCtx, "deeke "+ historyView.getId() + "\n" + historyView.getQuizname(),Toast.LENGTH_SHORT).show();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

                databaseReference.child("daily_user_credit").child(userId).child(historyView.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {
                            long count = dataSnapshot.getChildrenCount();
                            //  Toast.makeText(getApplicationContext(), ""+ count,Toast.LENGTH_SHORT).show();
                            if (count<3){

                                historyViewList.clear();
                                Intent intent = new Intent(mCtx, StartQuizActivity.class);
                                intent.putExtra("quiz_date", historyView.getId());
                                mCtx.startActivity(intent);

                            }else {
                                Toast.makeText(mCtx, "You have already attemped 3 times", Toast.LENGTH_LONG).show();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });



    }


    @Override
    public int getItemCount() {
        return historyViewList.size();
    }

    public class HistoryHolder extends RecyclerView.ViewHolder{

        TextView textView;
        TextView tvcoins;
        public HistoryHolder(final View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.tvhistroy);
            tvcoins = itemView.findViewById(R.id.tvcoins);
            Typeface type = Typeface.createFromAsset(mCtx.getAssets(), "fonts/Roboto-Regular.ttf");
            textView.setTypeface(type);
            tvcoins.setTypeface(type);

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
