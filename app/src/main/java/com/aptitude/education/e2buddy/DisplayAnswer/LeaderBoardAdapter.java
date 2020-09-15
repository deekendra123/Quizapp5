package com.aptitude.education.e2buddy.DisplayAnswer;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aptitude.education.e2buddy.R;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Matrix on 31-01-2019.
 */


public class LeaderBoardAdapter extends RecyclerView.Adapter<LeaderBoardAdapter.LeaderBoardHolder> {

    Context mCtx;
    List<LeaderBoardData> list;

    String userid ;
    TextView yourrank;
    DatabaseReference reference;
    OnItemClickListener listener;
    int rankcounter =1;


    public LeaderBoardAdapter(Context mCtx, List<LeaderBoardData> list, String userid, TextView yourrank) {
        this.mCtx = mCtx;
        this.list = list;
        this.userid = userid;
        this.yourrank = yourrank;
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
    public LeaderBoardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mCtx).inflate(R.layout.leader_board_item, parent, false);
        return new LeaderBoardHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull LeaderBoardHolder holder, int position) {

        reference = FirebaseDatabase.getInstance().getReference();


        LeaderBoardData leaderBoardData = list.get(position);

        holder.leaderdata.setText(leaderBoardData.getName());

        holder.score.setText("" + leaderBoardData.getScore());
        holder.layout.setBackgroundResource(R.drawable.ic_medal);


        Picasso.get()
                .load(leaderBoardData.getImage_Url())
                .placeholder(R.drawable.userimg)
                .fit()
                .into(holder.linearLayout);


/*
        for (int i=0; i<list.size() ;i++){

            LeaderBoardData data = list.get(i);

            int i1 = i;
            if (i1==i){
                i1++;
                rankcounter++;
                if (userid.equals(data.getUserid())){
                    yourrank.setText(""+i1);

                    //  Toast.makeText(mCtx,"rank : "+ i1,Toast.LENGTH_SHORT).show();
                }

            }

        }
*/


    }

    @Override
    public int getItemCount() {

        return Math.min(list.size(), 10);
    }


    public class LeaderBoardHolder extends RecyclerView.ViewHolder {

        TextView leaderdata,rack, score;
        TextView imageView;
        LinearLayout layout;
        CircleImageView linearLayout;
        public LeaderBoardHolder(final View itemView) {
            super(itemView);
            leaderdata = itemView.findViewById(R.id.tvdata);

            score = itemView.findViewById(R.id.textView13);
            linearLayout = itemView.findViewById(R.id.img);
            layout = itemView.findViewById(R.id.scoresss);


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
