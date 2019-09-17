package com.aptitude.education.e2buddy.DisplayAnswer;

import android.content.Context;
import android.support.annotation.NonNull;

/**
 * Created by Matrix on 31-01-2019.
 */

public class LeaderBoardData implements Comparable{
    String userid;
    String name;
    public  int score;
    String image_Url;
    String date;
    String quiz_date;
    int filterdate;
    Context mct;

    public LeaderBoardData(String userid, String name, int score, String image_Url) {
        this.userid = userid;
        this.name = name;
        this.score = score;
        this.image_Url = image_Url;
    }




    public LeaderBoardData(String userid, String name, int score, String image_Url, String date, String quiz_date) {
        this.userid = userid;
        this.name = name;
        this.score = score;
        this.image_Url = image_Url;
        this.date = date;
        this.quiz_date = quiz_date;
    }

    public LeaderBoardData(int filterdate, Context mct) {
        this.filterdate = filterdate;
        this.mct = mct;
    }


    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }


    @Override
    public int compareTo(@NonNull Object o) {

        int compare = getScore();
        return this.score-compare;
    }

    public String getImage_Url() {
        return image_Url;
    }

    public void setImage_Url(String image_Url) {
        this.image_Url = image_Url;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getFilterdate() {
        return filterdate;
    }

    public void setFilterdate(int filterdate) {
        this.filterdate = filterdate;
    }

    public String getQuiz_date() {
        return quiz_date;
    }

    public void setQuiz_date(String quiz_date) {
        this.quiz_date = quiz_date;
    }
}
