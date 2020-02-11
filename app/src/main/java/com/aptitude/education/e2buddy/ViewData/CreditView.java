package com.aptitude.education.e2buddy.ViewData;

/**
 * Created by Matrix on 27-12-2018.
 */

public class CreditView {

    int credit_points;
    String date_time;

    int correct_answers;
    String timeleft;



    public CreditView(int credit_points, String date_time, int correct_answers) {
        this.credit_points = credit_points;
        this.date_time = date_time;

        this.correct_answers = correct_answers;
    }

    public CreditView(int credit_points, int correct_answers) {
        this.credit_points = credit_points;
        this.correct_answers = correct_answers;
    }

    public CreditView(String timeleft,int correct_answers) {
        this.correct_answers = correct_answers;
        this.timeleft = timeleft;
    }


    public int getCredit_points() {
        return credit_points;
    }

    public void setCredit_points(int credit_points) {
        this.credit_points = credit_points;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

     public int getCorrect_answers() {
        return correct_answers;
    }

    public void setCorrect_answers(int correct_answers) {
        this.correct_answers = correct_answers;
    }

    public String getTimeleft() {
        return timeleft;
    }

    public void setTimeleft(String timeleft) {
        this.timeleft = timeleft;
    }
}
