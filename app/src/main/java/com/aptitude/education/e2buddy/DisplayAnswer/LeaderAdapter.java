package com.aptitude.education.e2buddy.DisplayAnswer;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aptitude.education.e2buddy.R;
import com.aptitude.education.e2buddy.ViewData.InsertRank;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Matrix on 08-02-2019.
 */

public class LeaderAdapter extends RecyclerView.Adapter<LeaderAdapter.LeaderHolder> {

    Context mCtx;
    List<LeaderBoardData> list;
    String userid ;
    TextView yourrank;

    int rankcounter =1;
    LeaderBoardAdapter.OnItemClickListener listener;

    public LeaderAdapter(Context mCtx, List<LeaderBoardData> list, String userid, TextView yourrank) {
        this.mCtx = mCtx;
        this.list = list;
        this.userid = userid;
        this.yourrank = yourrank;
    }


    public interface OnItemClickListener
    {
        void onItemClick(View itemView, int position);

    }

    public void setOnItemClickListener(LeaderBoardAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public LeaderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.leader_board_item, parent, false);
        return new LeaderHolder(view);
    }


    public void inserRank(){

        for (int i=0; i<list.size() ;i++){

            LeaderBoardData data = list.get(i);

            int i1 = i;
            if (i1==i){
                i1++;
                InsertRank insertRank = new InsertRank(data.getScore());

                // reference6.child(data.getUserid()).setValue(insertRank);
                rankcounter++;

                if (userid.equals(data.getUserid())){
                    yourrank.setText(""+i1);
                }
            }


        }


    }

    @Override
    public void onBindViewHolder(@NonNull LeaderHolder holder, int position) {

        final LeaderBoardData leaderBoardData = list.get(position);

        holder.leaderdata.setText(leaderBoardData.getName());

        holder.score.setText("" + leaderBoardData.getScore());

        Picasso.with(mCtx)
                .load(leaderBoardData.getImage_Url())
                .placeholder(R.drawable.userimg)
                .fit()
                .transform(holder.transformation)
                .into(holder.linearLayout);


        for (int i=0; i<list.size() ;i++){

            LeaderBoardData data = list.get(i);

            int i1 = i;
            if (i1==i){
                i1++;
                InsertRank insertRank = new InsertRank(data.getScore());

                //reference6.child(data.getUserid()).setValue(insertRank);
                rankcounter++;

                if (userid.equals(data.getUserid())){
                    yourrank.setText(""+i1);
                }
            }

        }


        if (position==0){

            holder.layout.setBackgroundResource(R.drawable.ic_medal);

        }

        if (position==1){

            holder.layout.setBackgroundResource(R.drawable.ic_medal);

        }
        if (position==2){

            holder.layout.setBackgroundResource(R.drawable.ic_medal);

        }
        if (position==3){

            holder.layout.setBackgroundResource(R.drawable.ic_medal);

        }
        if (position==4){

            holder.layout.setBackgroundResource(R.drawable.ic_medal);

        }
        if (position==5){

            holder.layout.setBackgroundResource(R.drawable.ic_medal);

        }
        if (position==6){

            holder.layout.setBackgroundResource(R.drawable.ic_medal);

        }
        if (position==7){

            holder.layout.setBackgroundResource(R.drawable.ic_medal);

        }
        if (position==8){

            holder.layout.setBackgroundResource(R.drawable.ic_medal);

        }
        if (position==9){

            holder.layout.setBackgroundResource(R.drawable.ic_medal);

        }

    }

    @Override
    public int getItemCount() {
        return Math.min(list.size(), 10);

        // return list.size();
    }

    public class LeaderHolder extends RecyclerView.ViewHolder {

        TextView leaderdata,rack, score;
        TextView imageView;
        LinearLayout layout;
        ImageView linearLayout;
        Transformation transformation;

        public LeaderHolder(final View itemView) {
            super(itemView);

            leaderdata = itemView.findViewById(R.id.tvdata);

            score = itemView.findViewById(R.id.textView13);
            linearLayout = itemView.findViewById(R.id.img);
            layout = itemView.findViewById(R.id.scoresss);

            Typeface type = Typeface.createFromAsset(mCtx.getAssets(), "fonts/Roboto-Regular.ttf");
            leaderdata.setTypeface(type);
            score.setTypeface(type);

            transformation = new RoundedTransformationBuilder()
                    .borderColor(mCtx.getResources().getColor(R.color.gray))
                    .borderWidthDp(0)
                    .cornerRadiusDp(50)
                    .oval(false)
                    .build();
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
