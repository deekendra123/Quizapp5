package com.aptitude.education.e2buddy.ViewData;

/**
 * Created by Matrix on 27-12-2018.
 */

public class CreditView {

    int credit_points;
    String date_time;
    String quiz_name;
    int correct_answers;

    public CreditView(int credit_points, String date_time, String quiz_name, int correct_answers) {
        this.credit_points = credit_points;
        this.date_time = date_time;
        this.quiz_name = quiz_name;
        this.correct_answers = correct_answers;
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

    public String getQuiz_name() {
        return quiz_name;
    }

    public void setQuiz_name(String quiz_name) {
        this.quiz_name = quiz_name;
    }

    public int getCorrect_answers() {
        return correct_answers;
    }

    public void setCorrect_answers(int correct_answers) {
        this.correct_answers = correct_answers;
    }
}
