package com.aptitude.education.e2buddy.Question;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aptitude.education.e2buddy.DisplayAnswer.View_Answer_Dialog;
import com.aptitude.education.e2buddy.R;
import com.aptitude.education.e2buddy.ViewData.HistoryView;

import java.util.List;

/**
 * Created by Matrix on 31-12-2018.
 */

public class UserQuizHistoryAdapter extends RecyclerView.Adapter<UserQuizHistoryAdapter.HistoryHolder> {

    Context mCtx;
    String newdate;
    List<HistoryView> historyViewList;
    OnItemClickListener listener;
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

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
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

        holder.textView.setText("Date: "+newdate);
        holder.tvcoins.setText(""+historyView.getCoins());
        holder.tvcorrectans.setText("Answers: "+historyView.getCorrect_answer()+"/10");

        holder.itemView.setClickable(false);
        holder.btCorrecctAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                System.gc();

                FragmentManager fm = ((AppCompatActivity)mCtx).getSupportFragmentManager();
                                DialogFragment dialog = View_Answer_Dialog.newInstance();

                                 Bundle bundle = new Bundle();
                                bundle.putString("quiz_date",historyView.getId());
                                bundle.putString("curent_date",historyView.getDate());
                                bundle.putString("userid",userId);
                                dialog.setArguments(bundle);
                                dialog.show(fm,"tag");


            }
        });



    }


    @Override
    public int getItemCount() {
        return historyViewList.size();
    }

    public class HistoryHolder extends RecyclerView.ViewHolder{

        TextView textView;
        TextView tvcoins,tvcorrectans;
        AppCompatButton btCorrecctAnswer;
        public HistoryHolder(final View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.tvhistroy);
            tvcoins = itemView.findViewById(R.id.tvcoins);
            tvcorrectans = itemView.findViewById(R.id.tvcorrectans);
            btCorrecctAnswer = itemView.findViewById(R.id.btCorrecctAnswer);

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
