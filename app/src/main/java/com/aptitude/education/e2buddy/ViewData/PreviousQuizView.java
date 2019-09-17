package com.aptitude.education.e2buddy.ViewData;

/**
 * Created by Matrix on 28-01-2019.
 */

public class PreviousQuizView {
    String quizdate;
    String quizid;

    public PreviousQuizView(String quizdate) {

        this.quizdate = quizdate;
    }

    public PreviousQuizView(String quizdate, String quizid) {
        this.quizdate = quizdate;
        this.quizid = quizid;
    }


    public String getQuizdate() {
        return quizdate;
    }

    public void setQuizdate(String quizdate) {
        this.quizdate = quizdate;
    }


    public String getQuizid() {
        return quizid;
    }

    public void setQuizid(String quizid) {
        this.quizid = quizid;
    }
}
