package com.aptitude.education.e2buddy.ViewData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Matrix on 26-12-2018.
 */

public class InsertAnswer {

    String useranswer;
    String datetime;
    long timeleft;

    public InsertAnswer(String useranswer, String datetime) {
        this.useranswer = useranswer;
        this.datetime = datetime;

    }


    public InsertAnswer(long timeleft) {
        this.timeleft = timeleft;
    }

    public InsertAnswer(String useranswer, String datetime, long timeleft) {
        this.useranswer = useranswer;
        this.datetime = datetime;
        this.timeleft = timeleft;
    }


    public String getUseranswer() {
        return useranswer;
    }

    public void setUseranswer(String useranswer) {
        this.useranswer = useranswer;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public long getTimeleft() {
        return timeleft;
    }

    public void setTimeleft(long timeleft) {
        this.timeleft = timeleft;
    }
}
