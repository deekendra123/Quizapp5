package com.aptitude.education.e2buddy.Question;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.aptitude.education.e2buddy.R;
import com.aptitude.education.e2buddy.ViewData.PreviousQuizView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.List;


public class PreviousQuizAdapter extends RecyclerView.Adapter<PreviousQuizAdapter.QuizHolder> {

    Context mCtx;
    List<PreviousQuizView> previousQuizViewList;

    String newdate,userid;
    FirebaseAuth auth;
    OnItemClickListener listener;

    public PreviousQuizAdapter(Context mCtx, List<PreviousQuizView> previousQuizViewList) {
        this.mCtx = mCtx;
        this.previousQuizViewList = previousQuizViewList;
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

        View view = LayoutInflater.from(mCtx).inflate(R.layout.daily_quiz_item, parent, false);
        return new QuizHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizHolder holder, int position) {
        PreviousQuizView previousQuizView = previousQuizViewList.get(position);

        newdate = previousQuizView.getQuizdate().replaceAll("-01", "-Jan")
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


        auth = FirebaseAuth.getInstance();
        final FirebaseUser user = auth.getCurrentUser();
        userid = user.getUid();


        holder.qdate.setText(newdate+"");
        final String id = previousQuizViewList.get(position).getQuizid();
        final String date = previousQuizViewList.get(position).getQuizdate();


        if (position==0){
            holder.imageView.setBackgroundResource(R.drawable.ic_playbutton);
            holder.qdate.setText("Today Quiz");
            Animation myFadeInAnimation = AnimationUtils.loadAnimation(mCtx, R.anim.play_anim);
            holder.imageView.startAnimation(myFadeInAnimation);

        }


        final AlphaAnimation buttonClick = new AlphaAnimation(1F, 1F);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

                databaseReference.child("daily_user_credit").child(userid).child(date).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {
                            long count = dataSnapshot.getChildrenCount();
                            //  Toast.makeText(mCtx, ""+ count,Toast.LENGTH_SHORT).show();
                            if (count<3){

                                Intent intent = new Intent(mCtx, StartQuizActivity.class);
                                intent.putExtra("quiz_date", date);
                                mCtx.startActivity(intent);

                            }else {
                                Toast.makeText(mCtx, "You have already attemped 3 times", Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                view.startAnimation(buttonClick);

            }
        });


    }

    @Override
    public int getItemCount() {


        return previousQuizViewList.size() ;

    }

    public class QuizHolder extends RecyclerView.ViewHolder {

        TextView qdate;
        ImageView imageView;
        public QuizHolder(final View itemView) {
            super(itemView);

            //   qid = itemView.findViewById(R.id.qid);
            qdate = itemView.findViewById(R.id.qdate);
            imageView = itemView.findViewById(R.id.imageView);

        }
    }
}
