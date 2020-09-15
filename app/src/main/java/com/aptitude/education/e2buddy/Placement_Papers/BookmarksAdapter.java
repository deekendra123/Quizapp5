package com.aptitude.education.e2buddy.Placement_Papers;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.aptitude.education.e2buddy.R;
import com.aptitude.education.e2buddy.ViewData.BookmarkData;
import java.util.List;

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

        View view = LayoutInflater.from(bookmarkQuestions_dialog).inflate(R.layout.bookmark_list_items, parent, false);
        return new PaperHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaperHolder holder, int position) {
        BookmarkData bookmarkData = list.get(position);

        holder.tvBookmark.setText("Question    "+bookmarkData.getQuestion_id());


        if (bookmarkData.isBookmark() && (bookmarkData.getAnswer().equals("null") || !bookmarkData.getAnswer().equals("null"))){

         holder.relativelayout.setBackgroundResource(R.drawable.ic_bg5);

       }
        else if (!bookmarkData.getAnswer().equals("null")){

            holder.relativelayout.setBackgroundResource(R.drawable.ic_bg3);

        }
        else {

            holder.relativelayout.setBackgroundResource(R.drawable.ic_bg4);

        }
      }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class PaperHolder extends RecyclerView.ViewHolder {

        TextView tvBookmark;
        RelativeLayout relativelayout;
        public PaperHolder(View itemView) {
            super(itemView);
            tvBookmark = itemView.findViewById(R.id.tvquestion);
            relativelayout = itemView.findViewById(R.id.relativelayoutBookmark);

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
