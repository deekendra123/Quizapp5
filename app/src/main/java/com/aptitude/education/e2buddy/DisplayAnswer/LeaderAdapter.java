package com.aptitude.education.e2buddy.DisplayAnswer;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.aptitude.education.e2buddy.R;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import java.util.List;

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

    @Override
    public void onBindViewHolder(@NonNull LeaderHolder holder, int position) {

        final LeaderBoardData leaderBoardData = list.get(position);

        holder.leaderdata.setText(leaderBoardData.getName());

        holder.score.setText("" + leaderBoardData.getScore());

        holder.layout.setBackgroundResource(R.drawable.ic_medal);


        Picasso.get()
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
                rankcounter++;

                if (userid.equals(data.getUserid())){
                    yourrank.setText(""+i1);
                }
            }

        }
    }

    @Override
    public int getItemCount() {
        return Math.min(list.size(), 10);
    }

    public class LeaderHolder extends RecyclerView.ViewHolder {

        TextView leaderdata, score;
        LinearLayout layout;
        ImageView linearLayout;
        Transformation transformation;

        public LeaderHolder(final View itemView) {
            super(itemView);

            leaderdata = itemView.findViewById(R.id.tvdata);

            score = itemView.findViewById(R.id.textView13);
            linearLayout = itemView.findViewById(R.id.img);
            layout = itemView.findViewById(R.id.scoresss);

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
