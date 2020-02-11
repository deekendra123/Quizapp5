package com.aptitude.education.e2buddy.Placement_Papers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.aptitude.education.e2buddy.DisplayAnswer.LeaderBoardAdapter;
import com.aptitude.education.e2buddy.R;
import com.aptitude.education.e2buddy.ViewData.BookmarkData;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class BookmarksAdapter extends RecyclerView.Adapter<BookmarksAdapter.PaperHolder> {


    Context bookmarkQuestions_dialog;
    List<BookmarkData> list;

    OnItemClickListener listener;


    public interface OnItemClickListener
    {
        void onItemClick(View itemView, int position);

    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public BookmarksAdapter(Context bookmarkQuestions_dialog, List<BookmarkData> list) {
        this.bookmarkQuestions_dialog = bookmarkQuestions_dialog;
        this.list = list;
    }


    @NonNull
    @Override
    public PaperHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(bookmarkQuestions_dialog).inflate(R.layout.bookmark_list_item, parent, false);
        return new PaperHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaperHolder holder, int position) {
        BookmarkData bookmarkData = list.get(position);

        holder.tvBookmark.setText("Question "+bookmarkData.getQuestion_id());

        if (bookmarkData.isBookmark()){

            holder.imgBookmark.setBackgroundResource(R.drawable.ic_bookmark1);
            holder.relativelayout.setBackgroundColor(Color.parseColor("#8AFF33"));
        }
        else {

            holder.imgBookmark.setVisibility(View.INVISIBLE);
            holder.relativelayout.setBackgroundColor(Color.parseColor("#FF334C"));

        }


      }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class PaperHolder extends RecyclerView.ViewHolder {

        TextView tvBookmark;
        RelativeLayout relativelayout;
        ImageView imgBookmark;

        public PaperHolder(View itemView) {
            super(itemView);
            tvBookmark = itemView.findViewById(R.id.tvBookmark);
            relativelayout = itemView.findViewById(R.id.relativelayout);
            imgBookmark = itemView.findViewById(R.id.imgBookmark);

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
