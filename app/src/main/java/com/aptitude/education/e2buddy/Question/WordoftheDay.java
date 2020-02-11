package com.aptitude.education.e2buddy.Question;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import com.aptitude.education.e2buddy.ViewData.WordData;
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

public class WordoftheDay {
    Context mCtx;
    RecyclerView recyclerView;

    String p1,p2,p3,p4, systemdate;
    Date pd1,pd2,pd3,pd4;
    List<WordData> list;
    WordoftheDayAdapter wordoftheDayAdapter;
    DatabaseReference databaseReference;

    public WordoftheDay(Context mCtx, RecyclerView recyclerView) {
        this.mCtx = mCtx;
        this.recyclerView = recyclerView;
    }

    public void getWordoftheDay(){



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


            pd1 = c1.getTime();
            p1 = dateFormat.format(pd1);
            pd2 = c2.getTime();
            p2 = dateFormat.format(pd2);

            pd3 = c3.getTime();
            p3 = dateFormat.format(pd3);

            pd4 = c4.getTime();
            p4 = dateFormat.format(pd4);


            databaseReference = FirebaseDatabase.getInstance().getReference();

            list = new ArrayList<>();

            wordoftheDayAdapter = new WordoftheDayAdapter(mCtx, list, systemdate);

            databaseReference.child("word_of_the_month").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot data : dataSnapshot.getChildren()){
                        String qdate = data.getKey();



                        if (systemdate.equals(qdate)
                                || p1.equals(qdate)
                                || p2.equals(qdate)
                                || p3.equals(qdate)
                                || p4.equals(qdate)
                                ) {

                            databaseReference.child("word_of_the_month").child(qdate).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {


                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                                        String wordId = dataSnapshot1.getKey();
                                        String word = dataSnapshot1.child("word").getValue(String.class);
                                        String meaning = dataSnapshot1.child("meaning").getValue(String.class);
                                        String word_usage = dataSnapshot1.child("word_usage").getValue(String.class);
                                        
                                        list.add(new WordData(qdate, meaning, word, word_usage));
                                    }
                                    Collections.sort(list, byDate);
                                    recyclerView.setAdapter(wordoftheDayAdapter);


                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });


                        }

                    }



                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

    static final Comparator<WordData> byDate = new Comparator<WordData>() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM");

        public int compare(WordData ord1, WordData ord2) {
            Date d1 = null;
            Date d2 = null;
            try {
                d1 = sdf.parse(ord1.getDate());
                d2 = sdf.parse(ord2.getDate());
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return (d1.getTime() > d2.getTime() ? -1 : 1);     //descending
            //  return (d1.getTime() > d2.getTime() ? 1 : -1);     //ascending
        }
    };

}
