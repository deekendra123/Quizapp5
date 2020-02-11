package com.aptitude.education.e2buddy.Quiz_Tournament;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.aptitude.education.e2buddy.DisplayAnswer.LeaderBoardData;
import com.aptitude.education.e2buddy.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

public class Quiz_Tournament_Result_Adapter extends RecyclerView.Adapter<Quiz_Tournament_Result_Adapter.TournamentResultHolder> {

    Context mCtx;
    List<LeaderBoardData> list;
    ImageView playerimg;
    TextView playername, imgcup, tvplayerscore;
    DatabaseReference databaseReference;
    int tournament_point_earn;
    String tournament_quiz_date;

    public Quiz_Tournament_Result_Adapter(Context mCtx, List<LeaderBoardData> list, ImageView playerimg, TextView imgcup, TextView playername, String tournament_quiz_date, TextView tvplayerscore) {
        this.mCtx = mCtx;
        this.list = list;
        this.playerimg = playerimg;
        this.imgcup = imgcup;
        this.playername = playername;
        this.tournament_quiz_date = tournament_quiz_date;
        this.tvplayerscore = tvplayerscore;
    }

    @NonNull
    @Override
    public TournamentResultHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.tournament_result_item, parent, false);
        return new TournamentResultHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TournamentResultHolder holder, final int position) {

        final LeaderBoardData leaderBoardData = list.get(position);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        if (position==0){
            playername.setText(""+leaderBoardData.getName());

           // imgcup.setText("1");
            Picasso.get()
                    .load(leaderBoardData.getImage_Url())
                    .placeholder(R.drawable.userimg)
                    .fit()
                    .transform(holder.transformation)
                    .into(playerimg);

            databaseReference.child("tournament_leaderboard_points").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    int points = Integer.parseInt(dataSnapshot.child("0").getValue().toString());
                    tvplayerscore.setText(points+ " Points");
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            ViewGroup.LayoutParams lp= new ViewGroup.LayoutParams(holder.rl.getLayoutParams());
            lp.height=0;
            holder.rl.setLayoutParams(lp);

        }
        else {

            int rank = position + 1;
            holder.leaderdata.setText(leaderBoardData.getName());

            //holder.tvrank.setText("" + rank);

            Picasso.get()
                    .load(leaderBoardData.getImage_Url())
                    .placeholder(R.drawable.userimg)
                    .fit()
                    .transform(holder.transformation)
                    .into(holder.linearLayout);

            if (position==1){
                holder.tvrank.setBackgroundResource(R.drawable.ic_two);
            }
            if (position==2){
                holder.tvrank.setBackgroundResource(R.drawable.ic_three);
            }
            if (position==3){
                holder.tvrank.setBackgroundResource(R.drawable.ic_four);
            }
            if (position==4){
                holder.tvrank.setBackgroundResource(R.drawable.ic_five);
            }
            if (position==5){
                holder.tvrank.setBackgroundResource(R.drawable.ic_six);
            }
            if (position==6){
                holder.tvrank.setBackgroundResource(R.drawable.ic_seven);
            }
            if (position==7){
                holder.tvrank.setBackgroundResource(R.drawable.ic_eight);
            }
            if (position==8){
                holder.tvrank.setBackgroundResource(R.drawable.ic_nine);
            }
            if (position==9){
                holder.tvrank.setBackgroundResource(R.drawable.ic_ten);
            }

            databaseReference.child("tournament_leaderboard_points").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    int points = Integer.parseInt(dataSnapshot.child(String.valueOf(position)).getValue().toString());
                    holder.tvPoints.setText(points+ " Points");
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        databaseReference.child("tournament_date_joiners").child(tournament_quiz_date).child(leaderBoardData.getUserid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {

                    if (dataSnapshot.hasChild("tournament_point_earn")){

                    }
                    else {

                        databaseReference.child("tournament_date_joiners").child(tournament_quiz_date).child(leaderBoardData.getUserid()).child("tournament_point_earn").setValue(500);

                        databaseReference.child("final_Points").child(leaderBoardData.getUserid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {


                                if (dataSnapshot.hasChild("tournament_point_earn")){
                                    tournament_point_earn = Integer.parseInt(dataSnapshot.child("tournament_point_earn").getValue().toString());

                                    databaseReference.child("final_Points").child(leaderBoardData.getUserid()).child("tournament_point_earn").setValue(tournament_point_earn+500);

                                }
                               else {
                                    databaseReference.child("final_Points").child(leaderBoardData.getUserid()).child("tournament_point_earn").setValue(500);

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


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


    @Override
    public int getItemCount() {

        return Math.min(list.size(), 10);
    }

    public class TournamentResultHolder extends RecyclerView.ViewHolder {

        TextView leaderdata, tvrank, tvPoints;
        ImageView linearLayout;
        Transformation transformation;
        RelativeLayout rl;
        public TournamentResultHolder(final View itemView) {
            super(itemView);

            leaderdata = itemView.findViewById(R.id.tvdata);
            linearLayout = itemView.findViewById(R.id.img);
            tvrank = itemView.findViewById(R.id.tvrank);
            rl=itemView.findViewById(R.id.hh);
            tvPoints = itemView.findViewById(R.id.tvPoints);

            transformation = new RoundedTransformationBuilder()
                    .borderColor(mCtx.getResources().getColor(R.color.gray))
                    .borderWidthDp(0)
                    .cornerRadiusDp(50)
                    .oval(false)
                    .build();
        }
    }
}
