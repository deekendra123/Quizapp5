package com.aptitude.education.e2buddy.Question;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aptitude.education.e2buddy.R;
import com.aptitude.education.e2buddy.ViewData.Quizdata;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Matrix on 21-11-2018.
 */

public class QuizAdapter1 extends RecyclerView.Adapter<QuizAdapter1.QuizHolder>{

    private Context mCtx;
    private List<Quizdata> quizlist;


    OnItemClickListener listener;
    DatabaseReference databaseReference;
    String quizdate, quizname1, id,systemdate, p1,p2,p3,p4,p5,p6,p7,p8,p9,p10,p11,p12,p13,p14,p15,p16,p17,p18,p19,p20,p21,p22,p23,p24,p25,p26,p27,p28,p29,p30,p31;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    Date  pd1,pd2,pd3,pd4,pd5,pd6,pd7,pd8,pd9,pd10,pd11,pd12,pd13,pd14,pd15,pd16,pd17,pd18,pd19,pd20,pd21,pd22,pd23,pd24,pd25,pd26,pd27,pd28,pd29,pd30,
    pd31;
    public interface OnItemClickListener
    {
        void onItemClick(View itemView, int position);

    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public QuizAdapter1(Context mCtx, List<Quizdata> quizlist) {
        this.mCtx = mCtx;
        this.quizlist = quizlist;
    }


    public class QuizHolder extends RecyclerView.ViewHolder {


        public TextView quizname;

        public QuizHolder(final View itemView) {
            super(itemView);
            mCtx = itemView.getContext();
            quizname = itemView.findViewById(R.id.textView4);


            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM");
            systemdate = sdf.format(new Date());


            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM");
            Date myDate = null;
            try {
                myDate = dateFormat.parse(systemdate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar c1 = Calendar.getInstance();
            c1.setTime(myDate);
            c1.add(Calendar.DATE, -1);

            Calendar c2 = Calendar.getInstance();
            c2.setTime(myDate);
            c2.add(Calendar.DATE, -2);

            Calendar c3 = Calendar.getInstance();
            c3.setTime(myDate);
            c3.add(Calendar.DATE, -3);

            Calendar c4 = Calendar.getInstance();
            c4.setTime(myDate);
            c4.add(Calendar.DATE, -4);

            Calendar c5 = Calendar.getInstance();
            c5.setTime(myDate);
            c5.add(Calendar.DATE, -5);

            Calendar c6 = Calendar.getInstance();
            c6.setTime(myDate);
            c6.add(Calendar.DATE, -6);
            Calendar c7 = Calendar.getInstance();
            c7.setTime(myDate);
            c7.add(Calendar.DATE, -7);
            Calendar c8 = Calendar.getInstance();
            c8.setTime(myDate);
            c8.add(Calendar.DATE, -8);
            Calendar c9 = Calendar.getInstance();
            c9.setTime(myDate);
            c9.add(Calendar.DATE, -9);
            Calendar c10 = Calendar.getInstance();
            c10.setTime(myDate);
            c10.add(Calendar.DATE, -10);
            Calendar c11 = Calendar.getInstance();
            c11.setTime(myDate);
            c11.add(Calendar.DATE, -11);
            Calendar c12 = Calendar.getInstance();
            c12.setTime(myDate);
            c12.add(Calendar.DATE, -12);
            Calendar c13 = Calendar.getInstance();
            c13.setTime(myDate);
            c13.add(Calendar.DATE, -13);
            Calendar c14 = Calendar.getInstance();
            c14.setTime(myDate);
            c14.add(Calendar.DATE, -14);
            Calendar c15 = Calendar.getInstance();
            c15.setTime(myDate);
            c15.add(Calendar.DATE, -15);
            Calendar c16 = Calendar.getInstance();
            c16.setTime(myDate);
            c16.add(Calendar.DATE, -16);
            Calendar c17 = Calendar.getInstance();
            c17.setTime(myDate);
            c17.add(Calendar.DATE, -17);


            Calendar c18 = Calendar.getInstance();
            c18.setTime(myDate);
            c18.add(Calendar.DATE, -18);
            Calendar c19 = Calendar.getInstance();
            c19.setTime(myDate);
            c19.add(Calendar.DATE, -19);
            Calendar c20 = Calendar.getInstance();
            c20.setTime(myDate);
            c20.add(Calendar.DATE, -20);
            Calendar c21 = Calendar.getInstance();
            c21.setTime(myDate);
            c21.add(Calendar.DATE, -21);
            Calendar c22 = Calendar.getInstance();
            c22.setTime(myDate);
            c22.add(Calendar.DATE, -22);
            Calendar c23 = Calendar.getInstance();
            c23.setTime(myDate);
            c23.add(Calendar.DATE, -23);
            Calendar c24 = Calendar.getInstance();
            c24.setTime(myDate);
            c24.add(Calendar.DATE, -24);
            Calendar c25 = Calendar.getInstance();
            c25.setTime(myDate);
            c25.add(Calendar.DATE, -25);
            Calendar c26 = Calendar.getInstance();
            c26.setTime(myDate);
            c26.add(Calendar.DATE, -26);
            Calendar c27 = Calendar.getInstance();
            c27.setTime(myDate);
            c27.add(Calendar.DATE, -27);
            Calendar c28 = Calendar.getInstance();
            c28.setTime(myDate);
            c28.add(Calendar.DATE, -28);
            Calendar c29 = Calendar.getInstance();
            c29.setTime(myDate);
            c29.add(Calendar.DATE, -29);
            Calendar c30 = Calendar.getInstance();
            c30.setTime(myDate);
            c30.add(Calendar.DATE, -30);
            Calendar c31 = Calendar.getInstance();
            c31.setTime(myDate);
            c31.add(Calendar.DATE, -31);

            pd1 = c1.getTime();
            p1 = dateFormat.format(pd1);
            pd2 = c2.getTime();
            p2 = dateFormat.format(pd2);

            pd3 = c3.getTime();
            p3 = dateFormat.format(pd3);

            pd4 = c4.getTime();
            p4 = dateFormat.format(pd4);

            pd5 = c5.getTime();
            p5 = dateFormat.format(pd5);

            pd6 = c6.getTime();
            p6 = dateFormat.format(pd6);

            pd7 = c7.getTime();
            p7 = dateFormat.format(pd7);

            pd8 = c8.getTime();
            p8 = dateFormat.format(pd8);

            pd9 = c9.getTime();
            p9 = dateFormat.format(pd9);

            pd10 = c10.getTime();
            p10 = dateFormat.format(pd10);

            pd11 = c11.getTime();
            p11 = dateFormat.format(pd11);

            pd12 = c12.getTime();
            p12 = dateFormat.format(pd12);

            pd13 = c13.getTime();
            p13 = dateFormat.format(pd13);

            pd14 = c14.getTime();
            p14 = dateFormat.format(pd14);

            pd15 = c15.getTime();
            p15 = dateFormat.format(pd15);

            pd16 = c16.getTime();
            p16 = dateFormat.format(pd16);

            pd17 = c17.getTime();
            p17 = dateFormat.format(pd17);

            pd18 = c18.getTime();
            p18 = dateFormat.format(pd18);

            pd19 = c19.getTime();
            p19 = dateFormat.format(pd19);

            pd20 = c20.getTime();
            p20 = dateFormat.format(pd20);

            pd21 = c21.getTime();
            p21 = dateFormat.format(pd21);

            pd22 = c22.getTime();
            p22 = dateFormat.format(pd22);

            pd23 = c23.getTime();
            p23 = dateFormat.format(pd23);

            pd24 = c24.getTime();
            p24 = dateFormat.format(pd24);

            pd25 = c25.getTime();
            p25 = dateFormat.format(pd25);

            pd26 = c26.getTime();
            p26 = dateFormat.format(pd26);

            pd27 = c27.getTime();
            p27 = dateFormat.format(pd27);

            pd28 = c28.getTime();
            p28 = dateFormat.format(pd28);

            pd29 = c29.getTime();
            p29 = dateFormat.format(pd29);

            pd30 = c30.getTime();
            p30 = dateFormat.format(pd30);

            pd31 = c31.getTime();
            p31 = dateFormat.format(pd31);

          //  Toast.makeText(mCtx, "deeke "+ p1 + p2,Toast.LENGTH_SHORT).show();



            databaseReference = FirebaseDatabase.getInstance().getReference();

            databaseReference.child("quiz_category").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot data :dataSnapshot.getChildren()){
                        id = data.getKey();

                        quizdate = data.child("date").getValue().toString();
                        quizname1 = data.child("name").getValue().toString();

                      //  Toast.makeText(mCtx, "" + quizdate, Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


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



    @NonNull
    @Override
    public QuizHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater= LayoutInflater.from(mCtx);
        View view =  layoutInflater.inflate(R.layout.quiz_list, null);
        return new QuizHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final QuizHolder holder, int position) {


        final Quizdata quizdata = quizlist.get(position);
        holder.quizname.setText("Quiz " + quizdata.getId() + "\n" + quizdata.getDate());

        final String id = quizlist.get(position).getId();
        final String name = quizlist.get(position).getDate();

        final  String qdate = quizlist.get(position).getDate();


        sharedPreferences = mCtx.getSharedPreferences("quizpref", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                editor.putString("quizid", id);
                editor.putString("quizname", name);
                editor.commit();

                if (systemdate.equals(quizdata.getDate())
                        || p1.equals(quizdata.getDate())
                        || p2.equals(quizdata.getDate())
                        || p3.equals(quizdata.getDate())
                        || p4.equals(quizdata.getDate())
                        || p5.equals(quizdata.getDate())
                        || p6.equals(quizdata.getDate())
                        || p7.equals(quizdata.getDate())
                        || p8.equals(quizdata.getDate())
                        || p9.equals(quizdata.getDate())
                        || p10.equals(quizdata.getDate())
                        || p11.equals(quizdata.getDate())
                        || p12.equals(quizdata.getDate())
                        || p13.equals(quizdata.getDate())
                        || p14.equals(quizdata.getDate())
                        || p15.equals(quizdata.getDate())
                        || p16.equals(quizdata.getDate())
                        || p17.equals(quizdata.getDate())
                        || p18.equals(quizdata.getDate())
                        || p19.equals(quizdata.getDate())
                        || p20.equals(quizdata.getDate())
                        || p21.equals(quizdata.getDate())
                        || p22.equals(quizdata.getDate())
                        || p23.equals(quizdata.getDate())
                        || p24.equals(quizdata.getDate())
                        || p25.equals(quizdata.getDate())
                        || p26.equals(quizdata.getDate())
                        || p27.equals(quizdata.getDate())
                        || p28.equals(quizdata.getDate())
                        || p29.equals(quizdata.getDate())
                        || p30.equals(quizdata.getDate())
                        || p31.equals(quizdata.getDate())){

                    holder.itemView.setClickable(true);

                    Intent intent = new Intent(mCtx, StartQuizActivity.class);
                    mCtx.startActivity(intent);

                }

                else {
                    holder.itemView.setClickable(false);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return quizlist.size();
    }
}
