package com.aptitude.education.e2buddy.Question;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import com.aptitude.education.e2buddy.ViewData.PreviousQuizView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class DailyQuizes {

    Context mCtx;
    RecyclerView recyclerView;
    String p1,p2,p3,p4,p5,p6,p7,p8,p9,p10, systemdate;
    Date pd1,pd2,pd3,pd4,pd5,pd6,pd7,pd8,pd9,pd10;
    List<PreviousQuizView> previousQuizViewList;
    PreviousQuizAdapter previousQuizAdapter;
    DatabaseReference databaseReference;
    String qdate;

    public DailyQuizes(Context mCtx, RecyclerView recyclerView) {
        this.mCtx = mCtx;
        this.recyclerView = recyclerView;
    }
    public void getQuiz(){

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        systemdate = sdf.format(new Date());


        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
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

        databaseReference = FirebaseDatabase.getInstance().getReference();

        previousQuizViewList = new ArrayList<>();

        previousQuizAdapter = new PreviousQuizAdapter(mCtx, previousQuizViewList);

        databaseReference.child("daily_Question").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot data : dataSnapshot.getChildren()){
                    qdate = data.getKey();

                    if (systemdate.equals(qdate)
                            || p1.equals(qdate)
                            || p2.equals(qdate)
                            || p3.equals(qdate)
                            || p4.equals(qdate)
                            || p5.equals(qdate)
                            || p6.equals(qdate)
                            || p7.equals(qdate)
                            || p8.equals(qdate)
                            || p9.equals(qdate)
                            || p10.equals(qdate)) {

                        previousQuizViewList.add(new PreviousQuizView(qdate));
                        Collections.sort(previousQuizViewList, byDate);

                    }
                }


                recyclerView.setAdapter(previousQuizAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    static final Comparator<PreviousQuizView> byDate = new Comparator<PreviousQuizView>() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        public int compare(PreviousQuizView ord1, PreviousQuizView ord2) {
            Date d1 = null;
            Date d2 = null;
            try {
                d1 = sdf.parse(ord1.getQuizdate());
                d2 = sdf.parse(ord2.getQuizdate());
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return (d1.getTime() > d2.getTime() ? -1 : 1);     //descending
            //  return (d1.getTime() > d2.getTime() ? 1 : -1);     //ascending
        }
    };
}
