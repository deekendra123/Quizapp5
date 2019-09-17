package com.aptitude.education.e2buddy.ViewData;

/**
 * Created by Matrix on 22-12-2018.
 */

public class Quizdata {


    public String id;
    //public String name;
    public String date;

    public Quizdata(String id, String date) {
        this.id = id;
        this.date = date;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
